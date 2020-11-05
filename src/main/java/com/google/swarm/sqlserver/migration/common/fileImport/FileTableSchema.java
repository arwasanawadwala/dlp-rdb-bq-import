package com.google.swarm.sqlserver.migration.common.fileImport;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileTableSchema {

  public static TableSchema getTableSchema(Map<String,String> schemaMap) {
    List<TableFieldSchema> schemaFields = new ArrayList<>();
    for (String key : schemaMap.keySet()) {
      schemaFields.add(new TableFieldSchema().setName(key).setType(schemaMap.get(key)));
    }
    return new TableSchema().setFields(schemaFields);
  }

}
