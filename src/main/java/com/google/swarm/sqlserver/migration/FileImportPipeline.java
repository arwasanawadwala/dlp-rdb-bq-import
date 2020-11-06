package com.google.swarm.sqlserver.migration;

import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DATA_SET;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DLP_CONFIG_BUCKET;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DLP_CONFIG_OBJECT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.FILE_DE_LIMITER;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.INPUT_FILE_PATH;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.PROJECT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.TEMP_LOCATION;

import com.google.api.services.bigquery.model.TableRow;
import com.google.swarm.sqlserver.migration.common.fileImport.DataImportPipelineOptions;
import com.google.swarm.sqlserver.migration.common.fileImport.FileRowToBQRowConverter;
import com.google.swarm.sqlserver.migration.common.fileImport.FileTableSchema;
import com.google.swarm.sqlserver.migration.common.fileImport.config.TableSchemaConfigUtil;
import java.util.Map;
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

  public void runFileImportPipeline(String[] args, Map<String, String> dataImportPipelineConfig) {

    DataImportPipelineOptions dataImportPipelineOptions = PipelineOptionsFactory.fromArgs(args)
        .withValidation().as(DataImportPipelineOptions.class);

    runFileImport(enrichOptions(dataImportPipelineOptions, dataImportPipelineConfig));

  }

  private DataImportPipelineOptions enrichOptions(
      DataImportPipelineOptions dataImportPipelineOptions,
      Map<String, String> dataImportPipelineConfig) {

    dataImportPipelineOptions
        .setProject(dataImportPipelineConfig.get(PROJECT));
    dataImportPipelineOptions.setDataSet(dataImportPipelineConfig.get(DATA_SET));
    dataImportPipelineOptions.setInputFilePath(dataImportPipelineConfig.get(INPUT_FILE_PATH));
    dataImportPipelineOptions.setFileDeLimiter(dataImportPipelineConfig.get(FILE_DE_LIMITER));
    dataImportPipelineOptions.setTempLocation(dataImportPipelineConfig.get(TEMP_LOCATION));
    dataImportPipelineOptions.setDLPConfigBucket(dataImportPipelineConfig.get(DLP_CONFIG_BUCKET));
    dataImportPipelineOptions.setDLPConfigObject(dataImportPipelineConfig.get(DLP_CONFIG_OBJECT));

    return dataImportPipelineOptions;
  }

  public void runFileImport(DataImportPipelineOptions options) {

    Pipeline importPipeline = Pipeline.create(options);

    importPipeline.apply("Read Lines", TextIO.read().from(options.getInputFilePath()))
        .apply("trnadform to Big query row",
            ParDo.of(new FileRowToBQRowConverter(options.getFileDeLimiter().toString())))
        .apply("write to big query", BigQueryIO.<TableRow>writeTableRows()
            .to("sookplatformspikes:spike_dlp_oesc_mysql_migration.oesc_on_prem_patient")
            .withWriteDisposition(WriteDisposition.WRITE_APPEND)
            .withCreateDisposition(CreateDisposition.CREATE_NEVER)
            .withSchema(FileTableSchema.getTableSchema(
                new TableSchemaConfigUtil().getSchemaMap().getFileTableSchemaMap()
                    .getPatientTableMap())));

    importPipeline.run().waitUntilFinish();

  }

}
