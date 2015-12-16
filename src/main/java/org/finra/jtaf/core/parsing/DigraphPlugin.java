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

import java.util.LinkedHashSet;
import java.util.Set;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestNamespace;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.model.test.digraph.DigraphFactory;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.finra.jtaf.core.plugins.parsing.IPostParseAllPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostAllParserPluginContext;
import org.finra.jtaf.core.utilities.logging.MessageCollector;

/**
 * Parser plugin that runs post all. Scans through the test scripts to create a Digraph as per denpencies.
 */
public class DigraphPlugin implements IPostParseAllPlugin {
    protected TestNamespace testNamespace;

    @Override
    public void execute(PostAllParserPluginContext ctx) throws ParserPluginException {
        TestDigraph testDigraph = AutomationEngine.getInstance().getTestDigraph();
        TestAgenda testAgenda = ctx.getTestAgenda();
        testNamespace = AutomationEngine.getInstance().getTestRoot();
        DigraphFactory graphFactory = new DigraphFactory(testDigraph, new MessageCollector());
        graphFactory.createGraph(testNamespace);

        Set<TestScript> dependentTests = new LinkedHashSet<TestScript>();
        for (TestScript t : testAgenda.getTestScripts())
            if (testAgenda.containsAutomationValue(t.getAutomationValue()))
                dependentTests.addAll(addDependentTests(testDigraph, (TestScript) t));

        for (TestScript t : dependentTests)
            if (!testAgenda.getTestScripts().contains(t))
                testAgenda.getTestScripts().add(t);
    }

    private Set<TestScript> addDependentTests(TestDigraph testDigraph, TestScript test) {
        Set<TestScript> additionalScript = new LinkedHashSet<TestScript>();
        for (DiNode d : testDigraph.getAllDependencies(test.getName())) {
            d.getTestScript().setAutomationValue(test.getAutomationValue());
            additionalScript.add(d.getTestScript());
            additionalScript.addAll(addDependentTests(testDigraph, d.getTestScript()));
        }
        return additionalScript;
    }
}
