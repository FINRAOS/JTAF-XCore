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

import javax.xml.parsers.ParserConfigurationException;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestScriptCollector;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.finra.jtaf.core.parsing.exceptions.MissingAttributeException;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.parsing.helpers.AttributeHelper;
import org.finra.jtaf.core.plugins.parsing.IPostParseStrategyElementPlugin;
import org.finra.jtaf.core.plugins.parsing.PostStrategyElementParserPluginContext;
import org.w3c.dom.Element;


/**
 * Parser for TestAgenda or Strategy xml file. 
 * 
 */
public class TestStrategyParser extends BaseParser {
	private static final String EXECUTE = "execute";

	private static final String TARGET = "target";
	
	private static final List<TestScript> EMPTY_LIST = new ArrayList<TestScript>();
	
	private TestAgenda testPlan;

	private List<IPostParseStrategyElementPlugin> postParseStrategyElementPlugins;
	
	public TestStrategyParser() throws ParserConfigurationException {
		testPlan = null;
	}

	public final TestAgenda getTestPlan() {
		return testPlan;
	}

	public void setDigraph(TestDigraph digraph){
	}
	@Override
	protected final void handleRoot(Element root) throws ParsingException {
		testPlan = new TestAgenda();

		if (root.getNodeName().equalsIgnoreCase(EXECUTE)) {
			handleExecute(root);
		} else {
			reportUnexpectedElement(root);
			throw new UnexpectedElementException(root);
		}
	}

	protected final void handleExecute(Element elem) {
		try {
			getErrorCollector().push("In " + EXECUTE + "block");
			for (Element child : getChildElements(elem)) {
				if(child.getNodeName().equalsIgnoreCase(TARGET) /*|| child.getNodeName().equalsIgnoreCase(UNION)*/)
				{
					getTestPlan().getTestScripts().addAll(handleTarget(child));
				}
				for(IPostParseStrategyElementPlugin postParseStrategyElementPlugin : postParseStrategyElementPlugins)
				{
					postParseStrategyElementPlugin.execute(new PostStrategyElementParserPluginContext(testPlan, child));
				}
			}
		} catch (Exception e) {
			getErrorCollector().error(e.getMessage());
		} finally {
			getErrorCollector().pop();
		}
	}

	protected List<TestScript> handleTarget(Element elem) throws MissingAttributeException{
		AttributeHelper attrs = new AttributeHelper(elem);
		final String targetName = attrs.getRequiredString("name");

		try {
			getErrorCollector().push("In target " + targetName);
			// TODO: pass in TestComponents rather than depending on AutomationEngine
			List<TestComponent> targets = AutomationEngine.getInstance().getTestRoot().toComponent(targetName);

			if (targets == null || targets.size() == 0) {
				getErrorCollector().error("Target does not exist");
				return EMPTY_LIST;
			} else {
				TestScriptCollector tsc = new TestScriptCollector();
				for (TestComponent target : targets) {
					target.acceptTestVisitor(tsc);	
				}
				return tsc.getTestScripts();
			}
		} catch (Exception e) {
			getErrorCollector().error(e.getMessage());
			return EMPTY_LIST;
		} 
		finally {
			getErrorCollector().pop();
		}
	}
	
	public void setPostParseStrategyElementPlugins(List<IPostParseStrategyElementPlugin> postParseStrategyElementPlugins)
	{
		this.postParseStrategyElementPlugins = postParseStrategyElementPlugins;
	}
}