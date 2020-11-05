package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineConfig {

  @JsonProperty("fileImport")
  private FileImportPipelineConfig fileImportPipelineConfig;

  public FileImportPipelineConfig getFileImportPipelineConfig() {
    return fileImportPipelineConfig;
  }

}
