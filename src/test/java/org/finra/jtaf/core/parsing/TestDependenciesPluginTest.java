package org.finra.jtaf.core.parsing;

import java.util.List;

import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.model.test.digraph.Dependencies;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;


public class TestDependenciesPluginTest extends ParserPluginTest
{
	private static final String SCRIPT_FILE_NAME = "testscripts/DependencyTest.xml";
	private static final String TEST_OF_INTEREST_NAME = "Dependency Plugin Debug Test F";
	private static final String TEST_DEPENDENCY_1 = "Dependency Plugin Debug Test C";
	private static final String TEST_DEPENDENCY_2 = "Dependency Plugin Debug Test D";
	
	@Test
	public void testExecute() throws Exception
	{
		TestDependenciesPlugin testDependenciesPlugin = new TestDependenciesPlugin();
		Node testRoot = buildTestRoot(SCRIPT_FILE_NAME, TEST_OF_INTEREST_NAME);
		TestSuite testSuite = buildTestSuite(testRoot);
		PostTestParserPluginContext postTestParserPluginContext = new PostTestParserPluginContext(null, testSuite, testRoot);
		testDependenciesPlugin.execute(postTestParserPluginContext);
		
		List<TestComponent> componentList = testSuite.getComponentList();
		TestScript latestTestScript = (TestScript) componentList.get(componentList.size() - 1);
		Dependencies dependencies = latestTestScript.getDependencies();
		Assert.assertEquals(2, dependencies.getDependenciesTests().size());
		Assert.assertTrue(dependencies.getDependenciesTests().contains(TEST_DEPENDENCY_1));
		Assert.assertTrue(dependencies.getDependenciesTests().contains(TEST_DEPENDENCY_2));
	}
}
