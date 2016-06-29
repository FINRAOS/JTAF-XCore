package org.finra.jtaf.core.parsing;

import java.util.List;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.parsing.LoopPlugin;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;


public class LoopPluginTest extends ParserPluginTest
{
	private static final String SCRIPT_FILE_NAME = "testscripts/LoopDebug.xml";
	private static final String TEST_OF_INTEREST_NAME = "Loop Debug Test";
	private static final int ITERATION_COUNT = 4;
	
	@Test
	public void testExecute() throws Exception
	{
		LoopPlugin loopPlugin = new LoopPlugin(AutomationEngine.getInstance());
		Node testRoot = buildTestRoot(SCRIPT_FILE_NAME, TEST_OF_INTEREST_NAME);
		TestSuite testSuite = buildTestSuite(testRoot);
		PostTestParserPluginContext postTestParserPluginContext = new PostTestParserPluginContext(null, testSuite, testRoot);
		Assert.assertEquals(1, testSuite.getComponentList().size());

		List<TestComponent> componentList = testSuite.getComponentList();
		String originalTestName = componentList.get(componentList.size() - 1).getName();
		Assert.assertEquals(TEST_OF_INTEREST_NAME, originalTestName);
		
		loopPlugin.execute(postTestParserPluginContext);
		Assert.assertEquals(ITERATION_COUNT, testSuite.getComponentList().size());
		for(int iterationNumber = 1; iterationNumber <= ITERATION_COUNT; iterationNumber++)
		{
			TestScript iterationTestScript = (TestScript) componentList.get(iterationNumber - 1);
			Assert.assertEquals(TEST_OF_INTEREST_NAME + " [iteration " + iterationNumber + " of " + ITERATION_COUNT + "]", iterationTestScript.getName());
			System.out.println(iterationTestScript.getName());
		}
	}
}
