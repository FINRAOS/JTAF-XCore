package org.finra.jtaf.core.parallel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParallelScriptRunnerTest {

    private TestScript testScript = null;
    private ParallelScriptRunner runner = null;

    @Test
    public void testCreateJunitName() throws Exception {
        Node testRoot = buildTestRoot("testscripts/ManipulateContextHelper.xml", "Manipulate");
        TestSuite testSuite = buildTestSuite(testRoot);

        List<TestComponent> componentList = testSuite.getComponentList();
        testScript = (TestScript) componentList.get(0);

        List<String> crs = new ArrayList<String>();
        crs.add("Test");
        testScript.setCRs(crs);
        runner = new ParallelScriptRunner(testScript);
        Assert.assertEquals(runner.getName(),
                "/dummy test suite/Manipulate CR=\"Test\" [ISSUE EXISTS]");

        testScript.setCRs(new ArrayList<String>());
        runner = new ParallelScriptRunner(testScript);
        Assert.assertEquals(runner.getName(), "/dummy test suite/Manipulate");

        testScript.setStatus("Pass");
        testScript.setCRs(null);
        runner = new ParallelScriptRunner(testScript);
        Assert.assertEquals(runner.getName(), "/dummy test suite/Manipulate STATUS=\"Pass\"");

        testScript.setStatus(null);
        testScript.setIssue("");
        runner = new ParallelScriptRunner(testScript);
        Assert.assertEquals(runner.getName(), "/dummy test suite/Manipulate");

        testScript.setStatus(null);
        testScript.setIssue(null);
        runner = new ParallelScriptRunner(testScript);
        Assert.assertEquals(runner.getName(), "/dummy test suite/Manipulate");

        testScript.setStatus(null);
        testScript.setIssue("Issue");
        runner = new ParallelScriptRunner(testScript);
        Assert.assertEquals(runner.getName(), "/dummy test suite/Manipulate [ISSUE EXISTS]");

    }

    @Test
    public void testRunJtafTestScript() throws Throwable {
        Node testRoot = buildTestRoot("testscripts/ParallelScriptRunner.xml", "Runner");
        TestSuite testSuite = buildTestSuite(testRoot);

        List<TestComponent> componentList = testSuite.getComponentList();
        testScript = (TestScript) componentList.get(0);
        if (AutomationEngine.getInstance().getTestDigraph().getVertex(testScript.getName()) != null) {
            AutomationEngine.getInstance().getTestDigraph().updateTestStatus(testScript.getName(),
                    "FAILED");

        } else {
            MyNode node = new MyNode(testScript);
            node.setTestStatus("FAILED");

            AutomationEngine.getInstance().getTestDigraph().addVertex(node);
        }
      
        runner = new ParallelScriptRunner(testScript);

        Throwable error = null;
        try {
            runner.runJtafTestScript();
        } catch (Throwable e) {
            error = e;
        }
        Assert.assertTrue(error.getClass().getSimpleName().equals("AssertionError"));
        Assert.assertTrue(error.getMessage().equals("One or more Dependent tests failed"));
        
        
        AutomationEngine.getInstance().getTestDigraph().updateTestStatus(testScript.getName(),
                "SUCCESS");
        try {
            runner.runJtafTestScript();
        } catch (Throwable e) {
            error = e;
        }
        Assert.assertTrue(error.getClass().getSimpleName().equals("AssertionFailedError"));

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

    private class MyNode extends DiNode {

        private String testName = ""; // This shouldnt change after being set
                                      // initially
        private String testStatus = "";
        private TestScript testScript = null; // This shouldnt change after
                                              // being set initially

        MyNode(TestScript ts) {
            super(ts);
            this.testName = ts.getName();
            this.testScript = ts;
        }

        public String getTestStatus() {
            return testStatus;
        }

        protected void setTestStatus(String newTestStatus) {
            testStatus = newTestStatus;
        }

        public String getTestName() {
            return testName;
        }

        public TestScript getTestScript() {
            return testScript;
        }
    }

}
