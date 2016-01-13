package org.finra.jtaf.core.parallel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConcurrentSchedulerTest {


    @Test
    public void testCheckTestsStatus() throws Exception {

        if (AutomationEngine.getInstance().getTestAgenda() == null) {
            AutomationEngine.getInstance().buildModel(new File("testlibrary"), new File("testscripts"));
            //  TraceabilityMatrix.produceTraceabilityMatrix(testAgenda);
        }
        AutomationEngine.getInstance().getTestDigraph().updateTestStatus("TestB", "FAILED");
        AutomationEngine.getInstance().getTestAgenda().setThreadCount("-1");
        ConcurrentScheduler scheduler = new ConcurrentScheduler();

        //scheduler.registerTestName("Test");
        scheduler.finished();


        AutomationEngine.getInstance().getTestDigraph().updateTestStatus("TestB", "READY");
        AutomationEngine.getInstance().getTestDigraph().updateTestStatus("TestA", "FAILED");

        scheduler.finished();

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
