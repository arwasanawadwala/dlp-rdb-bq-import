package com.google.swarm.sqlserver.migration.common.pipelineConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DataImportPipelineConfig {

  @JsonProperty("run")
  private boolean run;

  @JsonProperty("type")
  private String type;

  @JsonProperty("options")
  private Map<String, String> options;

  public boolean isRun() {
    return run;
  }

  public void setOptions(Map<String, String> options) {
    this.options = options;
  }

  public String getType() {
    return type;
  }

  public Map<String, String> getOptions() {
    return options;
  }

}
