<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:page="http://apache.org/cocoon/lenya/cms-page/1.0"
    xmlns:session="http://www.apache.org/xsp/session/2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    
  <xsl:output encoding="ISO-8859-1" indent="yes" version="1.0"/>
  
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  
  <xsl:template match="page">
    <page:page>
      <page:title>Edit User</page:title>
      <page:body>
        <xsl:apply-templates select="body"/>
        <xsl:apply-templates select="user"/>
      </page:body>
    </page:page>
  </xsl:template>
  
  
  <xsl:template match="user">
    
    <table class="lenya-noborder">
    <tr>
    <td valign="top">
    
    <div class="lenya-box">
      <div class="lenya-box-title">User Data</div>
      <div class="lenya-box-body">
        
        <form method="GET" action="{/page/continuation}.continuation">
          <input type="hidden" name="lenya.usecase" value="user-admin"/>
          <input type="hidden" name="user-id" value="{id}"/>
          <table class="lenya-table-noborder">
            
            <xsl:apply-templates select="/page/message[@area = 'profile']"/>
              
            <tr>
              <td class="lenya-entry-caption">User&#160;ID</td>
              <td><xsl:value-of select="id"/></td>
            </tr>
            <tr>
              <td class="lenya-entry-caption">Full&#160;Name</td>
              <td><xsl:value-of select="fullname"/></td>
            </tr>
            <tr>
              <td class="lenya-entry-caption">E-Mail</td>
              <td><xsl:value-of select="email"/></td>
            </tr>
            <tr>
              <td class="lenya-entry-caption">Groups</td>
              <td>
                <xsl:apply-templates select="groups"/>
              </td>
            </tr>
          </table>
        </form>
      </div>
    </div>
            
    </td>
    </tr>
    </table>
    
  </xsl:template>
  
  
  <xsl:template match="groups">
    <xsl:apply-templates select="group"/>
  </xsl:template>
  
  
  <xsl:template match="group">
    <xsl:if test="position() &gt; 1"><br/></xsl:if>
    <xsl:value-of select="."/>
  </xsl:template>
  
  
  <xsl:template match="message">
    <xsl:if test="text()">
      <tr>
        <td colspan="2"><span class="lenya-form-message-{@type}"><xsl:apply-templates/></span></td>
      </tr>
    </xsl:if>
  </xsl:template>
  
  
</xsl:stylesheet>
