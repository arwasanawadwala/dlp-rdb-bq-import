package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TableSchemaConfig {

  @JsonProperty("tableSchema")
  private FileTableSchemaMapConfig fileTableSchemaMap;

  public FileTableSchemaMapConfig getFileTableSchemaMap() {
    return fileTableSchemaMap;
  }

  public void setFileTableSchemaMap(
      FileTableSchemaMapConfig fileTableSchemaMap) {
    this.fileTableSchemaMap = fileTableSchemaMap;
  }
}
