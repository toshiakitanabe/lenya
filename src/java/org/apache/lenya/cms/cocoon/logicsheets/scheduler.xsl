<?xml version="1.0"?>

<!--
 * @author Michael Wechner
 * @created 2002.4.12
 * @version 2002.4.12
-->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsp="http://apache.org/xsp"
  xmlns:xsp-scheduler="http://apache.org/cocoon/lenya/xsp/scheduler/1.0"
  xmlns:sch="http://apache.org/cocoon/lenya/scheduler/1.0"
>

<xsl:template match="xsp-scheduler:current-date">
  <xsp:logic>
    {
    java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
    
    <sch:current-date>
      <sch:year><xsp:expr>calendar.get(java.util.Calendar.YEAR)</xsp:expr></sch:year>
      <sch:month><xsp:expr>calendar.get(java.util.Calendar.MONTH) + 1</xsp:expr></sch:month>
      <sch:day><xsp:expr>calendar.get(java.util.Calendar.DAY_OF_MONTH)</xsp:expr></sch:day>
      <sch:hour><xsp:expr>calendar.get(java.util.Calendar.HOUR_OF_DAY)</xsp:expr></sch:hour>
      <sch:minute><xsp:expr>calendar.get(java.util.Calendar.MINUTE)</xsp:expr></sch:minute>
      <sch:second><xsp:expr>calendar.get(java.util.Calendar.SECOND)</xsp:expr></sch:second>
    </sch:current-date>
    }
  </xsp:logic>
</xsl:template>

<xsl:template match="@*|node()">
	<xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy>
</xsl:template>

</xsl:stylesheet>
