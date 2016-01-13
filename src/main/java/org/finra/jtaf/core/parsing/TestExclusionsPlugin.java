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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Parser plugin responsible for parsing exclusions in test script.
 */
public class TestExclusionsPlugin implements IPostParseTestPlugin {
    public static final String EXCLUSIONS_NAME = "exclusions";
    public static final String TEST_NAME = "test";
    public static final String TEST_SUITE_NAME = "testsuite";

    @Override
    public String getTagName() {
        return EXCLUSIONS_NAME;
    }

    @Override
    public void execute(PostTestParserPluginContext ctx) throws ParserPluginException {
        Set<String> exclusionSuites = new HashSet<String>();
        Set<String> exclusionTests = new HashSet<String>();

        NodeList testChildNodes = ctx.getRootNodeTest().getChildNodes();
        for (int testChildNodeIndex = 0; testChildNodeIndex < testChildNodes.getLength(); testChildNodeIndex++) {
            Node testChildNode = testChildNodes.item(testChildNodeIndex);
            if (testChildNode.getNodeName().equalsIgnoreCase(EXCLUSIONS_NAME)) {
                handleExclusionNode(testChildNode, exclusionSuites, exclusionTests);
            }
        }

        List<TestComponent> componentList = ctx.getTestSuite().getComponentList();
        TestScript lastTestScript = (TestScript) componentList.get(componentList.size() - 1);
        lastTestScript.setExclusions(exclusionSuites, exclusionTests);
    }

    private void handleExclusionNode(Node testChildNode, Set<String> exclusionSuites, Set<String> exclusionTests) {
        NodeList exclusionChildNodes = testChildNode.getChildNodes();
        for (int exclusionChildNodeIndex = 0; exclusionChildNodeIndex < exclusionChildNodes.getLength(); exclusionChildNodeIndex++) {
            Node exclusionChildNode = exclusionChildNodes.item(exclusionChildNodeIndex);
            Set<String> addSet = determineSet(exclusionChildNode.getNodeName(), exclusionSuites, exclusionTests);
            if (addSet == null)
                continue;
            String name = exclusionChildNode.getAttributes().getNamedItem("name").getTextContent();
            addSet.add(name);
        }
    }

    private Set<String> determineSet(String nodeName, Set<String> exclusionSuites, Set<String> exclusionTests) {
        if (nodeName.equalsIgnoreCase(TEST_SUITE_NAME)) {
            return exclusionSuites;
        } else if (nodeName.equalsIgnoreCase(TEST_NAME)) {
            return exclusionTests;
        }
        return null;
    }
}
