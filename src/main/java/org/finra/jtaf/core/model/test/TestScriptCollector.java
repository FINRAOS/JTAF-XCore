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
package org.finra.jtaf.core.model.test;

import java.util.ArrayList;
import java.util.List;


/**
 * Crawls the TestComponent model to discover TestScript elements
 *
 */
public class TestScriptCollector implements ITestVisitor {

	private final List<TestScript> testScripts;
	
	public TestScriptCollector() {
		testScripts = new ArrayList<TestScript>();
	}
	
	/**
	 * @return A copy of the list of testScripts
	 */
	public final List<TestScript> getTestScripts() {
		return new ArrayList<TestScript>(testScripts);
	}
	
	public final void addToTestScripts(TestScript c){
		this.testScripts.add(c);
	}
	public void visitTestNamespace(TestNamespace ns) throws Exception {
		for(TestComponent c : ns) {
			c.acceptTestVisitor(this);
		}
	}

	public void visitTestScript(TestScript test) throws Exception {
		this.testScripts.add(test);
	}

	@Override
	public void visitTestSuite(TestSuite testSuite) throws Exception {
		for(TestComponent c : testSuite) {
			c.acceptTestVisitor(this);
		}
	}
}
