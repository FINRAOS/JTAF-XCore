/*
 * (C) Copyright 2015 Java Test Automation Framework Contributors.
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
import org.finra.jtaf.core.model.test.TestScript;

/**
 * This is the context visible to tearDown plugins
 *
 */
public class TearDownPluginContext {
	private TestScript testScript;
	private Throwable failureReason;
	private IInvocationContext invocationContext;
	
	public TearDownPluginContext(TestScript testScript, Throwable failureReason, IInvocationContext invocationContext) {
		this.testScript = testScript;
		this.failureReason = failureReason;
		this.invocationContext = invocationContext;
	}
	
	public TestScript getTestScript() {
		return testScript;
	}
	
	public Throwable getFailureReason() {
		return failureReason;
	}
	
	public IInvocationContext getInvocationContext() {
		return invocationContext;
	}
}
