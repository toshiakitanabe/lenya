<?xml version="1.0"?>

<!--
 $Id: copy.xsl,v 1.11 2003/09/05 14:42:59 andreas Exp $
 -->

 <xsl:stylesheet version="1.0"
   xmlns="http://www.w3.org/1999/xhtml"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:session="http://www.apache.org/xsp/session/2.0"
   xmlns:page="http://apache.org/cocoon/lenya/cms-page/1.0"
   >
  
  <xsl:output version="1.0" indent="yes" encoding="ISO-8859-1"/>
  
  <xsl:variable name="document-id"><xsl:value-of select="/page/info/document-id"/></xsl:variable>
  <xsl:variable name="action"><xsl:value-of select="/page/info/action"/></xsl:variable>
  <xsl:variable name="area"><xsl:value-of select="/page/info/area"/></xsl:variable>
  <xsl:variable name="request-uri"><xsl:value-of select="/page/info/request-uri"/></xsl:variable>

  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="page">
    <page:page>
      <page:title>Copy Document</page:title>
      <page:body>
        <xsl:apply-templates select="body"/>
	    <xsl:apply-templates select="info"/>
      </page:body>
    </page:page>
  </xsl:template>
  
  <xsl:template match="info">
    <div class="lenya-box">
      <div class="lenya-box-title">Copy Document</div>
      <div class="lenya-box-body">
        <form method="get">
        <xsl:attribute name="action"></xsl:attribute>
          <input type="hidden" name="documentid" value="{$document-id}"/>
          <input type="hidden" name="area" value="{$area}"/>
          <input type="hidden" name="action" value="{$action}"/>
          <input type="hidden" name="lenya.usecase" value="copy"/>
          <input type="hidden" name="lenya.step" value="copy"/>
          
          <table class="lenya-table-noborder">
          	<tr>
          		<td class="lenya-entry-caption">Document:</td>
          		<td><xsl:value-of select="document-id"/></td>
          	</tr>
          	<tr>
          		<td/>
          		<td><br/>The document will be placed on the clipboard, ready<br/> to be pasted at the location of your choosing.</td>
          	</tr>
          	<tr>
          		<td/>
          		<td>
          			<br/>
        <input type="submit" value="Copy"/>&#160;
        <input type="button" onClick="location.href='{$request-uri}';" value="Cancel"/>
          		</td>
          	</tr>
          </table>
          
    </form>
      </div>
    </div>
  </xsl:template>
  
</xsl:stylesheet>