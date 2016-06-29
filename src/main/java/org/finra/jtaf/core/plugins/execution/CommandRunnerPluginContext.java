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
package org.finra.jtaf.core.plugins.execution;

import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.test.TestStepsDetails;

/**
 * This is the context visible to Command Runner Plugins
 */
public class CommandRunnerPluginContext {

	private TestStepsDetails testStepsDetails;
	private IInvocationContext context;

	public CommandRunnerPluginContext(TestStepsDetails ts, IInvocationContext ctx){
		this.context = ctx;
		this.testStepsDetails = ts;
	}
	
	/***
	 * @return IInovocationContext - holds a map of values over the life of a Test Script
	 */
	public IInvocationContext getIInvocationContext() {
		return context;
	}

	/***
	 * @return TestStepsDetails - contains some metadata concerning the command
	 */
	public TestStepsDetails getTestStepsDetails() {
		return testStepsDetails;
	}

	
}
