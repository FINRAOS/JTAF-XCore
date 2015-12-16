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

import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.exceptions.NameCollisionException;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser plugin meant to handle the loop element in the script that determines
 * the number of times a test script needs to be run.
 */
public class LoopPlugin implements IPostParseTestPlugin {
    protected TestDigraph testDigraph;

    @Override
    public String getTagName() {
        return "loop";
    }

    public LoopPlugin(AutomationEngine automationEngine) {
        this.testDigraph = automationEngine.getTestDigraph();
    }

    @Override
    public void execute(PostTestParserPluginContext ctx)
            throws ParserPluginException {
        int iterationCount = getIterationCount(ctx.getRootNodeTest());
        if (iterationCount != 1) {
            TestScript oldTestScript = getOldTestScript(ctx.getTestSuite());
            List<TestScript> iteratedScripts = createIteratedTestScripts(ctx,
                    oldTestScript, iterationCount);
            removeOldTestScript(ctx.getTestSuite());
            addIteratedTestScripts(ctx.getTestSuite(), iteratedScripts);
        }
    }

    private int getIterationCount(Node rootNodeTest) {
        int result = 1;
        NodeList children = rootNodeTest.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equalsIgnoreCase("loop")) {
                result = Integer.parseInt(children.item(i).getAttributes()
                        .getNamedItem("iterations").getNodeValue());
            }
        }
        return result;
    }

    private TestScript getOldTestScript(TestSuite testSuite) {
        List<TestComponent> testComponents = testSuite.getComponentList();
        return (TestScript) testComponents.get(testComponents.size() - 1);
    }

    List<TestScript> createIteratedTestScripts(PostTestParserPluginContext ctx,
                                               TestScript oldTestScript, int iterationCount)
            throws ParserPluginException {
        List<TestScript> result = new ArrayList<TestScript>();
        for (int currentIteration = 1; currentIteration <= iterationCount; currentIteration++) {
            TestScript newTestScript;
            ScriptParser sp = AutomationEngine.getInstance().getScriptParser();
            try {
                newTestScript = sp
                        .processTestScript((Element) ctx.getRootNodeTest(),
                                new MessageCollector());
            } catch (ParsingException parsingException) {
                throw new ParserPluginException("Problem re-parsing test",
                        parsingException);
            }
            newTestScript.setName(newTestScript.getName() + " [iteration "
                    + currentIteration + " of " + iterationCount + "]");
            result.add(newTestScript);
        }
        return result;
    }

    private void removeOldTestScript(TestSuite testSuite) {
        List<TestComponent> testComponents = testSuite.getComponentList();
        testComponents.remove(testComponents.size() - 1);
    }

    private void addIteratedTestScripts(TestSuite testSuite,
                                        List<TestScript> iteratedScripts) throws ParserPluginException {
        try {
            for (TestScript iteratedScript : iteratedScripts) {
                testSuite.add(iteratedScript);
                // DigraphPlugin dependenciesPlugin =
                // DigraphPlugin.getInstance();
                // TestDigraph testDigraph =
                // dependenciesPlugin.getTestDigraph();
                testDigraph.addVertex(new DiNode(iteratedScript));
            }
        } catch (NameCollisionException nameCollisionException) {
            throw new ParserPluginException("Duplicate test name: ",
                    nameCollisionException);
        }
    }
}
