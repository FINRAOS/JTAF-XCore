package org.finra.jtaf.core.parsing;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.parsing.MaxThreadsPlugin;
import org.finra.jtaf.core.plugins.parsing.PostStrategyElementParserPluginContext;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MaxThreadsPluginTest
{
	private static final String MAX_THREADS_NAME = "maxThreads";
	private static final String MAX_THREADS_TEST_STRATEGY_FILE = "MaxThreadsTestStrategy.strategy.xml";
	private static final int DEFAULT_THREAD_COUNT = 1;
	private static final int FILE_THREAD_COUNT = 3;
	
	@Test
	public void testExecute() throws Exception
	{
		TestAgenda testAgenda = new TestAgenda();
		Assert.assertEquals(DEFAULT_THREAD_COUNT, testAgenda.getThreadCount());
		MaxThreadsPlugin maxThreadsPlugin = new MaxThreadsPlugin();
		Element element = buildElement();
		PostStrategyElementParserPluginContext postStrategyParserPluginContext = new PostStrategyElementParserPluginContext(testAgenda, element);
		maxThreadsPlugin.execute(postStrategyParserPluginContext);
		Assert.assertEquals(FILE_THREAD_COUNT, testAgenda.getThreadCount());
	}

	private Element buildElement() throws Exception
	{
		InputStream inputStream = AutomationValuePluginTest.class.getClassLoader().getResourceAsStream(MAX_THREADS_TEST_STRATEGY_FILE);
		Element executeRoot = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getDocumentElement();
		NodeList nodeList = executeRoot.getChildNodes();
		Element element = null;
		for(int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++)
		{
			Node node = nodeList.item(nodeIndex);
			if (node.getNodeName().equalsIgnoreCase(MAX_THREADS_NAME))
			{
				element = (Element) node;
			}
		}
		return element;
	}
}
