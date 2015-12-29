package org.finra.jtaf.core.parsing;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.parsing.AutomationValuePlugin;
import org.finra.jtaf.core.plugins.parsing.PostStrategyElementParserPluginContext;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AutomationValuePluginTest {
    private static final String AUTOMATION_VALUE_NAME = "automationValue";
    private static final String AUTOMATION_VALUE_TEST_STRATEGY_FILE = "AutomationValueTestStrategy.strategy.xml";
    private static final String FILE_AUTOMATION_VALUE = "DummyAutomationValue";

    @Test
    public void testExecute() throws Exception {
        TestAgenda testAgenda = new TestAgenda();
        Assert.assertTrue("Test agenda automation values set is NOT empty", testAgenda.isAutomationValuesEmpty());
        AutomationValuePlugin automationValuePlugin = new AutomationValuePlugin();
        Element element = buildElement();
        PostStrategyElementParserPluginContext postStrategyParserPluginContext = new PostStrategyElementParserPluginContext(testAgenda, element);
        automationValuePlugin.execute(postStrategyParserPluginContext);
        Assert.assertFalse("Test agenda automation values set IS empty", testAgenda.isAutomationValuesEmpty());
        Assert.assertEquals("Test agenda has incorrect automation values size", 1, testAgenda.getAutomationValues().size());
        Assert.assertTrue("Automation Value: " + FILE_AUTOMATION_VALUE + " was NOT found", testAgenda.containsAutomationValue(FILE_AUTOMATION_VALUE));
    }

    private Element buildElement() throws Exception {
        InputStream inputStream = AutomationValuePluginTest.class.getClassLoader().getResourceAsStream(AUTOMATION_VALUE_TEST_STRATEGY_FILE);
        Element executeRoot = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getDocumentElement();
        NodeList nodeList = executeRoot.getChildNodes();
        Element element = null;
        for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {
            Node node = nodeList.item(nodeIndex);
            if (node.getNodeName().equalsIgnoreCase(AUTOMATION_VALUE_NAME)) {
                element = (Element) node;
            }
        }
        return element;
    }
}
