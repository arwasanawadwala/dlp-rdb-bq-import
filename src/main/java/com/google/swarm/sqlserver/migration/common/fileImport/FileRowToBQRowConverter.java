package com.google.swarm.sqlserver.migration.common.fileImport;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.swarm.sqlserver.migration.common.fileImport.config.TableSchemaConfigUtil;
import org.apache.beam.sdk.transforms.DoFn;

@SuppressWarnings("serial")
public class FileRowToBQRowConverter extends DoFn<String, TableRow> {

  private String deLimiter = ",";

  public FileRowToBQRowConverter(String deLimiter) {
    this.deLimiter = deLimiter;
  }

  @ProcessElement
  public void processElement(ProcessContext context) {
    TableRow row = new TableRow();

    String[] parts = context.element().split(this.deLimiter);

    for (int i = 0; i < parts.length; i++) {
      TableFieldSchema columns = FileTableSchema
          .getTableSchema(new TableSchemaConfigUtil().getSchemaMap().getFileTableSchemaMap()
              .getPatientTableMap()).getFields().get(i);
      row.set(columns.getName(), parts[i]);
    }
    context.output(row);
  }
}
