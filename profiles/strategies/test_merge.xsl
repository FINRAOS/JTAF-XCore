<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html" omit-xml-declaration="yes" indent="yes"/>
<xsl:preserve-space elements="desc"/>

<xsl:template match="/">
	<html>
	<body>
		<h1><xsl:value-of select="execute/@name"/></h1>
		<xsl:apply-templates select="execute/target"/>
	</body>
	</html>
</xsl:template>
	
<xsl:template match="execute/target">
	<h3>Test Script: <xsl:value-of select="@name"/></h3>
	<xsl:variable name="script" select="@name"/>
	<xsl:apply-templates select="document(substring($script,2))/testsuite"/>
</xsl:template>
	
<xsl:template match="testsuite">
	<h3>Test Suite: <xsl:value-of select="@name"/></h3>
	<xsl:apply-templates select="test"/>
</xsl:template>
	
<xsl:template match="test">
	<h4><xsl:value-of select="@testCaseID"/> : <xsl:value-of select="@name"/></h4>
	<xsl:apply-templates select="desc"/>
</xsl:template>
	
<xsl:template match="desc">
	<pre><xsl:value-of select="."/></pre>
</xsl:template>

</xsl:stylesheet>
