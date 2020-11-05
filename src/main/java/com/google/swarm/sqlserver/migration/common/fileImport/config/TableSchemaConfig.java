package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.annotation.JsonProperty;

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
