#!/bin/bash
#
# This script sets the variables in Composer. The variables are needed for the
# data processing DAGs to properly execute, such as project-id, GCP region and
#zone. It also sets Cloud Storage buckets where test files are stored.
#
# Copyright 2019 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

echo "GCP_PROJECT_ID=${GCP_PROJECT_ID}"
echo "_PROJECT_NUMBER=${_PROJECT_NUMBER}"
echo "_DATAFLOW_JAR_BUCKET=${_DATAFLOW_JAR_BUCKET}"
echo "_DATAFLOW_STAGING_BUCKET=${_DATAFLOW_STAGING_BUCKET}"
echo "_COMPOSER_REGION=${_COMPOSER_REGION}"
echo "_COMPOSER_ZONE_ID=${_COMPOSER_ZONE_ID}"
echo "_COMPOSER_ENV_NAME=${_COMPOSER_ENV_NAME}"
echo "_COMPOSER_DAG_NAME=${_COMPOSER_DAG_NAME}"


gcloud composer environments run "${_COMPOSER_ENV_NAME}"  --location "${_COMPOSER_REGION}" variables -- --set "gcp_project" "${GCP_PROJECT_ID}"
gcloud composer environments run "${_COMPOSER_ENV_NAME}"  --location "${_COMPOSER_REGION}" variables -- --set "gcp_region" "${_COMPOSER_REGION}"
gcloud composer environments run "${_COMPOSER_ENV_NAME}"  --location "${_COMPOSER_REGION}" variables -- --set "gcp_zone" "${_COMPOSER_ZONE_ID}"
gcloud composer environments run "${_COMPOSER_ENV_NAME}"  --location "${_COMPOSER_REGION}" variables -- --set "dataflow_jar_location" "${_DATAFLOW_JAR_BUCKET}"
gcloud composer environments run "${_COMPOSER_ENV_NAME}"  --location "${_COMPOSER_REGION}" variables -- --set "dataflow_jar_file" "dlp-rdb-bq-import-dataflow.jar"
gcloud composer environments run "${_COMPOSER_ENV_NAME}"  --location "${_COMPOSER_REGION}" variables -- --set "dataflow_staging_bucket" "${_DATAFLOW_STAGING_BUCKET}"

gcp_project=${GCP_PROJECT_ID}, gcp_region=${_COMPOSER_REGION}, gcp_zone=${_COMPOSER_ZONE_ID}, dataflow_jar_location=${_DATAFLOW_JAR_BUCKET}, dataflow_jar_file=dlp-rdb-bq-import-dataflow.jar, dataflow_staging_bucket=${_DATAFLOW_STAGING_BUCKET}