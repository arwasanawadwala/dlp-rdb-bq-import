package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFlowPipelineConfig {

  @JsonProperty("pipelineConfig")
  private PipelineConfig pipelineConfig;

  public PipelineConfig getPipelineConfig() {
    return pipelineConfig;
  }
}
