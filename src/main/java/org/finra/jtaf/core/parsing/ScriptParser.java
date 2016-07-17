/*
 * (C) Copyright 2014 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.finra.jtaf.core.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.CommandRegistry;
import org.finra.jtaf.core.exceptions.DependencyException;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.test.Requirement;
import org.finra.jtaf.core.model.test.TestNamespace;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.model.test.digraph.Dependencies;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.finra.jtaf.core.parsing.exceptions.ExceptionAccumulator;
import org.finra.jtaf.core.parsing.exceptions.MissingAttributeException;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.parsing.helpers.AttributeHelper;
import org.finra.jtaf.core.parsing.helpers.ParserHelper;
import org.finra.jtaf.core.plugins.parsing.IPostParseSuitePlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;
import org.finra.jtaf.core.plugins.parsing.PostSuiteParserPluginContext;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.finra.jtaf.core.utilities.ExcelFileParser;
import org.finra.jtaf.core.utilities.StringHelper;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;

/**
 * FIXME This is a "just get the job done" implementation. I plan to replace
 * this class as soon as I get the chance. (I mean... ay ay ay! This is bad!)
 * 
 */
public class ScriptParser {

    static Logger logger = Logger.getLogger(ScriptParser.class.getPackage().getName());
    private final DocumentBuilder db;
    private TestDigraph digraph;
    private StatementParser stmtParser;
    private Document d;
    private CommandRegistry commandRegistry;

	private List<IPostParseSuitePlugin> postParseSuitePlugins;
	private List<IPostParseTestPlugin> postParseTestPlugins;
    

    public ScriptParser() throws ParserConfigurationException {
        db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        stmtParser = new StatementParser();
    }

    public void setDigraph(TestDigraph digraph) {
        this.digraph = digraph;
    }
		
	public void setPostParseSuitePlugins(List<IPostParseSuitePlugin> plugins) {
		postParseSuitePlugins = plugins;
	}
	
	
	public void setPostParseTestPlugins(List<IPostParseTestPlugin> plugins) {
		postParseTestPlugins = plugins;
	}
	
    public void setCommandRegistry(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }
    public final TestNamespace handleTestSource(File f, MessageCollector mc)
            throws NameFormatException, SAXException, IOException, ParsingException {
        if (!f.exists()) {
            throw new FileNotFoundException(f.getAbsolutePath());
        }
        TestNamespace testNamespace = new TestNamespace(f.getName());
        if (f.isDirectory() && !f.isHidden()) {
            ExceptionAccumulator acc = new ExceptionAccumulator();

            for (File child : f.listFiles()) {
                if (child.isDirectory()) {
                    try {
                        testNamespace.add(handleTestSource(child, mc));
                    } catch (Throwable th) {
                        mc.error(th.getMessage());
                        acc.add(th);
                    }
                } else { // It's a file
                    try {
                        TestSuite ts = handleTestSuite(child, mc);
                        if (ts != null) {
                            testNamespace.add(ts);
                            // run all post suite parse plugins
                            
                            Node suiteRootNode = (Node) (d.getDocumentElement());
                            // TODO: This needs to check for null, otherwise it
                            // crashes
                     
                            if (postParseSuitePlugins != null) {
                                for (IPostParseSuitePlugin p : postParseSuitePlugins) {
                                    if (suiteContainsTheTag(suiteRootNode, p.getTagName())) {
                                        p.execute(new PostSuiteParserPluginContext(commandRegistry, ts, suiteRootNode));
                                    }
                                }
                            }
                        }
                    } catch (Throwable th) {
                        mc.error(th.getMessage());
                        acc.add(th);
                    }
                }
            }

            if (!acc.isEmpty()) {
                throw acc;
            }
        }
        return testNamespace;
    }

    private boolean suiteContainsTheTag(Node suiteRootNode, String tagName) {
        NodeList children = suiteRootNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equalsIgnoreCase(tagName)) {
                return true;
            }
        }
        return false;
    }

    private TestSuite handleTestSuite(File f, MessageCollector mc) throws ParsingException,
            NameFormatException, IOException {
        if (!f.exists()) {
            throw new FileNotFoundException(f.getAbsolutePath());
        }
        TestSuite testSuite = null;
        if (f.isFile() && f.getName().endsWith(".xml")) { // This should be a
                                                          // TestSuite
            mc.push("In test file " + f.getAbsolutePath());
            try {
                d = db.parse(f);
                testSuite = processTestSuite(d.getDocumentElement(), mc, f.getName());
                for (TestScript ts : processTestScripts(d.getDocumentElement(), mc)) {
                    try {
                        if (!digraph.addVertex(new DiNode(ts))) {
                            throw new DependencyException("Duplicate test name '" + ts.getName()
                                    + "' found at: "
                                    + digraph.getVertex(ts.getName()).getTestScript().getFullName());
                        }
                        ts.setFileName(f.getName());

                        testSuite.add(ts);
                        // run post parse test plugins
                        Node testRootNode = getTestRootNode(d.getDocumentElement(), ts);
                        for (IPostParseTestPlugin p : postParseTestPlugins) {
                            if (testContainsTheTag(testRootNode, p.getTagName())) {
                                p.execute(new PostTestParserPluginContext(commandRegistry, testSuite, testRootNode));
                            }
                        }
                    } catch (DependencyException de) {
                        mc.error(de.getMessage());
                        throw de;
                    } catch (Throwable th) {
                        mc.error(th.getMessage());
                        logger.fatal(th.getMessage());
                    }
                }
            } catch (SAXException e) {
                mc.error(e.getMessage());
                // throw e;
            } catch (IOException e) {
                mc.error(e.getMessage());
                throw e;
            } finally {
                mc.pop();
            }
        }
        return testSuite;
    }

    private boolean testContainsTheTag(Node testRootNode, String tagName) {
        NodeList children = testRootNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equalsIgnoreCase(tagName)) {
                return true;
            }
        }
        return false;
    }

    private Node getTestRootNode(Element documentElement, TestScript ts) {
        NodeList list = documentElement.getElementsByTagName("test");
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n.getAttributes().getNamedItem("name").getNodeValue().equals(ts.getOriginalName())) {
                return n;
            }
        }
        return null;
    }

    public final TestSuite processTestSuite(Element element, MessageCollector mc, String fileName)
            throws ParsingException, NameFormatException {
        TestSuite testSuite = new TestSuite(fileName);
        if (element.getNodeName().equalsIgnoreCase("testsuite")) {
            AttributeHelper testScriptAttributeHelper = new AttributeHelper(element);

            String testSuiteName = null;
            try {
                testSuiteName = testScriptAttributeHelper.getRequiredString("name");
                testSuite.setTestSuiteName(testSuiteName);
            } catch (MissingAttributeException e) {
                mc.push(e.getMessage());
                logger.debug("Oops! Test suite has no 'name' attribute! ('" + element.toString()
                        + "')");
            }
            
            testSuite.setDependencies(new Dependencies(null, null));
            testSuite.setExclusions(new Dependencies(null, null));

            return testSuite;
        } else {
            UnexpectedElementException z = new UnexpectedElementException(element);
            mc.error(z.getMessage());
            throw z;
        }
    }

    private List<TestScript> processTestScripts(Element element, MessageCollector mc)
            throws ParsingException {
        if (element.getNodeName().equalsIgnoreCase("testsuite")) {
            List<TestScript> testScripts = null;
            AttributeHelper testScriptAttributeHelper = new AttributeHelper(element);

            String testSuiteName = null;
            try {
                testSuiteName = testScriptAttributeHelper.getRequiredString("name");
            } catch (MissingAttributeException e) {
                mc.push(e.getMessage());
                logger.debug("Oops! Test suite hasn't 'name' attibite! ('" + element.toString()
                        + "')");
            }

            // MULTITHREADED 2012.4 Changes START
            // -------------------------------------
            // This will seperate out the elements at the testsuite level
            ArrayList<Element> testSuiteChildren = (ArrayList<Element>) ParserHelper
                    .getChildren(element);
            ArrayList<Element> tests = new ArrayList<Element>();

            for (Element e : testSuiteChildren) {
                if (e.getNodeName().equalsIgnoreCase("test")) {
                    tests.add(e);
                }
            }
            testScripts = parseTests(tests, mc, testSuiteName);

            // MULTITHREADED 2012.4 Changes END
            // -------------------------------------
            return testScripts;
        } else {
            UnexpectedElementException z = new UnexpectedElementException(element);
            mc.error(z.getMessage());
            throw z;
        }
    }

    private List<TestScript> parseTests(List<Element> elementList, MessageCollector mc,
            String testSuiteName) throws ExceptionAccumulator {
        List<TestScript> testScripts = new ArrayList<TestScript>();
        ExceptionAccumulator acc = new ExceptionAccumulator();
        for (Element child : elementList) {
            try {
                AttributeHelper testAttributeHelper = new AttributeHelper(child);
                String testName = testAttributeHelper.getOptionalString("name");
                int testLoopNumber = getLoopNumber(testAttributeHelper, mc);

                String testDataFile = getTestDataFile(child);
                String sheetName = getSheetName(child);
                List<List<String>> testData = null;
                int tsNamefromFilePosition = -1;
                if (testDataFile != null) {
                    if (testDataFile.endsWith(".xlsx")) {
                        testData = getExcelDataFromFile(testDataFile, sheetName, mc, true);
                    } else if (testDataFile.endsWith(".xls")) {
                        testData = getExcelDataFromFile(testDataFile, sheetName, mc, false);
                    } else if (testDataFile.endsWith(".csv")) {
                        testData = getCSVDataFromFile(testDataFile, mc);
                    } else {
                        logger.fatal("Oops! can't parse test data file ('" + testDataFile
                                + "'). Supported 'xls', 'xlsx' and 'csv' extentions.");
                    }
                    tsNamefromFilePosition = getTSNameFromDataFilePosition(testData);
                }

                for (int i = 1; i <= testLoopNumber; i++) {
                    TestScript ts = processTestScript(child, mc);
                    ts.setTestSuiteName(testSuiteName);

                    if (testData != null && testData.size() > 0) {
                        int rowNumber = 0;
                        List<String> titleRow = null;
                        for (List<String> row : testData) {
                            String tsNamefromFile = "";
                            if (tsNamefromFilePosition >= 0) {
                                tsNamefromFile = "testNameFromDataFile-"
                                        + row.get(tsNamefromFilePosition);
                            }
                            if (0 == rowNumber) {
                                titleRow = row;
                            } else {
                                if (testLoopNumber != 1) {
                                    ts.setName(testName + " [data file row #" + rowNumber
                                            + "; iteration #" + i + " of " + testLoopNumber + "]; "
                                            + tsNamefromFile);
                                } else {
                                    ts.setName(testName + " [data file row #" + rowNumber + "] ; "
                                            + tsNamefromFile);
                                }
                                ts.setDescription(ts.getDescription() + " ("
                                        + StringHelper.getZipAndConcatenated(titleRow, row, ", ")
                                        + ")");
                                setTestDataToTestScript(titleRow, row, ts);
                                testScripts.add(ts);
                            }
                            rowNumber++;
                            ts = processTestScript(child, mc);
                            ts.setTestSuiteName(testSuiteName);
                        }
                    } else {
                        if (testLoopNumber > 1) {
                            ts.setName(testName + " [iteration " + i + " of " + testLoopNumber
                                    + "]");
                        }
                        testScripts.add(ts);
                    }
                }
            } catch (Throwable th) {
                logger.fatal(th.getMessage());
                mc.error(th.getMessage());
                acc.add(th);
            }
        }

        if (!acc.isEmpty()) {
            throw acc;
        }

        return testScripts;
    }

    private String getTestDataFile(Element test) {
        for (Element testChild : ParserHelper.getChildren(test)) {
            if (testChild.getTagName().equalsIgnoreCase("testData")) {
                String file = testChild.getAttribute("file");
                if (file != null && file.length() > 0) {
                    return file;
                }
            }
        }
        return null;
    }

    private String getSheetName(Element test) {
        for (Element testChild : ParserHelper.getChildren(test)) {
            if (testChild.getTagName().equalsIgnoreCase("testData")) {
                String file = testChild.getAttribute("sheet");
                if (file != null && file.length() > 0) {
                    return file;
                }
            }
        }
        return null;
    }

    private List<List<String>> getExcelDataFromFile(String testDataFile, String sheetName,
            MessageCollector mc, boolean isXlsx) {
        if (testDataFile != null && testDataFile.length() > 0) {
            ExcelFileParser excelFileParser = null;
            try {
                if (sheetName != null) {
                    excelFileParser = new ExcelFileParser(testDataFile, sheetName, isXlsx);
                } else {
                    excelFileParser = new ExcelFileParser(testDataFile, isXlsx);
                }
                return excelFileParser.parseExcelFile(isXlsx);
            } catch (Exception e) {
                String errorMessage = "Oops! Can't parse excel file '" + testDataFile + "'!";
                logger.fatal(errorMessage);
                mc.error(errorMessage);
            }
        }
        return null;
    }

    private List<List<String>> getCSVDataFromFile(String testDataFile, MessageCollector mc) {
        List<List<String>> result = new ArrayList<List<String>>();
        CSVReader reader = null;
        try {
             reader = new CSVReader(new FileReader(testDataFile));
            List<String> nextLine;

            while ((nextLine = StringHelper.ArrayToList(reader.readNext())) != null) {
                if ((nextLine != null) && (nextLine.size() > 0)
                        && (!nextLine.get(0).startsWith("#"))) {
                    result.add(nextLine);
                }
            }
        } catch (Exception e) {
            logger.fatal("Oops! Can't open file '" + testDataFile + "'!");
            return null;
        } finally {
        	if (reader != null) {
        		try {
        		reader.close();
        		} catch (Exception e) {
        			//Dont care
                    logger.fatal("Oops! Can't close file '" + testDataFile + "'!");

        		}
        	}
        }
        return result;
    }

    private int getLoopNumber(AttributeHelper testAttributeHelper, MessageCollector mc) {
        String testLoop = testAttributeHelper.getOptionalString("loop");
        int testLoopNumber = 1;
        if (testLoop != null) {
            try {
                testLoopNumber = Integer.parseInt(testLoop);
                return testLoopNumber;
            } catch (java.lang.NumberFormatException e) {
                String errorMessage = "Oops! Can't parse test 'loop' property ('"
                        + testLoop
                        + "'). It has be number like '3'. This parameter means number of execution for this test. Fix your test case script, please!";
                logger.fatal(errorMessage);
                mc.error(errorMessage);
            }
        }
        return 1;
    }

    // TODO: Start with here
    // This gets passed the row of titles, a row of data, and the testscript
    // associated with the file
    // It then goes through and adds the
    // this gets called for every row of data in the datafile
    // once this method executes, the testscript is added to a list of
    // testscripts.
    // so each row of data is effectively a new test script
    private void setTestDataToTestScript(List<String> title, List<String> data, TestScript ts) {
        for (Invocation statement : ts.getBody()) {
            if (statement instanceof Invocation) {
                Map<String, Object> parameters = ((Invocation) statement).getParameters();
                for (int i = 0; i < title.size(); i++) {
                    if (i < data.size()) {
                        parameters.put(title.get(i), data.get(i));
                    } else {
                        parameters.put(title.get(i), "");
                    }
                }
            }
            /**
             * if (statement instanceof TryRecoverCleanup) { TryRecoverCleanup
             * trcSt = (TryRecoverCleanup) statement;
             * 
             * // StatementList statementList = trcSt.getTry(); //
             * statementList.addAll(trcSt.getRecover()); //
             * statementList.addAll(trcSt.getCleanup()); // TODO: Ask author why
             * this was placed and when?
             * 
             * for (IStatement currentStatement : statementList) { if
             * (currentStatement instanceof Invocation) { Map<String, Object>
             * parameters = ((Invocation) currentStatement) .getParameters();
             * for (int i = 0; i < title.size(); i++) { if (i < data.size()) {
             * parameters.put(title.get(i), data.get(i)); } else {
             * parameters.put(title.get(i), ""); } } } } }
             **/
        }
    }

    public final TestScript processTestScript(Element elem, MessageCollector mc)
            throws ParsingException {
        preprocessTestScript(ParserHelper.getRequireElement(elem, "teststeps"), mc);

        AttributeHelper ah = new AttributeHelper(elem);
        String name = null;
        try {
            name = ah.getRequiredString("name");
        } catch (MissingAttributeException e) {
            mc.push(e.getMessage());
            throw e;
        }

        boolean isCaptureSystemInformation = false;
        String isCaptureSystemInformationStr = ah.getOptionalString("isCaptureSystemInformation");
        if (isCaptureSystemInformationStr != null) {
            if (isCaptureSystemInformationStr.equalsIgnoreCase("true")
                    || isCaptureSystemInformationStr.equalsIgnoreCase("1")
                    || isCaptureSystemInformationStr.equalsIgnoreCase("yes")) {
                isCaptureSystemInformation = true;
            } else if (!isCaptureSystemInformationStr.equalsIgnoreCase("false")
                    && !isCaptureSystemInformationStr.equalsIgnoreCase("0")
                    && !isCaptureSystemInformationStr.equalsIgnoreCase("no")) {
                logger.fatal("Oops! Can't parse '"
                        + isCaptureSystemInformationStr
                        + "'. It can be only one of 'true'/'false', '1'/'0' or 'yes'/'no'. Fix your test script, please!..");
            }
        }

        try {
            mc.push("In test " + name);
            TestScript testScript = new TestScript(name, isCaptureSystemInformation);
            testScript.setBody(stmtParser.processStatementList(ParserHelper.getRequireElement(elem,
                    "teststeps"), mc));

            // Multithread 2012.4 CHANGES START------------------------
            // This is to reference the local test case values
            Element dependentElement = ParserHelper.getFirstChildElementCaseInsensitive(elem,
                    "dependencies");
            Set<String> dependentListTests = new HashSet<String>();
            Set<String> dependentListTestSuites = new HashSet<String>();

            if (dependentElement != null) {
                for (Element e : ParserHelper.getChildren(dependentElement)) {
                    if (e.getNodeName().equalsIgnoreCase("test")) {
                        dependentListTests.add(new AttributeHelper(e).getRequiredString("name"));
                    } else if (e.getNodeName().equalsIgnoreCase("testsuite")) {
                        dependentListTestSuites.add(new AttributeHelper(e)
                                .getRequiredString("name"));
                    }
                }
            }

            Element exclusionElement = ParserHelper.getFirstChildElementCaseInsensitive(elem,
                    "exclusions");
            Set<String> excludedListTests = new HashSet<String>();
            Set<String> excludedListTestSuites = new HashSet<String>();
            if (exclusionElement != null) {
                for (Element e : ParserHelper.getChildren(exclusionElement)) {
                    if (e.getNodeName().equalsIgnoreCase("test")) {
                        excludedListTests.add(new AttributeHelper(e).getRequiredString("name"));
                    } else if (e.getNodeName().equalsIgnoreCase("testsuite")) {
                        excludedListTestSuites
                                .add(new AttributeHelper(e).getRequiredString("name"));
                    }
                }
            }
            testScript.setDependencies(dependentListTestSuites, dependentListTests);
            testScript.setExclusions(excludedListTestSuites, excludedListTests);
            // Multithread 2012.4 CHANGES END------------------------

            testScript.setTestCaseID(ah.getOptionalString("testcaseid"));

            Element issuesElem = ParserHelper.getOptionalElement(elem, "issues");
            if (issuesElem != null) {
                testScript.setIssue(processIssue(issuesElem));
                testScript.setCRs(processCRs(issuesElem));
            }

            testScript.setStatus(ah.getOptionalString("status"));

            Element avElem = ParserHelper.getFirstChildElementCaseInsensitive(elem,
                    "automationvalue");
            if (avElem != null) {
            	// not sure why this doesn't just call avElem.getTextContent()
                testScript.setAutomationValue(stmtParser.processString(avElem, mc).toString());
            }

            // TODO: Should we make this into a list?
            // retval.getRequirements() = new
            Element covElem = ParserHelper.getOptionalElement(elem, "coverage");
            if (covElem != null) {

                // Requirement List
                List<Element> children = ParserHelper.getChildren(covElem);
                ArrayList<Requirement> requirements = new ArrayList<Requirement>();
                StringBuffer coverage = new StringBuffer();
                if (!children.isEmpty()) {
                    for (Element child : children) {
                        Requirement requirement = new Requirement();
                        String type = child.getAttribute("type");
                        String value = child.getTextContent();

                        if (!type.equals("")) {
                            requirement.setType(child.getAttribute("type"));
                            requirement.setValue(value.trim());
                            requirements.add(requirement);
                        }

                        // to maintain coverage for tracebility matrix
                        coverage = coverage.append(value.trim() + ",");
                    }
                    testScript.setRequirements(requirements);
                    testScript.setCoverage(coverage.substring(0, coverage.length() - 1));
                } else {
                    testScript.setCoverage(stmtParser.processString(covElem, mc).toString().trim());
                }
            }

            Element descElem = ParserHelper.getOptionalElement(elem, "desc");
            if (descElem != null) {
                testScript.setDescription(stmtParser.processString(descElem, mc).toString());
            }

            return testScript;
        } catch (NameFormatException e) {
            mc.error(e.getMessage());
            throw new ParsingException(e);
        } finally {
            mc.pop();
        }
    }

    private void preprocessTestScript(Element elem, MessageCollector mc) {
        List<Element> children = ParserHelper.getChildren(elem);
        if (children.isEmpty()) {
            return;
        }

        // If we detect a Teardown command, then we need to create
        // a Try...Cleanup block
        final Element last = children.get(children.size() - 1);
        if (last.getNodeName().equalsIgnoreCase("teardown")) {
            final Element safety = elem.getOwnerDocument().createElement("TryRecoverCleanup");
            final Element tryBlock = elem.getOwnerDocument().createElement("try");
            final Element cleanupBlock = elem.getOwnerDocument().createElement("cleanup");

            safety.appendChild(tryBlock);
            safety.appendChild(cleanupBlock);

            for (int i = 0, max = children.size() - 1; i < max; ++i) {
                Element next = children.get(i);
                elem.removeChild(next);
                tryBlock.appendChild(next);
            }

            elem.removeChild(last);
            cleanupBlock.setAttribute("isTearDown", "true");
            for (Element e : ParserHelper.getChildren(last)) {
                cleanupBlock.appendChild(e);
            }
            elem.appendChild(safety);
        }
    }

    private String processIssue(Element elem) {
        String retval = "";
        NodeList nl = elem.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.TEXT_NODE) {
                retval += n.getNodeValue();
            }
        }

        if (!retval.equals("")) {
            return retval.trim();
        }
        else {
            return null;
        }
    }

    private List<String> processCRs(Element elem) {
        List<String> retval = new ArrayList<String>();
        for (Element child : ParserHelper.getChildren(elem)) {
            if (child.getNodeName().equalsIgnoreCase("cr")) {
                String crNumber = child.getAttribute("no");
                retval.add(crNumber);
            }
        }
        return retval;
    }

    private int getTSNameFromDataFilePosition(List<List<String>> testData) {
        // try to findout 'JTAF.test.name' column. Value from this column
        // necessary to add to ts name.
        if (testData != null && testData.size() > 0) {
            List<String> firstLine = testData.get(0);
            int pos = 0;
            for (String firstLineItem : firstLine) {
                if (firstLineItem != null && firstLineItem.equalsIgnoreCase("JTAF.test.name")) {
                    return pos;
                } else {
                    pos++;
                }
            }
        }
        return -1;
    }

}
