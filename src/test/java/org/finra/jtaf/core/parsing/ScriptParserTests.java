package org.finra.jtaf.core.parsing;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.statement.InvocationList;
import org.finra.jtaf.core.model.test.TestNamespace;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.junit.Assert;
import org.junit.Test;

public class ScriptParserTests
{
	private static final String SCRIPT_FILE_NAME = "EveryTagSuite.xml";

	
	private static final String MISSING_FILE_NAME = "ThisFileDoesNotExist.xml";
	private static final String DUPLICATE_TEST_FILE_NAME = "parser_testing/DuplicateTestNameFolder/DuplicateTestName.xml";
	
	@Test
	public void testHandleTestSource() throws Exception
	{
		DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("parser_testing/every_tag/" + SCRIPT_FILE_NAME).getDocumentElement();
//		TestSuite testSuite = AutomationEngine.getInstance().getScriptParser().processTestSuite(element, new MessageCollector(), SCRIPT_FILE_NAME);
		TestNamespace fileNameSpace = AutomationEngine.getInstance().getScriptParser().handleTestSource(new File("parser_testing/every_tag"), new MessageCollector());
		TestSuite testSuite = (TestSuite) fileNameSpace.getComponentList().get(0);
		TestScript testScript = (TestScript) testSuite.getComponentList().get(0);
		
		Assert.assertEquals("Test with Every Tag", testScript.getName());
		Assert.assertEquals("Some issue", testScript.getIssue());
		Assert.assertEquals(1, testScript.getCRs().size());
		Assert.assertEquals("cr123", testScript.getCRs().get(0));
		Assert.assertEquals("Some coverage", testScript.getCoverage());
		
		InvocationList invocationList = testScript.getBody();
		Assert.assertEquals(1, invocationList.size());
		Assert.assertEquals("TryRecoverCleanup", invocationList.get(0).getTargetName());
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testHandleTestSourceNoFile() throws Exception
	{
		(new ScriptParser()).handleTestSource(new File(MISSING_FILE_NAME), new MessageCollector());
	}
	
	@Test
	public void testhandleTestSourceDuplicateTestName() throws Exception
	{
		(new ScriptParser()).handleTestSource(new File(DUPLICATE_TEST_FILE_NAME), new MessageCollector());
	}
}
