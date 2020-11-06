package com.google.swarm.sqlserver.migration;

import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineRunnerConfigUtil;

public class PipelineOrchestrator {

  public static void main(String[] args) {
    FileImportPipeline fileImportPipeline = new FileImportPipeline();

    PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();

    if (pipelineRunnerConfigUtil.getPipelineConfigMap().getPipelineConfig()
        .getFileImportPipelineConfig().isRun()) {
      fileImportPipeline.runFileImportPipeline(args);
    } else {
      System.out.println("No pipelines are triggered");
    }
  }

}
