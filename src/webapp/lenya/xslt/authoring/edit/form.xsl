<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:param name="docid"/>
<xsl:param name="doctype"/>
<xsl:param name="message"/>

<xsl:template match="/">
<html>
<body>
<p>
Edit Document <b><xsl:value-of select="$docid"/></b> (Document Type: <xsl:value-of select="$doctype"/>)
</p>

<xsl:if test="$message">
<p>
<font color="red"><b>Message:</b></font>
<br /><xsl:value-of select="$message"/>
<br />(Check log files for more details: lenya/WEB-INF/logs/*)
</p>
</xsl:if>

<form method="post" action="?lenya.usecase=edit&amp;lenya.step=close&amp;doctype={$doctype}">
<table border="1">
<tr>
  <td colspan="3" align="right"><input type="submit" value="SAVE" name="save"/><input type="submit" value="CANCEL" name="cancel"/></td>
</tr>
<xsl:apply-templates/>
<tr>
  <td colspan="3" align="right"><input type="submit" value="SAVE" name="save"/><input type="submit" value="CANCEL" name="cancel"/></td>
</tr>
</table>
</form>
</body>
</html>
</xsl:template>
 
</xsl:stylesheet>  
