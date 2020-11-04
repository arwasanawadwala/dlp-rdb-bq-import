package com.google.swarm.sqlserver.migration.common.fileImport.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class TableSchemaConfig {

  @JsonProperty("patient")
  private Map<String, String> patient;

  @JsonProperty("patientleave")
  private Map<String, String> patientleave;

  public Map<String, String> getPatient() {
    return patient;
  }

  public Map<String, String> getPatientleave() {
    return patientleave;
  }

}
