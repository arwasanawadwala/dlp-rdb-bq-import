package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

public class TableSchemaConfigUtil {

  private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

  public TableSchemaConfig getSchemaMap(String tableSchemaFilePath) {
    objectMapper.findAndRegisterModules();
    TableSchemaConfig tableSchemaConfig = null;
    try {
      tableSchemaConfig = objectMapper.readValue(new File(tableSchemaFilePath),
          TableSchemaConfig.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tableSchemaConfig;
  }

}
