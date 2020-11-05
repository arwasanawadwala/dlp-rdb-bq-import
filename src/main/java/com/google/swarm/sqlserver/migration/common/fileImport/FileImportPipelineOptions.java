package com.google.swarm.sqlserver.migration.common.fileImport;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;

public interface FileImportPipelineOptions extends PipelineOptions, DataflowPipelineOptions {

  @Description("BQ Dataset")
  ValueProvider<String> getDataSet();

  void setDataSet(ValueProvider<String> value);

  @Description("DLP Bucket Config")
  ValueProvider<String> getDLPConfigBucket();

  void setDLPConfigBucket(ValueProvider<String> value);

  @Description("DLP Object Config")
  ValueProvider<String> getDLPConfigObject();

  void setDLPConfigObject(ValueProvider<String> value);

  @Description("Input file path")
  ValueProvider<String> getInputFilePath();

  void setInputFilePath(ValueProvider<String> value);

  @Description("file delimiter")
  ValueProvider<String> getFileDeLimiter();

  void setFileDeLimiter(ValueProvider<String> value);
}
