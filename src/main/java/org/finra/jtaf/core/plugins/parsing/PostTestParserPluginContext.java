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

package org.finra.jtaf.core.plugins.parsing;

import org.finra.jtaf.core.CommandRegistry;
import org.finra.jtaf.core.model.test.TestSuite;
import org.w3c.dom.Node;

/***
 * Exposes the data that can be used and/or modified by a IPostParseTestPlugin.
 * 
 */
public class PostTestParserPluginContext {

	private CommandRegistry commandRegistry;
	private TestSuite testSuite;
	private Node testRoot;

	public PostTestParserPluginContext(CommandRegistry commandRegistry,
			TestSuite testSuite, Node testRoot) {
		this.commandRegistry = commandRegistry;
		this.testSuite = testSuite;
		this.testRoot = testRoot;
	}

	/***
	 * Returns the Command Library model, so plugins can use the data
	 * 
	 * @return commandModel
	 */
	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}

	/***
	 * Returns the Test Suite the test is a part of, so plugins can add test
	 * scripts to the suite if needed
	 * 
	 * @return testSuite
	 */
	public TestSuite getTestSuite() {
		return testSuite;
	}

	/***
	 * Returns the root Node object of the current test so that the relevant xml
	 * data can be used in the plugin
	 * 
	 * @return elementRoot
	 */
	public Node getRootNodeTest() {
		return testRoot;
	}
}
