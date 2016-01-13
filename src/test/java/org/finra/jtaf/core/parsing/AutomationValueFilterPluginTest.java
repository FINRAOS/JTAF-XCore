package org.finra.jtaf.core.parsing;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.parsing.AutomationValueFilterPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostAllParserPluginContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AutomationValueFilterPluginTest {
    private static final String AUTOMATION_VALUE_A = "A";
    private static final String AUTOMATION_VALUE_B = "B";
    private static final String AUTOMATION_VALUE_C = "C";
    private static final String AUTOMATION_VALUE_D = "D";

    private TestAgenda testAgenda;
    private TestScript dummyScriptA;
    private TestScript dummyScriptB1;
    private TestScript dummyScriptB2;
    private TestScript dummyScriptC;
    private PostAllParserPluginContext postAllParserPluginContext;

    @Before
    public void setup() throws NameFormatException {
        testAgenda = new TestAgenda();

        dummyScriptA = buildDummyScript(testAgenda, "DummyScriptA", AUTOMATION_VALUE_A);
        dummyScriptB1 = buildDummyScript(testAgenda, "DummyScriptB1", AUTOMATION_VALUE_B);
        dummyScriptB2 = buildDummyScript(testAgenda, "DummyScriptB2", AUTOMATION_VALUE_B);
        dummyScriptC = buildDummyScript(testAgenda, "DummyScriptC", AUTOMATION_VALUE_C);

        postAllParserPluginContext = new PostAllParserPluginContext(testAgenda, null);
    }

    private TestScript buildDummyScript(TestAgenda testAgenda, String name, String automationValueA) throws NameFormatException {
        TestScript result = new TestScript(name, false);
        result.setAutomationValue(automationValueA);
        result.setFileName("DummyFileName.xml");
        testAgenda.getTestScripts().add(result);
        return result;
    }

    @Test
    public void testExecuteNoAutomationValues() throws NameFormatException, ParserPluginException {
        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptA));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptB1));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptB2));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptC));
    }

    @Test
    public void testExecuteOneAutomationValueNoIncludedTests() throws NameFormatException, ParserPluginException {
        testAgenda.addAutomationValue(AUTOMATION_VALUE_D);

        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptA));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptB1));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptB2));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptC));
    }

    @Test
    public void testExecuteOneAutomationValueOneIncludedTest() throws NameFormatException, ParserPluginException {
        testAgenda.addAutomationValue(AUTOMATION_VALUE_A);

        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptA));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptB1));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptB2));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptC));
    }

    @Test
    public void testExecuteOneAutomationValueTwoIncludedTests() throws NameFormatException, ParserPluginException {
        testAgenda.addAutomationValue(AUTOMATION_VALUE_B);

        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptA));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptB1));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptB2));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptC));
    }

    @Test
    public void testExecuteTwoAutomationValuesThreeIncludedTests() throws NameFormatException, ParserPluginException {
        testAgenda.addAutomationValue(AUTOMATION_VALUE_A);
        testAgenda.addAutomationValue(AUTOMATION_VALUE_B);

        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptA));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptB1));
        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptB2));
        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptC));
    }

    @Test
    public void testExecuteTwoAutomationValuesNoAutomationValueInTestOrStrategy() throws NameFormatException, ParserPluginException {
        dummyScriptA.setAutomationValue(null);

        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertTrue(testAgenda.getTestScripts().contains(dummyScriptA));
    }

    @Test
    public void testExecuteTwoAutomationValuesNoAutomationValueInTestOneInStrategy() throws NameFormatException, ParserPluginException {
        testAgenda.addAutomationValue(AUTOMATION_VALUE_A);

        dummyScriptA.setAutomationValue(null);

        AutomationValueFilterPlugin automationValueFilterPlugin = new AutomationValueFilterPlugin();
        automationValueFilterPlugin.execute(postAllParserPluginContext);

        Assert.assertFalse(testAgenda.getTestScripts().contains(dummyScriptA));
    }
}
