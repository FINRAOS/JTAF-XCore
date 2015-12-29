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
 * This class represents a single test step object i.e. a command in the test
 */
public class TestStepsDetails {
    String name;
    String usage;
    boolean expectedResult = true;
    boolean actualResult = true;
    String type = "general";

    public TestStepsDetails(String name, String usage) {
        this.name = name;
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(boolean expectedResult) {
        this.expectedResult = expectedResult;
    }

    public boolean getActualResult() {
        return actualResult;
    }

    public void setActualResult(boolean actualResult) {
        this.actualResult = actualResult;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
