package com.google.swarm.sqlserver.migration.common.sink;

import com.google.swarm.sqlserver.migration.common.fileImport.DataImportPipelineOptions;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;

public class PipelineGcsSink {

  public static void WriteToGcsSink(DataImportPipelineOptions options, PCollection<String> file_data) {
    file_data.apply("write data to GCS", TextIO.write().to(options.getGcsSinkBucket()));
  }

}
