package com.google.swarm.sqlserver.migration.common.sink;

import com.google.api.services.bigquery.model.TableRow;
import com.google.swarm.sqlserver.migration.common.BigQueryTableDestination;
import com.google.swarm.sqlserver.migration.common.SqlTable;
import com.google.swarm.sqlserver.migration.common.fileImport.DataImportPipelineOptions;
import com.google.swarm.sqlserver.migration.utils.CustomValueProvider;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.InsertRetryPolicy;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class PipelineBqSink {

  public static WriteResult getWriteResult(DataImportPipelineOptions options,
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
}
