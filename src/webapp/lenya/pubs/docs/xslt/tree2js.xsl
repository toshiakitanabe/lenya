<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="tree">
    <xsl:apply-templates select="branch" mode="trunk">
    </xsl:apply-templates>
  </xsl:template>
  
<!-- 
Start at Trunk
-->
  <xsl:template match="branch" mode="trunk">
    <array>
      <xsl:apply-templates select="branch|leaf">
        <xsl:with-param name="parentPath">/wyona-cms/docs/xdocs</xsl:with-param>
        <xsl:with-param name="parentNumber"></xsl:with-param>
      </xsl:apply-templates>
    </array>
  </xsl:template>

<!--
Write the branch
-->
  <xsl:template match="branch">
    <xsl:param name="parentPath"/>
    <xsl:param name="parentNumber"/>
    <tocTab position="to_be_determined">
    <xsl:choose>
      <xsl:when test="@doctype='Guide'">
        <xsl:attribute name="href"><xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/>.html</xsl:attribute>
	new Array("<xsl:value-of select="position()"/>",
	"<xsl:value-of select="@menuName"/>",
	"<xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/>.html");
      </xsl:when>
      <xsl:when test="@doctype='XDoc'">
        <xsl:attribute name="href"><xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/></xsl:attribute>
	new Array("<xsl:value-of select="$parentNumber"/>.<xsl:value-of select="position()"/>",
	"<xsl:value-of select="@menuName"/>",
	"<xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/>");
      </xsl:when>
      <xsl:when test="@doctype='Empty'">
        <xsl:attribute name="href"><xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/></xsl:attribute>
	new Array("<xsl:value-of select="$parentNumber"/>.<xsl:value-of select="position()"/>",
	"<xsl:value-of select="@menuName"/>",
	"<xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/>");
      </xsl:when>
      <xsl:otherwise>
      </xsl:otherwise>
    </xsl:choose>
    </tocTab>

    <xsl:choose>
       <xsl:when test="@doctype='Guide'">
          <xsl:apply-templates select="branch|leaf">
	  <!--
            <xsl:with-param name="parentPath"><xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/></xsl:with-param>
	  -->
            <xsl:with-param name="parentPath"><xsl:value-of select="$parentPath"/></xsl:with-param>
            <xsl:with-param name="parentNumber"><xsl:value-of select="position()"/></xsl:with-param>
          </xsl:apply-templates>
       </xsl:when>
       <xsl:otherwise>
          <xsl:apply-templates select="branch|leaf">
	  <!--
            <xsl:with-param name="parentPath"><xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/></xsl:with-param>
	  -->  
            <xsl:with-param name="parentPath"><xsl:value-of select="$parentPath"/></xsl:with-param>
            <xsl:with-param name="parentNumber"><xsl:value-of select="$parentNumber"/>.<xsl:value-of select="position()"/></xsl:with-param>
          </xsl:apply-templates>
       </xsl:otherwise>
    </xsl:choose>
</xsl:template>
  
  
<!--
Write the leaf
-->
  <xsl:template match="leaf">
    <xsl:param name="parentPath"/>
    <xsl:param name="parentNumber"/>
    <tocTab position="to_be_determined">
  <xsl:if test="@relURI">
        <xsl:attribute name="href"><xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/></xsl:attribute>
  new Array("<xsl:value-of select="$parentNumber"/>.<xsl:value-of select="position()"/>",
    "<xsl:value-of select="@menuName"/>",
    "<xsl:value-of select="$parentPath"/>/<xsl:value-of select="@relURI"/>");
  </xsl:if>  
  <xsl:if test="@absURI">
        <xsl:attribute name="href"><xsl:value-of select="@absURI"/></xsl:attribute>
  new Array("<xsl:value-of select="$parentNumber"/>.<xsl:value-of select="position()"/>",
    "<xsl:value-of select="@menuName"/>",
    "<xsl:value-of select="@absURI"/>");
  </xsl:if>  
  <xsl:if test="@URL">
        <xsl:attribute name="href"><xsl:value-of select="@URL"/></xsl:attribute>
  new Array("<xsl:value-of select="$parentNumber"/>.<xsl:value-of select="position()"/>",
    "<xsl:value-of select="@menuName"/>",
    "<xsl:value-of select="@URL"/>");
  </xsl:if>  
    </tocTab>


  </xsl:template>


</xsl:stylesheet>

