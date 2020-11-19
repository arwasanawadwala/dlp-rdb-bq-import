package com.google.swarm.sqlserver.migration;

import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineConfig;
import com.google.swarm.sqlserver.migration.common.pipelineConfiguration.PipelineRunnerConfigUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.DB_SOURCE;
import static com.google.swarm.sqlserver.migration.common.pipelineConfiguration.Constants.FILE_SOURCE;

public class PipelineOrchestrator {

    public static void main(String[] args) {
        DBImportPipeline dbImportPipeline = new DBImportPipeline();
        FileImportPipeline fileImportPipeline = new FileImportPipeline();

        PipelineRunnerConfigUtil pipelineRunnerConfigUtil = new PipelineRunnerConfigUtil();

        String jobMode = getArg(args, "jobMode");
        PipelineConfig pipelineConfig;

        String configObject = getArg(args, "configObject");
        if (jobMode.equals("local")) {
            pipelineConfig = pipelineRunnerConfigUtil.getPipelineConfigMap(configObject).getPipelineConfig();
        } else {
            String projectId = getArg(args, "project");
            String configBucket = getArg(args, "configBucket");
            pipelineConfig = pipelineRunnerConfigUtil.getPipelineConfigMap(projectId,configBucket, configObject).getPipelineConfig();
        }
        String pipelineSource = pipelineConfig.getDataImportPipelineConfig().getSource();

        if (pipelineSource.equals(FILE_SOURCE)) {
            fileImportPipeline
                    .runFileImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions(),
                            pipelineConfig.getDataImportPipelineConfig().getSink());
        } else if (pipelineSource.equals(DB_SOURCE)) {
            dbImportPipeline
                    .runDBImportPipeline(args, pipelineConfig.getDataImportPipelineConfig().getOptions(),
                            pipelineConfig.getDataImportPipelineConfig().getSink());
        }

    }

    private static String getArg(String[] args, String argKey) {
        String configBucketArg = Arrays.stream(args).filter(arg -> arg.contains(argKey)).collect(Collectors.joining());
        return configBucketArg.split("=")[1];
    }
}
