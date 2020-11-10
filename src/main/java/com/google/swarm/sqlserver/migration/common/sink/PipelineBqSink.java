package com.google.swarm.sqlserver.migration.common.sink;

import com.google.api.services.bigquery.model.TableRow;
import com.google.swarm.sqlserver.migration.common.BigQueryTableDestination;
import com.google.swarm.sqlserver.migration.common.SqlTable;
import com.google.swarm.sqlserver.migration.common.fileImport.DataImportPipelineOptions;
import com.google.swarm.sqlserver.migration.common.fileImport.FileRowToBQRowConverter;
import com.google.swarm.sqlserver.migration.common.fileImport.FileTableSchema;
import com.google.swarm.sqlserver.migration.common.fileImport.config.TableSchemaConfigUtil;
import com.google.swarm.sqlserver.migration.utils.CustomValueProvider;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.CreateDisposition;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.WriteDisposition;
import org.apache.beam.sdk.io.gcp.bigquery.InsertRetryPolicy;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class PipelineBqSink {

  public static WriteResult getWriteResultToBigQuery(DataImportPipelineOptions options,
      PCollection<KV<SqlTable, TableRow>> successRecords) {
    return successRecords.apply(
        "Write to BQ",
        BigQueryIO.<KV<SqlTable, TableRow>>write()
            .to(new BigQueryTableDestination(
                CustomValueProvider.getValueProviderOf(options.getDataSet())))
            .withFormatFunction(
                new SerializableFunction<KV<SqlTable, TableRow>, TableRow>() {
                  @Override
                  public TableRow apply(KV<SqlTable, TableRow> kv) {
                    return kv.getValue();
                  }
                })
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
            .withMethod(BigQueryIO.Write.Method.STREAMING_INSERTS)
            .withoutValidation()
            .withFailedInsertRetryPolicy(InsertRetryPolicy.retryTransientErrors()));
  }


  public static void WriteFileImportToBQ(DataImportPipelineOptions options,
      PCollection<String> file_data) {
    file_data.apply("transform to Big query row",
        ParDo.of(new FileRowToBQRowConverter(options.getFileDeLimiter(),
            options.getTableSchemaPath())))
        .apply("write to big query", BigQueryIO.<TableRow>writeTableRows()
            .to("sookplatformspikes:spike_dlp_oesc_mysql_migration.oesc_on_prem_patient")
            .withWriteDisposition(WriteDisposition.WRITE_APPEND)
            .withCreateDisposition(CreateDisposition.CREATE_NEVER)
            .withSchema(FileTableSchema.getTableSchema(
                new TableSchemaConfigUtil().getSchemaMap(options.getTableSchemaPath())
                    .getFileTableSchemaMap()
                    .getPatientTableMap())));
  }


}
