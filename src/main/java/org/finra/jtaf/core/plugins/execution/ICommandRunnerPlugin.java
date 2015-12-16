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


/**
 * This is the interface to be used for Command Execution plugins
 */
public interface ICommandRunnerPlugin {

    /**
     * Called before every command is run
     *
     * @param ctx
     * @throws RunnerPluginException
     */
    void handleCommandBefore(CommandRunnerPluginContext ctx) throws RunnerPluginException;

    /**
     * Called after every command is finished running
     *
     * @param ctx
     * @throws RunnerPluginException
     */
    void handleCommandAfter(CommandRunnerPluginContext ctx) throws RunnerPluginException;
}
