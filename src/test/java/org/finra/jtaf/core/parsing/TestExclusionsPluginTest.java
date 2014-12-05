package org.finra.jtaf.core.parsing;

import java.util.List;

import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.model.test.digraph.Dependencies;
import org.finra.jtaf.core.parsing.TestExclusionsPlugin;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;


public class TestExclusionsPluginTest extends ParserPluginTest
{
	private static final String SCRIPT_FILE_NAME = "testscripts/ExclusionTest.xml";
	private static final String TEST_OF_INTEREST_NAME = "Exclusion Plugin Debug Test F";
	private static final String TEST_EXCLUSION_1 = "Exclusion Plugin Debug Test C";
	private static final String TEST_EXCLUSION_2 = "Exclusion Plugin Debug Test D";
	
	@Test
	public void testExecute() throws Exception
	{
		TestExclusionsPlugin testExclusionsPlugin = new TestExclusionsPlugin();
		Node testRoot = buildTestRoot(SCRIPT_FILE_NAME, TEST_OF_INTEREST_NAME);
		TestSuite testSuite = buildTestSuite(testRoot);
		PostTestParserPluginContext postTestParserPluginContext = new PostTestParserPluginContext(null, testSuite, testRoot);
		testExclusionsPlugin.execute(postTestParserPluginContext);
		
		List<TestComponent> componentList = testSuite.getComponentList();
		TestScript latestTestScript = (TestScript) componentList.get(componentList.size() - 1);
		Dependencies exclusions = latestTestScript.getExclusions();
		Assert.assertEquals(2, exclusions.getDependenciesTests().size());
		Assert.assertTrue(exclusions.getDependenciesTests().contains(TEST_EXCLUSION_1));
		Assert.assertTrue(exclusions.getDependenciesTests().contains(TEST_EXCLUSION_2));
	}
}
