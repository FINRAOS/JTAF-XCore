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

/**
 * This is the interface for tearDown plugins
 */
public interface ITearDownPlugin {
    /**
     * This method is called when a script fails and the tearDown is entered
     *
     * @param tearDownPluginContext
     * @throws RunnerPluginException
     */
    void handleBeforeTearDown(TearDownPluginContext tearDownPluginContext) throws RunnerPluginException;
}
