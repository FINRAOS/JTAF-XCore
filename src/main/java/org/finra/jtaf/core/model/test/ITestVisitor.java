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

/**
 * Visitor for different test elements
 *
 */
public interface ITestVisitor {

	/**
	 * Operates on a folder level
	 * @param testSuite
	 * @throws Exception
	 */
	void visitTestNamespace(TestNamespace suite) throws Exception;
	
	
	/**
	 * Operates on a Test element
	 * @param test
	 * @throws Exception
	 */
	void visitTestScript(TestScript test) throws Exception;
	
	/**
	 * Operates on a file level
	 * @param testSuite
	 */
	void visitTestSuite(TestSuite testSuite) throws Exception;
}
