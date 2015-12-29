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

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.test.digraph.Dependencies;

/**
 * This class represents a testsuite object
 */
public class TestSuite extends TestNamespace {
    private Dependencies dependencies;
    private Dependencies exclusions;
    private String testSuiteName;

    public TestSuite(String name) throws NameFormatException {
        super(name);
    }

    /**
     * Return the tests this testsuite depends upon
     *
     * @return
     */
    public Dependencies getDependencies() {
        return dependencies;
    }

    /**
     * Set the dependencies for this testsuite
     *
     * @param dp
     */
    public void setDependencies(Dependencies dp) {
        dependencies = dp;
    }

    public Dependencies getExclusions() {
        return exclusions;
    }

    public void setExclusions(Dependencies exc) {
        exclusions = exc;
    }

    /**
     * Return the name of the testsuite
     *
     * @return
     */
    public String getTestSuiteName() {
        return testSuiteName;
    }

    /**
     * Set the name of the testsuite
     *
     * @param tsn
     */
    public void setTestSuiteName(String tsn) {
        testSuiteName = tsn;
    }
}
