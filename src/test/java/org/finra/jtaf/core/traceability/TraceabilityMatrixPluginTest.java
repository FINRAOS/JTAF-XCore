package org.finra.jtaf.core.traceability;

import java.io.File;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.parsing.TestStrategyParser;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostAllParserPluginContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TraceabilityMatrixPluginTest
{
	private File file;
	
	@Before
	public void setup()
	{
		file = new File(TraceabilityMatrix.OUTPUT_FILE);
		if(file.exists())
			file.delete();
	}
	
	@After
	public void teardown()
	{
		file.delete();
	}
	
	// Only verify that the file was created. Content validation will be part of the TraceabilityMatrix unit test
	@Test
	public void testExecute() throws ParserPluginException
	{
		TestAgenda testAgenda = new TestAgenda();
		PostAllParserPluginContext postAllParserPluginContext = new PostAllParserPluginContext(testAgenda, null);
		TraceabilityMatrixPlugin traceabilityMatrixPlugin = new TraceabilityMatrixPlugin();
		traceabilityMatrixPlugin.execute(postAllParserPluginContext);
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void testNoAutomationValue() throws ParserPluginException
	{
		try{
			TestStrategyParser tsp= new TestStrategyParser();
			TestAgenda ta = new TestAgenda();
			tsp.parse(new File(System.getProperty("user.dir") + "/profiles/strategies/test.strategy.xml"));
			tsp.setDigraph(AutomationEngine.getInstance().getTestDigraph());
			ta = tsp.getTestPlan();
			PostAllParserPluginContext postAllParserPluginContext = new PostAllParserPluginContext(ta, null);
			TraceabilityMatrixPlugin traceabilityMatrixPlugin = new TraceabilityMatrixPlugin();
			traceabilityMatrixPlugin.execute(postAllParserPluginContext);
			Assert.assertTrue(file.exists());
		}catch (Exception e) {
            throw new RuntimeException(e);
        }
	
	}
}
