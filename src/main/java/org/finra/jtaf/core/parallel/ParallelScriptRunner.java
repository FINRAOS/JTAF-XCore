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

import org.junit.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.test.TestResult;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.utilities.StringHelper;
import org.junit.Test;


/**
 * Allows more than one script to run in parallel
 */
public class ParallelScriptRunner extends TestCase {

    private TestScript theTestScript;
    boolean expectedFailureFlag = false;
    public static final Logger logger = Logger.getLogger(ParallelScriptRunner.class);

    public ParallelScriptRunner(TestScript theTestScript) {
        super(createJUnitName(theTestScript));
        this.theTestScript = theTestScript;
    }

    private static final String createJUnitName(TestScript test) {
        StringBuilder sb = new StringBuilder();
        sb.append(test.getFullName());
        boolean issuesExist = false;
        if (test.getCRs() != null && test.getCRs().size() > 0) {
            sb.append(" CR=\"");
            sb.append(StringHelper.join(test.getCRs(), ", "));

            sb.append("\"");

            issuesExist = true;
        }

        if (test.getIssue() != null && !test.getIssue().equals("")) {
            issuesExist = true;
        }

        if (test.getStatus() != null) {
            sb.append(" STATUS=\"");
            sb.append(test.getStatus());
            sb.append("\"");
        }

        if (issuesExist) {
            sb.append(" [ISSUE EXISTS]");
        }

        return sb.toString();
    }

    @Test
    public void runJtafTestScript() throws Throwable {

        logger.info("Thread " + Thread.currentThread().getId() + " is executing " + this.getName());
        DiNode theTest = AutomationEngine.getInstance().getTestDigraph().getVertex(this.theTestScript.getName());
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        try {
            if (theTest.getTestStatus().equalsIgnoreCase("FAILED")) {
                ConcurrentScheduler.updateWithStatus(new ResultUpdate(this.theTestScript.getName(), "FAILED"));
                Assert.fail("One or more Dependent tests failed");
            }
            TestResult tr = iv.interpret(this.theTestScript);
            if (!tr.isTestPassed()) {
                ConcurrentScheduler.updateWithStatus(new ResultUpdate(this.theTestScript.getName(), "FAILED"));
                throw tr.getFailureReason();
            }
            ConcurrentScheduler.updateWithStatus(new ResultUpdate(this.theTestScript.getName(), "PASSED"));
        } catch (Throwable t) {
            throw t;
        } finally {

        }


    }
}
