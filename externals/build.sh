#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# Java compiler requires at least 128m of memory for cocoon-serializers block,
# plus another 128m for maven itself to fit its dependency tree.
MAVEN_OPTS="-Xmx256m"

export MAVEN_OPTS

# Invoke patch
echo "============================================="
echo "Apply patch to cocoon still issue COCOON-2294 not solve (https://issues.apache.org/jira/browse/COCOON-2294)"
echo "============================================="

patch -p0 < parent-pom_serializer-impl-dependency.patch
# Invoke maven
cd cocoon-rev-964648
echo "============================================="
echo "Install cocoon" 
echo "============================================="
mvn install
#mvn -P allblocks $ARGS
