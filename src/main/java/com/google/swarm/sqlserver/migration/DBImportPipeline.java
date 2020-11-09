/* Copyright 2018 Google LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.google.swarm.sqlserver.migration;

import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DATA_SET;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DLP_CONFIG_BUCKET;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DLP_CONFIG_OBJECT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.JDBC_SPEC;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.OFFSET_COUNT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.PROJECT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.TEMP_LOCATION;

import com.google.api.services.bigquery.model.TableRow;
import com.google.common.collect.ImmutableList;
import com.google.swarm.sqlserver.migration.common.BigQueryTableRowDoFn;
import com.google.swarm.sqlserver.migration.common.CreateTableMapDoFn;
import com.google.swarm.sqlserver.migration.common.DLPTokenizationDoFn;
import com.google.swarm.sqlserver.migration.common.DeterministicKeyCoder;
import com.google.swarm.sqlserver.migration.common.SqlTable;
import com.google.swarm.sqlserver.migration.common.TableToDbRowFn;
import com.google.swarm.sqlserver.migration.common.fileImport.DataImportPipelineOptions;
import com.google.swarm.sqlserver.migration.common.sink.PipelineBqSink;
import com.google.swarm.sqlserver.migration.utils.CustomValueProvider;
import java.util.Map;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.CoderProviders;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.AfterProcessingTime;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBImportPipeline {

  public static final Logger LOG = LoggerFactory.getLogger(DBImportPipeline.class);

  public void runDBImportPipeline(String[] args, Map<String, String> dataImportPipeLineConfig) {
    DataImportPipelineOptions dbImportPipelineOptions =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(DataImportPipelineOptions.class);

    runDBImport(enrichOptions(dbImportPipelineOptions, dataImportPipeLineConfig));
  }

  private DataImportPipelineOptions enrichOptions(
      DataImportPipelineOptions dataImportPipelineOptions,
      Map<String, String> dataImportPipelineConfig) {

    dataImportPipelineOptions
        .setProject(dataImportPipelineConfig.get(PROJECT));
    dataImportPipelineOptions.setDataSet(dataImportPipelineConfig.get(DATA_SET));
    dataImportPipelineOptions.setTempLocation(dataImportPipelineConfig.get(TEMP_LOCATION));
    dataImportPipelineOptions.setDLPConfigBucket(dataImportPipelineConfig.get(DLP_CONFIG_BUCKET));
    dataImportPipelineOptions.setDLPConfigObject(dataImportPipelineConfig.get(DLP_CONFIG_OBJECT));
    dataImportPipelineOptions.setJDBCSpec(dataImportPipelineConfig.get(JDBC_SPEC));
    dataImportPipelineOptions.setOffsetCount(
        Integer.valueOf(dataImportPipelineConfig.get(OFFSET_COUNT)));

    return dataImportPipelineOptions;
  }

  @SuppressWarnings("serial")
  public static void runDBImport(DataImportPipelineOptions options) {

    Pipeline p = Pipeline.create(options);

    p.getCoderRegistry()
        .registerCoderProvider(
            CoderProviders.fromStaticMethods(SqlTable.class, DeterministicKeyCoder.class));

    PCollection<ValueProvider<String>> jdbcString =
        p.apply("Check DB Properties",
            Create.of(CustomValueProvider.getValueProviderOf(options.getJDBCSpec())));

    PCollectionTuple tableCollection =
        jdbcString.apply(
            "Create Table Map",
            ParDo.of(
                new CreateTableMapDoFn(
                    options.getExcludedTables(),
                    options.getDLPConfigBucket(),
                    options.getDLPConfigObject(),
                    options.getJDBCSpec(),
                    options.getDataSet(),
                    options.as(GcpOptions.class).getProject()))
                .withOutputTags(
                    CreateTableMapDoFn.successTag,
                    TupleTagList.of(CreateTableMapDoFn.deadLetterTag)));

    PCollectionTuple dbRowKeyValue =
        tableCollection
            .get(CreateTableMapDoFn.successTag)
            .apply(
                "Create DB Rows",
                ParDo.of(new TableToDbRowFn(
                    CustomValueProvider.getValueProviderOf(options.getJDBCSpec()),
                    CustomValueProvider.getValueProviderOf(options.getOffsetCount())))
                    .withOutputTags(
                        TableToDbRowFn.successTag, TupleTagList.of(TableToDbRowFn.deadLetterTag)));

    PCollection<KV<SqlTable, TableRow>> successRecords =
        dbRowKeyValue
            .get(TableToDbRowFn.successTag)
            .apply(
                "DLP Tokenization",
                ParDo.of(new DLPTokenizationDoFn(options.as(GcpOptions.class).getProject())))
            .apply("Convert To BQ Row", ParDo.of(new BigQueryTableRowDoFn()))
            .apply(
                Window.<KV<SqlTable, TableRow>>into(FixedWindows.of(Duration.standardSeconds(30)))
                    .triggering(
                        AfterProcessingTime.pastFirstElementInPane().plusDelayOf(Duration.ZERO))
                    .discardingFiredPanes()
                    .withAllowedLateness(Duration.ZERO));

    WriteResult writeResult = PipelineBqSink.getWriteResult(options, successRecords);

    writeResult
        .getFailedInserts()
        .apply(
            "LOG BQ Failed Inserts",
            ParDo.of(
                new DoFn<TableRow, TableRow>() {

                  @ProcessElement
                  public void processElement(ProcessContext c) {
                    LOG.error("***ERROR*** FAILED INSERT {}", c.element().toString());
                    c.output(c.element());
                  }
                }));

    PCollectionList.of(
        ImmutableList.of(
            tableCollection.get(CreateTableMapDoFn.deadLetterTag),
            dbRowKeyValue.get(TableToDbRowFn.deadLetterTag)))
        .apply("Flatten", Flatten.pCollections())
        .apply(
            "Write Log Errors",
            ParDo.of(
                new DoFn<String, String>() {
                  @ProcessElement
                  public void processElement(ProcessContext c) {
                    LOG.error("***ERROR*** DEAD LETTER TAG {}", c.element().toString());
                    c.output(c.element());
                  }
                }));

    p.run();
  }
}
