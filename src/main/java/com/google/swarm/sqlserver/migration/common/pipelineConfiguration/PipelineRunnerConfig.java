package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineRunnerConfig {

  @JsonProperty("pipeline")
  private PipelineConfig pipelineConfig;

  public PipelineConfig getPipelineConfig() {
    return pipelineConfig;
  }

}
