package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableSchemaConfig {

  @JsonProperty("tableSchema")
  private FileTableSchemaMap fileTableSchemaMap;

  public FileTableSchemaMap getFileTableSchemaMap() {
    return fileTableSchemaMap;
  }

  public void setFileTableSchemaMap(
      FileTableSchemaMap fileTableSchemaMap) {
    this.fileTableSchemaMap = fileTableSchemaMap;
  }
}
