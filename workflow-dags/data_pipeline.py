# Copyright 2019 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""Data processing test workflow definition.
"""
import datetime

from airflow.contrib.operators.dataflow_operator import DataFlowJavaOperator
from airflow.models import Variable, DAG

dataflow_staging_bucket = 'gs://%s' % (
    Variable.get('dataflow_staging_bucket'))

dataflow_jar_location = 'gs://%s/%s' % (
    Variable.get('dataflow_jar_location'),
    Variable.get('dataflow_jar_file'))

project = Variable.get('gcp_project')
region = Variable.get('gcp_region')
zone = Variable.get('gcp_zone')

yesterday = datetime.datetime.combine(
    datetime.datetime.today() - datetime.timedelta(1),
    datetime.datetime.min.time())

default_args = {
    'dataflow_default_options': {
        'project': project,
        'zone': zone,
        'region': region,
        'subnetwork': 'https://www.googleapis.com/compute/v1/projects/sookplatformspikes/regions/us-central1/subnetworks/sook',
        'stagingLocation': dataflow_staging_bucket
    }
}

with DAG(
        'db-import',
        schedule_interval=None,
        default_args=default_args) as dag:
    dataflow_execution = DataFlowJavaOperator(
        task_id='db-import-run',
        jar=dataflow_jar_location,
        start_date=yesterday,
        options={
            'autoscalingAlgorithm': 'THROUGHPUT_BASED',
            'maxNumWorkers': '3',
            'project': 'sookplatformspikes',
            'runner': 'DirectRunner',
            'dataSet': 'spike_dlp_oesc_mysql_migration',
            'JDBCSpec': 'jdbc:mysql://(host=localhost,port=3306,user=root,password=password)/oesc_on_prem?encrypt=true&trustServerCertificate=true',
            'tempLocation': 'gs://spike_oesc/spike_dpl_template_oesc.json',
            'offsetCount': '500',
            'DLPConfigBucket': 'spike_oesc',
            'DLPConfigObject': 'spike_dpl_template_oesc.json'
        }
    )
