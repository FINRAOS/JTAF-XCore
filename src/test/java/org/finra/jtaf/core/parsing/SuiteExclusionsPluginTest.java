package org.finra.jtaf.core.parsing;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostSuiteParserPluginContext;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;


public class SuiteExclusionsPluginTest extends ParserPluginTest {
    private static final String TAG_NAME = "exclusions";
    private static final String SCRIPT_FILE_NAME = "SuiteExclusionParserTest.xml";
    private static final String UNEXPECTED_ELEMENT_SCRIPT_FILE_NAME = "UnexpectedElementSuiteExclusion.xml";
    private static final String EXCLUSION_SUITE_NAME = "JTAF CORE";
    private static final String EXCLUSION_TEST_NAME = "Block Testing";

    @Test()
    public void testExecute() throws Exception {
        SuiteExclusionsPlugin suiteExclusionsPlugin = new SuiteExclusionsPlugin();
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("parser_testing/" + SCRIPT_FILE_NAME).getDocumentElement();
        TestSuite testSuite = AutomationEngine.getInstance().getScriptParser().processTestSuite(element, new MessageCollector(), SCRIPT_FILE_NAME);
        PostSuiteParserPluginContext postSuiteParserPluginContext = new PostSuiteParserPluginContext(null, testSuite, element);
        suiteExclusionsPlugin.execute(postSuiteParserPluginContext);

        Assert.assertEquals(1, testSuite.getExclusions().getDependenciesSuites().size());
        Assert.assertTrue(testSuite.getExclusions().getDependenciesSuites().contains(EXCLUSION_SUITE_NAME));

        Assert.assertEquals(1, testSuite.getExclusions().getDependenciesTests().size());
        Assert.assertTrue(testSuite.getExclusions().getDependenciesTests().contains(EXCLUSION_TEST_NAME));
    }

    @Test(expected = ParserPluginException.class)
    public void testExecuteUnexpectedElement() throws Exception {
        SuiteExclusionsPlugin suiteExclusionsPlugin = new SuiteExclusionsPlugin();
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("parser_testing/" + UNEXPECTED_ELEMENT_SCRIPT_FILE_NAME).getDocumentElement();
        TestSuite testSuite = AutomationEngine.getInstance().getScriptParser().processTestSuite(element, new MessageCollector(), UNEXPECTED_ELEMENT_SCRIPT_FILE_NAME);
        PostSuiteParserPluginContext postSuiteParserPluginContext = new PostSuiteParserPluginContext(null, testSuite, element);
        suiteExclusionsPlugin.execute(postSuiteParserPluginContext);
    }

    @Test
    public void testGetTagName() {
        Assert.assertEquals(TAG_NAME, new SuiteExclusionsPlugin().getTagName());
    }
}
