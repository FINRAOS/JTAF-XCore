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
package org.finra.jtaf.core.parallel;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * The test case runner class
 *
 */
public class TestCaseRunner extends BlockJUnit4ClassRunner {
	
	private ParallelScriptRunner scriptWrapper;

	public TestCaseRunner(Class<?> klass, ParallelScriptRunner scriptWrapper)
			throws InitializationError {
		super(klass);
		this.scriptWrapper = scriptWrapper;
	}

	protected Object createTest() throws Exception {
		return scriptWrapper;
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {

		return Description.createSuiteDescription(scriptWrapper.getName());

	}

	
	public Description getDescription() {
		return Description.createSuiteDescription(scriptWrapper.getName());

	}

	@Override
	protected void collectInitializationErrors(List<Throwable> errors) {
	}

}
