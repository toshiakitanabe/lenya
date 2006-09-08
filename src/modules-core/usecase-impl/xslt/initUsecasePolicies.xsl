<?xml version="1.0" encoding="UTF-8"?>
<!--
  Run this stylesheet over build/lenya/webapp/WEB-INF/cocoon.xconf to
  get a default permissions file with all usecases allowed for the "admin" role.

  Example for Xalan on Linux:
  Xalan -i 2 $LENYA_HOME/build/lenya/webapp/WEB-INF/cocoon.xconf \
    $LENYA_HOME/src/modules-core/usecase-impl/xslt/initUsecasePolicies.xsl \
    > $YOUR_PUB/config/ac/usecase-policies.xml
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns="http://apache.org/cocoon/lenya/ac/1.0">
  
  <xsl:template match="cocoon">
    <xsl:apply-templates select="usecases"/>
  </xsl:template>

  <xsl:template match="usecases">
    <xsl:text>
</xsl:text>
    <xsl:comment>+++NOTE+++ The usecase list was initialized using modules/usecase-impl/xslt/initUsecasePolicies.xsl.</xsl:comment>
    <xsl:text>
</xsl:text>
    <usecases>
      <xsl:apply-templates select="component-instance"/>
    </usecases>
  </xsl:template>
  
  <xsl:template match="usecases/component-instance">
    <usecase id="{@name}"><role id="admin" method="grant"/></usecase>
  </xsl:template>

</xsl:stylesheet>