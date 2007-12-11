<?xml version="1.0" encoding="UTF-8"?>
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

<!-- $Id: global-sitemap.xmap 393761 2006-04-13 08:38:00Z michi $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:mod="http://apache.org/lenya/module/1.0"
  xmlns:list="http://apache.org/lenya/module-list/1.0"
  exclude-result-prefixes="mod list">
  
  <xsl:import href="util.xsl"/>
  
  <xsl:output indent="yes"/>
  
  <xsl:param name="copy-modules"/>

  <xsl:template match="list:modules">
    
    <xconf xpath="/cocoon" remove="/cocoon/component[@role = 'org.apache.lenya.cms.module.ModuleManager']">

      <component
        role="org.apache.lenya.cms.module.ModuleManager"
        class="org.apache.lenya.cms.module.ModuleManagerImpl">
        <modules copy="{$copy-modules}">
          <xsl:apply-templates select="list:module"/>
        </modules>
      </component>
    </xconf>
    
  </xsl:template>


  <xsl:template match="list:module">
    <xsl:variable name="shortcut">
      <xsl:call-template name="lastStep">
        <xsl:with-param name="path" select="@src"/>
      </xsl:call-template>
    </xsl:variable>
    
    <module src="{@src}" shortcut="{$shortcut}"/>
    
  </xsl:template>  

</xsl:stylesheet>