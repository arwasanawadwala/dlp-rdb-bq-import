package com.google.swarm.sqlserver.migration.common;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

public interface BQReIdentificationPipelineOptions extends DataflowPipelineOptions {

  @Description("BQ Dataset")
  String getDataSet();

  void setDataSet(String value);

  @Description("Query to execute")
  @Default.String(
      "SELECT age FROM `sookplatformspikes.spike_dlp_oesc_mysql_migration.oesc_on_prem_patient`")
  String getQuery();

  void setQuery(String value);

  @Description("DLP DeIdentify Template")
  String getDeidentifyTemplateName();

  void setDeidentifyTemplateName(String value);

  @Description("DLP Inspect Template")
  String getInspectTemplateName();

  void setInspectTemplateName(String value);

  @Description("PUB Sub Topic")
  String getTopic();

  void setTopic(String value);

  @Description("Mapping of a BQ columns and Original Column names")
  String getColumnMap();

  void setColumnMap(String value);
}
