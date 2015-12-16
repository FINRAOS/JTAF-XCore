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
package org.finra.jtaf.core.model.test.digraph;

import org.finra.jtaf.core.model.test.TestScript;

/**
 * This class represents a vertex in the digraph
 */
public class DiNode {
    private final String testName; //This shouldnt change after being set initially
    private String testStatus = "";
    private final TestScript testScript; //This shouldnt change after being set initially

    public DiNode(TestScript ts) {
        this.testName = ts.getName();
        this.testScript = ts;
    }

    public String getTestStatus() {
        return testStatus;
    }

    protected void setTestStatus(String newTestStatus) {
        testStatus = newTestStatus;
    }

    public String getTestName() {
        return testName;
    }

    public TestScript getTestScript() {
        return testScript;
    }

    @Override
    public int hashCode() {
        return this.getTestName().hashCode()
                + this.getTestScript().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DiNode) {
            if (this.getTestName().equalsIgnoreCase(((DiNode) o).getTestName())
                    && this.getTestScript().equals(((DiNode) o).getTestScript())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}