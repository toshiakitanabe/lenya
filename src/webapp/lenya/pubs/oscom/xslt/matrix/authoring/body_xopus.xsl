<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="text()">
  <xsl:copy />
</xsl:template>

<xsl:template match="system">
<div>
 <font face="verdana">

 <h3>Content Management
<xsl:choose>
  <xsl:when test="@type='cms'">
System
  </xsl:when>
  <xsl:when test="@type='framework'">
Framework
  </xsl:when>
  <xsl:otherwise>
<xsl:value-of select="."/>
  </xsl:otherwise>
</xsl:choose>
</h3>

 <h2><xsl:value-of select="system_name"/></h2>
 <xsl:apply-templates select="editor"/>
 <xsl:apply-templates select="description"/>
 <ul>
   <li>Home: <xsl:apply-templates select="main_url"/></li>
   <li><xsl:apply-templates select="license"/></li>
 </ul>
 </font>
</div>
</xsl:template>

<xsl:template match="license">
 License: <xsl:apply-templates select="license_name"/> (<xsl:apply-templates select="license_url"/>)
</xsl:template>

<xsl:template match="description">
  <p><xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="editor">
<p>
<font size="-1">
Data maintained by <xsl:apply-templates/> (<xsl:value-of select="@email"/>)
</font>
</p>
</xsl:template>
 
</xsl:stylesheet>  
