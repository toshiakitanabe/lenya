<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:page="http://apache.org/cocoon/lenya/cms-page/1.0"
    xmlns:usecase="http://apache.org/cocoon/lenya/usecase/1.0"
    xmlns:not="http://apache.org/cocoon/lenya/notification/1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    >
 
<xsl:import href="../util/page-util.xsl"/>

<xsl:output version="1.0" indent="yes" encoding="UTF-8"/>

<xsl:param name="lenya.event"/>

<xsl:template match="/usecase:submit">

  <page:page>
    <page:title>Submit for Approval: <xsl:value-of select="document-id"/></page:title>
    <page:body>
        <div class="lenya-box">
            <div class="lenya-box-title">Do you want to submit <xsl:value-of select="document-id"/> for approval?</div>
            <form method="GET" action="">
                <table class="lenya-table-noborder">
                    <tr>
                        <td class="lenya-entry-caption" valign="top">
                            <input type="hidden" name="lenya.usecase" value="transition"/>
                            <input type="hidden" name="lenya.event" value="{$lenya.event}"/>
                            <input type="hidden" name="task-id" value="ant"/>
                            <input type="hidden" name="target" value="mail"/>
                            <not:notification>
                                <not:select><xsl:copy-of select="not:users"/></not:select>
                            </not:notification>
                        </td>
                    </tr>
                    <tr>
                        <td>&#160;</td>
                    </tr>
                    <tr>
                        <td/>
                        <td>
                            <input type="submit" name="submit" value="Submit"/> &#160;
                            <input type="button" onClick="location.href='{usecase:referer}';" value="Cancel"/>
                        </td>
                    </tr>
                    <tr>
                        <td>&#160;</td>
                    </tr>
                </table>
            </form>
        </div>
    </page:body>
  </page:page>
</xsl:template>

</xsl:stylesheet>  
