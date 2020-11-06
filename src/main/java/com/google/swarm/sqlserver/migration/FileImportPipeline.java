package com.google.swarm.sqlserver.migration;

import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DATA_SET;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DLP_CONFIG_BUCKET;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DLP_CONFIG_OBJECT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.FILE_DE_LIMITER;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.INPUT_FILE_PATH;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.PROJECT;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.TEMP_LOCATION;

import com.google.api.services.bigquery.model.TableRow;
import com.google.swarm.sqlserver.migration.common.fileImport.FileImportPipelineOptions;
import com.google.swarm.sqlserver.migration.common.fileImport.FileRowToBQRowConverter;
import com.google.swarm.sqlserver.migration.common.fileImport.FileTableSchema;
import com.google.swarm.sqlserver.migration.common.fileImport.config.TableSchemaConfigUtil;
import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineRunnerConfigUtil;
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

  public void runFileImportPipeline(String[] args) {

    FileImportPipelineOptions fileImportPipelineOptions = PipelineOptionsFactory.fromArgs(args)
        .withValidation().as(FileImportPipelineOptions.class);
    System.out.println("------->>>" + INPUT_FILE_PATH);
    runFileImport(enrichOptions(fileImportPipelineOptions));

  }

  private FileImportPipelineOptions enrichOptions(
      FileImportPipelineOptions fileImportPipelineOptions) {

    PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();

    Map<String, String> fileImportOptionsMap = pipelineRunnerConfigUtil.getPipelineConfigMap()
        .getPipelineConfig().getFileImportPipelineConfig().getOptions();

    fileImportPipelineOptions
        .setProject(fileImportOptionsMap.get(PROJECT));
    fileImportPipelineOptions.setDataSet(fileImportOptionsMap.get(DATA_SET));
    fileImportPipelineOptions.setInputFilePath(fileImportOptionsMap.get(INPUT_FILE_PATH));
    fileImportPipelineOptions.setFileDeLimiter(fileImportOptionsMap.get(FILE_DE_LIMITER));
    fileImportPipelineOptions.setTempLocation(fileImportOptionsMap.get(TEMP_LOCATION));
    fileImportPipelineOptions.setDLPConfigBucket(fileImportOptionsMap.get(DLP_CONFIG_BUCKET));
    fileImportPipelineOptions.setDLPConfigObject(fileImportOptionsMap.get(DLP_CONFIG_OBJECT));

    return fileImportPipelineOptions;
  }

  public void runFileImport(FileImportPipelineOptions options) {

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
