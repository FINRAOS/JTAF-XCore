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
package org.finra.jtaf.core.plugins;

import java.util.List;

import org.finra.jtaf.core.plugins.execution.ICommandRunnerPlugin;
import org.finra.jtaf.core.plugins.execution.ITestRunnerPlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseAllPlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseStrategyElementPlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseSuitePlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;

/**
 * Plugin Manager is a class to register the parsing and the execution level
 * plugins. Any number of custom plugins can be registered by addin them as a
 * property of the Pluginmanager bean in framework.xml
 */
public class PluginManager {

	private List<IPostParseAllPlugin> postParseAllPlugins;
	private List<IPostParseSuitePlugin> postParseSuitePlugins;
	private List<IPostParseTestPlugin> postParseTestPlugins;
	private List<IPostParseStrategyElementPlugin> postParseStrategyElementPlugins;

	private List<ITestRunnerPlugin> testRunnerPlugins;
	private List<ICommandRunnerPlugin> commandRunnerPlugins;

	public void setPostParseAllPlugins(List<IPostParseAllPlugin> plugins) {
		postParseAllPlugins = plugins;
	}

	public List<IPostParseAllPlugin> getPostParseAllPlugins() {
		return postParseAllPlugins;
	}

	public void setPostParseSuitePlugins(
			List<IPostParseSuitePlugin> customPlugins) {
		postParseSuitePlugins = customPlugins;
	}

	public List<IPostParseSuitePlugin> getPostParseSuitePlugins() {
		return postParseSuitePlugins;
	}

	public void setPostParseTestPlugins(List<IPostParseTestPlugin> plugins) {
		postParseTestPlugins = plugins;
	}

	public List<IPostParseTestPlugin> getPostParseTestPlugins() {
		return postParseTestPlugins;
	}

	public void setPostParseStrategyElementPlugins(
			List<IPostParseStrategyElementPlugin> plugins) {
		postParseStrategyElementPlugins = plugins;
	}

	public List<IPostParseStrategyElementPlugin> getPostParseStrategyElementPlugins() {
		return postParseStrategyElementPlugins;
	}

	public void setTestRunnerPlugins(List<ITestRunnerPlugin> customPlugins) {
		this.testRunnerPlugins = customPlugins;
	}

	public List<ITestRunnerPlugin> getTestRunnerPlugins() {
		return testRunnerPlugins;
	}

	public void setCommandRunnerPlugins(List<ICommandRunnerPlugin> customPlugins) {
		this.commandRunnerPlugins = customPlugins;
	}

	public List<ICommandRunnerPlugin> getCommandRunnerPlugins() {
		return commandRunnerPlugins;
	}

}
