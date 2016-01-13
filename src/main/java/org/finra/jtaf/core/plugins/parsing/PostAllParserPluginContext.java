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

package org.finra.jtaf.core.plugins.parsing;

import org.finra.jtaf.core.CommandRegistry;
import org.finra.jtaf.core.model.test.TestAgenda;


/***
 * Exposes the data that can be used and/or modified by a IPostParseTestPlugin.
 */
public class PostAllParserPluginContext {

    private TestAgenda testAgenda;
    private CommandRegistry commandRegistry;

    public PostAllParserPluginContext(TestAgenda testAgenda, CommandRegistry commandRegistry) {
        this.testAgenda = testAgenda;
        this.commandRegistry = commandRegistry;
    }

    /***
     * Returns the Test Agenda associated with the test run, so plugins can read and modify the data
     *
     * @return testAgenda
     */
    public TestAgenda getTestAgenda() {
        return testAgenda;
    }

    /***
     * Returns the Command Library model, so plugins can use the data
     *
     * @return commandModel
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

}
