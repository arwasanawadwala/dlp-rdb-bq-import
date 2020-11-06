package com.google.swarm.sqlserver.migration;

import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineConfig;
import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineRunnerConfigUtil;

public class PipelineOrchestrator {

//  public static void main(String[] args) {
//    FileImportPipeline fileImportPipeline = new FileImportPipeline();
//
//    PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();
//
//    for (PipelineConfig pipelineConfig : pipelineRunnerConfigUtil.getPipelineConfigMap()
//        .getPipelineConfig()) {
//      if (pipelineConfig.getDataImportPipelineConfig().isRun()) {
//        fileImportPipeline
//            .runFileImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions());
//      } else {
//        System.out.println("No pipelines are triggered");
//      }
//    }
//  }


  public static void main(String[] args) {
    DBImportPipeline dbImportPipeline = new DBImportPipeline();

    PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();

    for (PipelineConfig pipelineConfig : pipelineRunnerConfigUtil.getPipelineConfigMap()
        .getPipelineConfig()) {
      if (pipelineConfig.getDataImportPipelineConfig().isRun()) {
        dbImportPipeline
            .runDBImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions());
      } else {
        System.out.println("No pipelines are triggered");
      }
    }
  }

}
