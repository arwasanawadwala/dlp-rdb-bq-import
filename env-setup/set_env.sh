#!/bin/bash
#
# This script sets the environment variables for project environment specific
# information such as project_id, region and zone choice. And also name of
# buckets that are used by the build pipeline and the data processing workflow.
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

export _PROJECT_NUMBER=$(gcloud projects describe "${PROJECT_ID}" --format='get(projectNumber)')
export _DATAFLOW_JAR_BUCKET="${GCP_PROJECT_ID}-artifacts"
export _DATAFLOW_STAGING_BUCKET="${GCP_PROJECT_ID}-artifacts-staging"
export _COMPOSER_REGION='us-central1'
export _COMPOSER_ZONE_ID='us-central1-a'
export _COMPOSER_ENV_NAME='dlp-pipeline-composer'
export _COMPOSER_DAG_NAME='db-import'
