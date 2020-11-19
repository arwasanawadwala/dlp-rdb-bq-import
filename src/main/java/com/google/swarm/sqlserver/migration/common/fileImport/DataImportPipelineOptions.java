package com.google.swarm.sqlserver.migration.common.fileImport;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface DataImportPipelineOptions extends PipelineOptions, DataflowPipelineOptions {

  @Description("BQ Dataset")
  String getDataSet();

  void setDataSet(String value);

  @Description("JDBC Spec")
  String getJDBCSpec();

  void setJDBCSpec(String value);

  @Description("Table Offset Count")
  Integer getOffsetCount();

  void setOffsetCount(Integer value);

  @Description("Exclude Tables")
  String getExcludedTables();

  void setExcludedTables(String value);

  @Description("DLP Bucket Config")
  String getDLPConfigBucket();

  void setDLPConfigBucket(String value);

  @Description("DLP Object Config")
  String getDLPConfigObject();

  void setDLPConfigObject(String value);

  @Description("Input file path")
  String getInputFilePath();

  void setInputFilePath(String value);

  @Description("file delimiter")
  String getFileDeLimiter();

  void setFileDeLimiter(String value);

  @Description("GCS Bucket")
  String getGcsSinkBucket();

  void setGcsSinkBucket(String value);

  @Description("Table schema file path")
  String getTableSchemaPath();

  void setTableSchemaPath(String value);

  @Description("Pipeline Config Object")
  String getConfigObject();

  void setConfigObject(String value);

  @Description("Pipeline Config Bucket")
  String getConfigBucket();

  void setConfigBucket(String value);

  @Description("Pipeline job Mode")
  String getJobMode();

  void setJobMode(String value);
}
