package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;

public class PipelineRunnerConfigUtil {

    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    DataFlowPipelineConfig dataFlowPipelineConfig = null;

    public DataFlowPipelineConfig getPipelineConfigMap(String configFilePath) {
        objectMapper.findAndRegisterModules();
        try {
            dataFlowPipelineConfig = objectMapper.readValue(new File(configFilePath), DataFlowPipelineConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataFlowPipelineConfig;
    }

    public DataFlowPipelineConfig getPipelineConfigMap(String projectId, String configBucket, String configObject) {
        objectMapper.findAndRegisterModules();
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Blob blob = storage.get(BlobId.of(configBucket, configObject));
        try {
            dataFlowPipelineConfig = objectMapper.readValue(blob.getContent(), DataFlowPipelineConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataFlowPipelineConfig;
    }
}
