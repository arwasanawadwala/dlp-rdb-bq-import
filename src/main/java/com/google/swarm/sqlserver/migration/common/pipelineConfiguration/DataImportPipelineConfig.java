package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DataImportPipelineConfig {

  @JsonProperty("source")
  private String source;

  @JsonProperty("sink")
  private String sink;

  @JsonProperty("options")
  private Map<String, String> options;

  public void setOptions(Map<String, String> options) {
    this.options = options;
  }

  public String getSink() {
    return sink;
  }

  public Map<String, String> getOptions() {
    return options;
  }

  public String getSource() {
    return source;
  }
}
