package com.google.swarm.sqlserver.migration;

import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.IMPORT_TYPE_DB;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.IMPORT_TYPE_FILE;

import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineConfig;
import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineRunnerConfigUtil;

public class PipelineOrchestrator {

  public static void main(String[] args) {
    DBImportPipeline dbImportPipeline = new DBImportPipeline();
    FileImportPipeline fileImportPipeline = new FileImportPipeline();

    PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();

    for (PipelineConfig pipelineConfig : pipelineRunnerConfigUtil.getPipelineConfigMap()
        .getPipelineConfig()) {
      if (pipelineConfig.getDataImportPipelineConfig().isRun()) {
        if (pipelineConfig.getDataImportPipelineConfig().getType().equals(IMPORT_TYPE_FILE)) {
          fileImportPipeline
              .runFileImportPipeline(args,
                  pipelineConfig.getDataImportPipelineConfig().getOptions());
        } else if (pipelineConfig.getDataImportPipelineConfig().getType().equals(IMPORT_TYPE_DB)) {
          dbImportPipeline
              .runDBImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions());
        }
      } else {
        System.out.println("No pipelines are triggered");
      }
    }
  }

}
