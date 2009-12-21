<?xml version="1.0" encoding="UTF-8" ?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:col="http://apache.org/cocoon/lenya/collection/1.0"
    xmlns:meta="http://apache.org/lenya/meta/1.0/"
    xmlns:doc="http://apache.org/lenya/metadata/document/1.0"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    >
    
  
  <xsl:template match="/col:collection">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates select="col:document">
        <xsl:sort select="dc:date" order="descending"/>
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  
  
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  

</xsl:stylesheet> 
