package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

public class PipelineRunnerConfigUtil {

  private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

  public DataFlowPipelineConfig getPipelineConfigMap() {
    objectMapper.findAndRegisterModules();
    DataFlowPipelineConfig dataFlowPipelineConfig = null;
    try {
      dataFlowPipelineConfig = objectMapper.readValue(new File(
          "/Users/akhilghatiki/oesc/spikes/dlp-rdb-bq-import/src/main/resources/tableSchema.yml"),
          DataFlowPipelineConfig.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataFlowPipelineConfig;
  }

}
