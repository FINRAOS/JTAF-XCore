package org.finra.jtaf.core.commands.context;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.CorrectiveContext;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.invocationtarget.InvocationTarget;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManipulateContextHelperTest {

    private ManipulateContextHelper helper = null;
    private DummyCmd cmd = null;
    private Interpreter interpreter = new Interpreter();
    private TestScript testScript = null;

    @Before
    public void setup() throws Throwable {

        Node testRoot = buildTestRoot("testscripts/ManipulateContextHelper.xml", "Manipulate");
        TestSuite testSuite = buildTestSuite(testRoot);
        Assert.assertEquals(1, testSuite.getComponentList().size());

        List<TestComponent> componentList = testSuite.getComponentList();
        testScript = (TestScript) componentList.get(0);

        cmd = new DummyCmd("Test");
        cmd.addOptionalParameter("String");
        IInvocationContext ctx = new CorrectiveContext();
        Invocation invocation = new Invocation("String");
        ((CorrectiveContext) ctx).pushInvocation(invocation, (InvocationTarget) cmd);
        interpreter.interpret(testScript);
        cmd.launch(ctx, interpreter);
        Map<String, Object> contextGlobal = new HashMap<String, Object>();
        helper = new ManipulateContextHelper(cmd, contextGlobal);

    }

    @Test
    public void testNullContext() {
        helper.setContext(null);
        helper.setValueOut("String", "Test");
        Assert.assertEquals(cmd.ctx().getObject("String"), "Test");
    }

    @Test
    public void testNotNullContext() throws Throwable {
        IInvocationContext ctx = new CorrectiveContext();
        Invocation invocation = new Invocation("String");
        ((CorrectiveContext) ctx).pushInvocation(invocation, (InvocationTarget) cmd);
        interpreter.interpret(testScript);
        cmd.launch(ctx, interpreter);
        helper.setContext(ctx);
        helper.setValueOut("String", "Test");

        Assert.assertEquals(ctx.getObject("String"), "Test");
    }

    @Test
    public void testGetVerifiedAction() throws Throwable {
        Assert.assertEquals("replace", helper.getVerifiedAction());
        helper.setValueOut("action", "prepend");
        Assert.assertEquals("prepend", helper.getVerifiedAction());
        helper.setValueOut("action", "append");
        Assert.assertEquals("append", helper.getVerifiedAction());

    }

    @Test
    public void testGetVerifiedActionFailure() throws Throwable {
        IInvocationContext ctx = new CorrectiveContext();
        Invocation invocation = new Invocation("String");
        ((CorrectiveContext) ctx).pushInvocation(invocation, (InvocationTarget) cmd);
        interpreter.interpret(testScript);
        cmd.launch(ctx, interpreter);
        helper.setContext(ctx);
        helper.setValueOut("action", "Test");
        IllegalArgumentException exception = null;
        try {
            helper.getVerifiedAction();
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        Assert.assertEquals(
                "Test: attribute 'action' must be 'replace' or 'append' or 'prepend' (or left unspecified)",
                exception.getMessage());

    }

    @Test
    public void testGetValueIn() {
        helper.setValueOut("getValueIn", "Value");
        Assert.assertEquals("Value", helper.getValueIn("getValueIn", null, null));
        // Assert.assertEquals("Value", helper.getValueIn("getValueIn", -1));
    }

    @Test
    public void testGetValueInList() {
        List<String> list = new ArrayList<String>();
        list.add("IndexOne");
        helper.setValueOut("getValueIn", list);
        Assert.assertEquals("IndexOne", helper.getValueIn("getValueIn", 0, null));
        Assert.assertEquals("IndexOne", helper.getValueIn("getValueIn", -1, null));
        Assert.assertEquals("IndexOne", helper.getValueIn("getValueIn", -1));
    }

    @Test
    public void testGetValueInMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "valueOne");
        helper.setValueOut("getValueIn", map);
        Assert.assertEquals("valueOne", helper.getValueIn("getValueIn", null, "key"));
        Assert.assertEquals("valueOne", helper.getValueIn("getValueIn", "key"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetValueOutObjectToList() {
        List<Object> list = new ArrayList<Object>();
        helper.setValueOut("testList", list, "testString");
        Assert.assertEquals(helper.getValueIn("testList", null, null), list);
        Assert.assertEquals("testString", list.get(0));

        list = ((List<Object>) helper.getValueIn("testList", null, null));

        helper.setValueOut("testList", list, "testString2", 0);
        list = ((List<Object>) helper.getValueIn("testList", null, null));
        Assert.assertEquals(helper.getValueIn("testList", null, null), list);
        Assert.assertEquals("testString2", list.get(0));
        Assert.assertEquals("testString", list.get(1));

    }

    @Test
    public void testSetValueOutListToList() {
        List<String> list = new ArrayList<String>();
        list.add("StringOne");

        List<String> list2 = new ArrayList<String>();
        list2.add("StringTwo");

        helper.setValueOut("testList", list, list2);
        Assert.assertEquals("StringOne", list.get(0));
        Assert.assertEquals("StringTwo", list.get(1));

        helper.setValueOut("testList", list, list2, 0);
        Assert.assertEquals("StringOne", list.get(1));
        Assert.assertEquals("StringTwo", list.get(0));

    }

    @Test
    public void testSetValueOutObjectToMap() {
        Map<String, String> map = new HashMap<String, String>();

        helper.setValueOut("testMap", map, "value", "key");
        Assert.assertEquals("value", helper.getValueIn("testMap", null, "key"));

    }

    @Test
    public void testGetRequiredObject() {
        try {
            helper.getRequiredObject("key");
        } catch (Throwable e) {
            Assert.assertEquals("Test : The parameter with key [key] is not set.", e.getMessage());

        }

        helper.setValueOut("test", "string");
        Assert.assertEquals("string", helper.getRequiredObject("test"));

    }

    @Test
    public void testGetRequiredString() {
        try {
            helper.getRequiredString("key");
        } catch (Throwable e) {
            Assert.assertEquals("Test: missing required String attribute 'key'", e.getMessage());

        }

        helper.setValueOut("test", "string");
        Assert.assertEquals("string", helper.getRequiredString("test"));

    }

    @Test
    public void testGetStringOrDefault() {

        helper.setValueOut("test", "string");
        Assert.assertEquals("string", helper.getStringOrDefault("test", "test2"));

    }

    @Test
    public void testOptionalString() {

        helper.setValueOut("test", "string");
        Assert.assertEquals("string", helper.getOptionalString("test"));

    }

    @Test
    public void testGetRequiredInteger() {
        try {
            helper.getRequiredInteger("key");
        } catch (Throwable e) {
            Assert.assertEquals("Test: missing required integer attribute 'key'", e.getMessage());

        }

        helper.setValueOut("test", "1");
        Assert.assertEquals(1, helper.getRequiredInteger("test"));

    }

    @Test
    public void testGetOptionalInteger() {

        Assert.assertEquals(null, helper.getOptionalInteger("key"));

        helper.setValueOut("test", "1");
        Assert.assertEquals(1, (int) helper.getOptionalInteger("test"));

    }

    @Test
    public void testGetRequiredIntegerOrDefault() {
        Assert.assertEquals(5, helper.getIntegerOrDefault("test", 5));
        helper.setValueOut("test", "1");
        Assert.assertEquals(1, helper.getIntegerOrDefault("test", 5));

    }

    @Test
    public void testGetRequiredFloat() {
        try {
            helper.getRequiredFloat("key");
        } catch (Throwable e) {
            Assert.assertEquals("Test: missing required float attribute 'key'", e.getMessage());

        }

        helper.setValueOut("test", "1");
        Assert.assertEquals(1f, helper.getRequiredFloat("test"), 0);
    }

    @Test
    public void testGetRequiredFloatOrDefault() {
        Assert.assertEquals(5f, helper.getFloatOrDefault("test", 5f), 0);
        helper.setValueOut("test", "1");
        Assert.assertEquals(1f, helper.getFloatOrDefault("test", 5f), 0);

    }

    @Test
    public void testGetRequiredBoolean() {
        try {
            helper.getRequiredBoolean("key");
        } catch (Throwable e) {
            Assert.assertEquals("Test: missing required boolean attribute 'key'", e.getMessage());

        }

        helper.setValueOut("test", "true");
        Assert.assertEquals(true, helper.getRequiredBoolean("test"));
        helper.setValueOut("test", "false");
        Assert.assertEquals(false, helper.getRequiredBoolean("test"));
        helper.setValueOut("test", "neither");

        try {
            helper.getRequiredBoolean("test");
        } catch (Throwable e) {
            Assert.assertEquals("Test: attribute 'test' must be either 'true' or 'false'", e
                    .getMessage());

        }

    }

    @Test
    public void testGetRequiredBooleanOrDefault() {

        Assert.assertEquals(true, helper.getBooleanOrDefault("key", true));

        helper.setValueOut("test", "true");
        Assert.assertEquals(true, helper.getBooleanOrDefault("test", false));
        helper.setValueOut("test", "false");
        Assert.assertEquals(false, helper.getBooleanOrDefault("test", true));
        helper.setValueOut("test", "neither");

        try {
            helper.getBooleanOrDefault("test", true);
        } catch (Throwable e) {
            Assert.assertEquals(
                    "Test: attribute 'test' must be either 'true' or 'false' (or left unspecified)",
                    e.getMessage());

        }

    }

    @Test
    public void testGetOptionalObject() throws Throwable {

        Map<String, Object> contextGlobal = new HashMap<String, Object>();
        contextGlobal.put("test2", "testString");
        contextGlobal.put("startIndex", "startIndexString");
        contextGlobal.put("endIndex", "endIndexString");

        helper = new ManipulateContextHelper(cmd, contextGlobal);

        helper.setValueOut("test", "$contextKey(test2)");
        Assert.assertEquals("testString", (String) helper.getOptionalObject("test"));
        
        helper.setValueOut("testStartIndex", "$contextKeyy(startIndex)");
        Assert.assertEquals("startIndexString", (String) helper.getOptionalObject("testStartIndex"));
        
        helper.setValueOut("testEndIndex", "$contextKeyy(endIndex");
        Assert.assertEquals("", (String) helper.getOptionalObject("testEndIndex"));
        RuntimeException exception = null;
        try {

            helper.setValueOut("failureTest", "$contextKey(test3)");
            helper.getOptionalObject("failureTest");
        } catch (RuntimeException e) {
            exception = e;
        }

        Assert.assertEquals(
                exception.getMessage(),
                "Oops! Processing 'failureTest'='$contextKey(test3)' key fails! Can't find run time parameter 'test3'! You have put it to global content before...");

    }

    private class DummyCmd extends AbstractContextCmd {

        DummyCmd(String name) throws NameFormatException {
            super(name);
            // TODO Auto-generated constructor stub
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.finra.jtaf.core.model.invocationtarget.Command#execute(org.finra
         * .jtaf.core.model.execution.IInvocationContext)
         */
        @Override
        protected void execute() throws Throwable {

        }

    }

    protected Node buildTestRoot(String scriptFileName, String testOfInterestName) throws Exception {
        Node testRoot = null;
        InputStream inputStream = new FileInputStream(scriptFileName);
        Node suiteNode = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                inputStream).getDocumentElement();
        NodeList suiteNodeChildNodes = suiteNode.getChildNodes();
        for (int suiteNodeChildIndex = 0; suiteNodeChildIndex < suiteNodeChildNodes.getLength(); suiteNodeChildIndex++) {
            Node suiteNodeChildNode = suiteNodeChildNodes.item(suiteNodeChildIndex);
            if (suiteNodeChildNode.getNodeName().equalsIgnoreCase("test")
                    && suiteNodeChildNode.getAttributes().getNamedItem("name").getTextContent()
                            .equals(testOfInterestName)) {
                testRoot = suiteNodeChildNode;
                break;
            }
        }
        return testRoot;
    }

    protected TestSuite buildTestSuite(Node testRoot) throws Exception {
        TestSuite testSuite = new TestSuite("dummy test suite");
        TestScript testScript = AutomationEngine.getInstance().getScriptParser().processTestScript(
                (Element) testRoot, new MessageCollector());
        testSuite.add(testScript);
        return testSuite;
    }

}
