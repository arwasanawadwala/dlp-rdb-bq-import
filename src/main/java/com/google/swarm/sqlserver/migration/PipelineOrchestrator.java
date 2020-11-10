package com.google.swarm.sqlserver.migration;

import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DB_SOURCE;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.FILE_SOURCE;

import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineConfig;
import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineRunnerConfigUtil;

public class PipelineOrchestrator {

  public static void main(String[] args) {
    DBImportPipeline dbImportPipeline = new DBImportPipeline();
    FileImportPipeline fileImportPipeline = new FileImportPipeline();

    PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();

//    for (PipelineConfig pipelineConfig : pipelineRunnerConfigUtil.getPipelineConfigMap()
//        .getPipelineConfig()) {
//      if (pipelineConfig.getDataImportPipelineConfig().isRun()) {
//        if (pipelineConfig.getDataImportPipelineConfig().getType().equals(IMPORT_TYPE_FILE)) {
//          fileImportPipeline
//              .runFileImportPipeline(args,
//                  pipelineConfig.getDataImportPipelineConfig().getOptions());
//        } else if (pipelineConfig.getDataImportPipelineConfig().getType().equals(IMPORT_TYPE_DB)) {
//          dbImportPipeline
//              .runDBImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions());
//        }
//      } else {
//        System.out.println("No pipelines are triggered");
//      }
//    }
    String configFilePath = System.getProperty("configFilePath");

    PipelineConfig pipelineConfig = pipelineRunnerConfigUtil
        .getPipelineConfigMap(configFilePath)
        .getPipelineConfig();

    String pipelineSource = pipelineConfig.getDataImportPipelineConfig().getSource();

    if(pipelineSource.equals(FILE_SOURCE)) {
      fileImportPipeline
          .runFileImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions(),
              pipelineConfig.getDataImportPipelineConfig().getSink());
    }
    else if (pipelineSource.equals(DB_SOURCE)) {
      dbImportPipeline
          .runDBImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions(),
              pipelineConfig.getDataImportPipelineConfig().getSink());
    }

  }
}
