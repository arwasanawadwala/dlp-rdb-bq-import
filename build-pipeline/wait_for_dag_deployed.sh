#!/bin/bash
#
# Script that waits for the specified Cloud Composer DAG to deploy.
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

n=0
until [[ $n -ge $4 ]]
do
  echo "try ${n}"
  status=0
  gcloud composer environments run "${1}" --location "${2}" list_dags \
  2>&1 | grep "${3}" && break
  status=$?
  n=$(($n+1))
  sleep "${5}"
done
exit $status
