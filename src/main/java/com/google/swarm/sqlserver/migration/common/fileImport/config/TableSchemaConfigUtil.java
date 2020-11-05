package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

public class TableSchemaConfigUtil {

  private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

  public TableSchemaConfig getParsedMap() {
    objectMapper.findAndRegisterModules();
    TableSchemaConfig ymlMap = null;
    try {
      ymlMap = objectMapper.readValue(new File(
              "/Users/akhilghatiki/oesc/spikes/dlp-rdb-bq-import/src/main/resources/tableSchema.yml"),
          TableSchemaConfig.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ymlMap;
  }

}
