package com.google.swarm.sqlserver.migration;

import com.google.api.services.bigquery.model.TableRow;
import com.google.swarm.sqlserver.migration.common.fileImport.FileImportPipelineOptions;
import com.google.swarm.sqlserver.migration.common.fileImport.FileRowToBQRowConverter;
import com.google.swarm.sqlserver.migration.common.fileImport.FileTableSchema;
import com.google.swarm.sqlserver.migration.common.fileImport.config.TableSchemaConfigUtil;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.CreateDisposition;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.WriteDisposition;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileImportPipeline {

  public static final Logger LOG = LoggerFactory.getLogger(FileImportPipeline.class);

  public static void main(String[] args) {

    FileImportPipelineOptions fileImportPipelineOptions = PipelineOptionsFactory.fromArgs(args)
        .withValidation().as(FileImportPipelineOptions.class);

    runFileImport(fileImportPipelineOptions);

  }

  public static void runFileImport(FileImportPipelineOptions options) {

    Pipeline importPipeline = Pipeline.create(options);


    importPipeline.apply("Read Lines", TextIO.read().from(options.getInputFilePath()))
        .apply("trnadform to Big query row", ParDo.of(new FileRowToBQRowConverter(options.getFileDeLimiter().toString())))
        .apply("write to big query", BigQueryIO.<TableRow>writeTableRows()
            .to("sookplatformspikes:spike_dlp_oesc_mysql_migration.oesc_on_prem_patient")
            .withWriteDisposition(WriteDisposition.WRITE_APPEND)
            .withCreateDisposition(CreateDisposition.CREATE_NEVER)
            .withSchema(FileTableSchema.getTableSchema(new TableSchemaConfigUtil().getParsedMap().getFileTableSchemaMap().getPatientTableMap())));

    importPipeline.run().waitUntilFinish();

  }

}
