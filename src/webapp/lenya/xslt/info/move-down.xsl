<?xml version="1.0"?>

<!--
 $Id: move-down.xsl,v 1.6 2003/09/05 14:42:59 andreas Exp $
 -->

 <xsl:stylesheet version="1.0"
   xmlns="http://www.w3.org/1999/xhtml"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:session="http://www.apache.org/xsp/session/2.0"
   xmlns:page="http://apache.org/cocoon/lenya/cms-page/1.0"
   >
  
  <xsl:output version="1.0" indent="yes" encoding="ISO-8859-1"/>
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="page">
    <page:page>
      <page:title>Move Document Down</page:title>
      <page:body>
        <xsl:apply-templates select="body"/>
	    <xsl:apply-templates select="info"/>
      </page:body>
    </page:page>
  </xsl:template>
  
  <xsl:template match="info">
    <div class="lenya-box">
      <div class="lenya-box-title">Move Document Down</div>
      <div class="lenya-box-body">
    <form method="post">
      <p>
	Do you really want to move down <xsl:value-of select="document-id"/>?
      </p>
      <input type="submit" value="Move"/>
      <input type="submit" value="Cancel"/>
    </form>
      </div>
    </div>
  </xsl:template>
  
</xsl:stylesheet>