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
      <page:title><xsl:value-of select="title"/></page:title>
      <page:body>
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
          <table class="lenya-table-noborder">
            
            <xsl:apply-templates select="messages"/>
            <xsl:apply-templates select="id"/>
            <xsl:apply-templates select="ldapid"/>
            <xsl:apply-templates select="fullname"/>
            <xsl:apply-templates select="email"/>
            <xsl:apply-templates select="description"/>
            
            <xsl:if test="@new = 'true' and not(@ldap = 'true')">
              <tr><td colspan="2">&#160;</td></tr>
              <xsl:apply-templates select="password"/>
              <xsl:apply-templates select="confirm-password"/>
            </xsl:if>
            
            <tr>
              <td/>
              <td>
                <input name="submit" type="submit" value="Submit"/>
                &#160;
                <input name="cancel" type="submit" value="Cancel"/>
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
  
  
  <xsl:template match="id">
		<tr>
			
			<td class="lenya-entry-caption">User&#160;ID</td>
			<td>
				 <xsl:choose>
					 <xsl:when test="../@new = 'true'">
						 <input class="lenya-form-element" name="userid" type="text" value="{normalize-space(.)}"/>
					 </xsl:when>
					 <xsl:otherwise>
						 <xsl:value-of select="."/>
					 </xsl:otherwise>
				 </xsl:choose>
			</td>
		</tr>
  </xsl:template>
  
  
  <xsl:template match="ldapid">
		<tr>
			<td class="lenya-entry-caption">LDAP&#160;ID</td>
			<td>
				<input class="lenya-form-element" name="fullname" type="text" value="{normalize-space(.)}"/>
			</td>
		</tr>
  </xsl:template>
  
  
  <xsl:template match="fullname">
    <xsl:if test="not(../@ldap = 'true')">
      <tr>
        <td class="lenya-entry-caption">Name</td>
        <td>
          <input class="lenya-form-element" name="fullname" type="text" value="{normalize-space(.)}"/>
        </td>
      </tr>
    </xsl:if>
  </xsl:template>
  
  
  <xsl:template match="email">
		<tr>
			<td class="lenya-entry-caption">E-Mail</td>
			<td>
				<input class="lenya-form-element" name="email" type="text" value="{normalize-space(.)}"/>
			</td>
		</tr>
  </xsl:template>
  
  
	<xsl:template match="description">
		<tr>
			<td class="lenya-entry-caption">Description</td>
			<td>
				<textarea class="lenya-form-element" name="description"><xsl:value-of select="normalize-space(.)"/>&#160;</textarea>
			</td>
		</tr>
	</xsl:template>  
	
	
	<xsl:template match="password">
		<tr>
			<td class="lenya-entry-caption">Password</td>
			<td>
				<input type="password" class="lenya-form-element" name="new-password" value="{normalize-space(.)}"/>
			</td>
		</tr>
	</xsl:template>  
	
	
	<xsl:template match="confirm-password">
		<tr>
			<td class="lenya-entry-caption">Confirm&#160;password</td>
			<td>
				<input type="password" class="lenya-form-element" name="confirm-password" value="{normalize-space(.)}"/>
			</td>
		</tr>
	</xsl:template>  
	
	<xsl:template match="messages">
    <xsl:if test="message">
      <tr>
        <td colspan="2"><xsl:apply-templates/></td>
      </tr>
    </xsl:if>
	</xsl:template>
	
  <xsl:template match="message">
    <xsl:if test="preceding-sibling::message"><br/></xsl:if>
    <span class="lenya-form-message-{@type}"><xsl:value-of select="."/></span>
  </xsl:template>
  
</xsl:stylesheet>
