<?xml version="1.0"?>
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

<!-- $Id: login.xsl 473841 2006-11-12 00:46:38Z gregor $ -->
    
    <xsl:stylesheet version="1.0"
      xmlns:i18n="http://apache.org/cocoon/i18n/2.1"      
      xmlns:page="http://apache.org/cocoon/lenya/cms-page/1.0"
      xmlns:session="http://www.apache.org/xsp/session/2.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
      
      <xsl:import href="login-user.xsl"/>
      
      <xsl:template name="afterLoginForm">
        <br/>
        <a href="?lenya.usecase=shibboleth&amp;lenya.step=wayf">Login via Shibboleth</a>
      </xsl:template>
      
    </xsl:stylesheet>
    
    