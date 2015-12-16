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
import org.finra.jtaf.core.model.test.TestResult;
import org.finra.jtaf.core.model.test.TestScript;

/**
 * This is the context visible to Test Runner Plugins
 */
public class TestRunnerPluginContext {
    private TestScript testScript;
    private TestResult testResult;
    private IInvocationContext invocationContext;

    public TestRunnerPluginContext(TestScript ts, TestResult tr, IInvocationContext ctx) {
        this.testScript = ts;
        this.testResult = tr;
        this.invocationContext = ctx;
    }

    /***
     * @return TestScript
     */
    public TestScript getTestScript() {
        return testScript;
    }

    /***
     * @return TestResult of the Test Script
     */
    public TestResult getTestResult() {
        return testResult;
    }

    /***
     * @return IInovocationContext - holds a map of values over the life of the Test Script
     */
    public IInvocationContext getIInvocationContext() {
        return invocationContext;
    }

}
