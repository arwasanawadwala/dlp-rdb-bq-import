package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class FileTableSchemaMapConfig {

  @JsonProperty("patient")
  private Map<String, String> patientTableMap;

  @JsonProperty("patientleave")
  private Map<String, String> patientLeaveTableMap;

  public Map<String, String> getPatientTableMap() {
    return patientTableMap;
  }

  public Map<String, String> getPatientLeaveTableMap() {
    return patientLeaveTableMap;
  }

}
