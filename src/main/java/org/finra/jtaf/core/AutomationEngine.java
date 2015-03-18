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
package org.finra.jtaf.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestNamespace;
import org.finra.jtaf.core.model.test.digraph.DiEdge;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.finra.jtaf.core.parsing.AutomationValueFilterPlugin;
import org.finra.jtaf.core.parsing.AutomationValuePlugin;
import org.finra.jtaf.core.parsing.CommandLibraryParser;
import org.finra.jtaf.core.parsing.DigraphPlugin;
import org.finra.jtaf.core.parsing.ExceptionPlugin;
import org.finra.jtaf.core.parsing.MaxThreadsPlugin;
import org.finra.jtaf.core.parsing.ScriptParser;
import org.finra.jtaf.core.parsing.SuiteDependenciesPlugin;
import org.finra.jtaf.core.parsing.SuiteExclusionsPlugin;
import org.finra.jtaf.core.parsing.TestStrategyParser;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.plugins.PluginManager;
import org.finra.jtaf.core.plugins.parsing.IPostParseAllPlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseStrategyElementPlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseSuitePlugin;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;
import org.finra.jtaf.core.plugins.parsing.PostAllParserPluginContext;
import org.finra.jtaf.core.traceability.TraceabilityMatrixPlugin;
import org.finra.jtaf.core.utilities.JTAFPropertyManager;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Singleton class that serves as the front end for the rest of the engine's
 * components. All of its subcomponents are instantiated from a Spring factory.
 */
public class AutomationEngine {
	private static AutomationEngine s_instance;

	public static final Logger logger = Logger
			.getLogger(AutomationEngine.class);
	public MessageCollector mc = new MessageCollector("Building model");
	private static final String FRAMEWORK_XML = "framework.xml";

	// FIXME: Rewrite this parser
	private final ScriptParser scriptParser;
	private final CommandLibraryParser commandlibParser;

	// FIXME: And rewrite this one while you're at it :-D
	private final TestStrategyParser testStrategyParser;

	private TestNamespace testRoot;
	private Interpreter interpreter;
	private PluginManager pluginManager;
	private TestAgenda testAgenda;
	private CommandRegistry commandRegistry;

	private TestDigraph digraph;

	private List<IPostParseAllPlugin> postParseAllPlugins;

	private AutomationEngine() {
		try {
			InputStream fi = getFrameworkFile();
			GenericApplicationContext ctx = new GenericApplicationContext();

			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
			xmlReader
					.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);

			xmlReader.loadBeanDefinitions(new InputSource(fi));

			ctx.refresh();

			this.pluginManager = (PluginManager) ctx.getBean("PluginManager");

			digraph = new TestDigraph(
					new ClassBasedEdgeFactory<DiNode, DiEdge>(DiEdge.class));
			commandlibParser = new CommandLibraryParser();
			scriptParser = new ScriptParser();
			scriptParser.setDigraph(digraph);
			commandlibParser
					.setAutomationClassLoader(new DefaultAutomationClassLoader());
			testStrategyParser = new TestStrategyParser();
			testStrategyParser.setDigraph(digraph);
			initPostParseStrategyElementPlugins();
			testRoot = null;

			this.interpreter = (Interpreter) ctx.getBean("Interpreter");

			this.interpreter.setCommandRunnerPlugins(pluginManager
					.getCommandRunnerPlugins());
			this.interpreter.setTestRunnerPlugins(pluginManager
					.getTestRunnerPlugins());
			this.interpreter.setTearDownPlugins(pluginManager.getTearDownPlugins());

			initPostParseAllPlugins();
			initPostParseSuitePlugins();
			initPostParseTestPlugins();

		} catch (Exception e) {
			// If something goes wrong here, we have a serious issue
			logger.fatal(e);
			throw new RuntimeException(e);
		}
	}

	private void initPostParseStrategyElementPlugins() {
		List<IPostParseStrategyElementPlugin> postParseStrategyElementPlugins = new ArrayList<IPostParseStrategyElementPlugin>();
		postParseStrategyElementPlugins.add(new AutomationValuePlugin());
		postParseStrategyElementPlugins.add(new MaxThreadsPlugin());
		List<IPostParseStrategyElementPlugin> postParseStrategyElementPluginsFromManager = pluginManager
				.getPostParseStrategyElementPlugins();
		if (postParseStrategyElementPluginsFromManager != null) {
			postParseStrategyElementPlugins
					.addAll(postParseStrategyElementPluginsFromManager);
		}
		testStrategyParser
				.setPostParseStrategyElementPlugins(postParseStrategyElementPlugins);
	}

	private void initPostParseAllPlugins() {
		postParseAllPlugins = new ArrayList<IPostParseAllPlugin>();
		postParseAllPlugins.add(new DigraphPlugin());
		postParseAllPlugins.add(new TraceabilityMatrixPlugin());
		List<IPostParseAllPlugin> postParseAllPluginsFromManager = pluginManager
				.getPostParseAllPlugins();
		if (postParseAllPluginsFromManager != null) {
			postParseAllPlugins.addAll(postParseAllPluginsFromManager);
		}
		postParseAllPlugins.add(new AutomationValueFilterPlugin());
	}

	private void initPostParseSuitePlugins() {
		List<IPostParseSuitePlugin> postParseSuitePlugins = new ArrayList<IPostParseSuitePlugin>();
		postParseSuitePlugins.add(new SuiteDependenciesPlugin());
		postParseSuitePlugins.add(new SuiteExclusionsPlugin());
		List<IPostParseSuitePlugin> postParseSuitePluginsFromManager = pluginManager
				.getPostParseSuitePlugins();
		if (postParseSuitePluginsFromManager != null) {
			postParseSuitePlugins.addAll(postParseSuitePluginsFromManager);
		}
		scriptParser.setPostParseSuitePlugins(postParseSuitePlugins);
	}

	private void initPostParseTestPlugins() {
		List<IPostParseTestPlugin> postParseTestPlugins = new ArrayList<IPostParseTestPlugin>();
		postParseTestPlugins.add(new ExceptionPlugin());
		List<IPostParseTestPlugin> postParseTestPluginsFromManager = pluginManager
				.getPostParseTestPlugins();
		if (postParseTestPluginsFromManager != null) {
			postParseTestPlugins.addAll(postParseTestPluginsFromManager);
		}
		scriptParser.setPostParseTestPlugins(postParseTestPlugins);

		List<IPostParseSuitePlugin> postParseSuitePlugins = new ArrayList<IPostParseSuitePlugin>();
		postParseSuitePlugins.add(new SuiteDependenciesPlugin());
		postParseSuitePlugins.add(new SuiteExclusionsPlugin());
		List<IPostParseSuitePlugin> postParseSuitePluginsFromManager = pluginManager
				.getPostParseSuitePlugins();
		if (postParseSuitePluginsFromManager != null) {
			postParseSuitePlugins.addAll(postParseSuitePluginsFromManager);
		}
		scriptParser.setPostParseSuitePlugins(postParseSuitePlugins);
	}

	private static InputStream getFrameworkFile() {
		InputStream is = AutomationEngine.class.getClassLoader()
				.getResourceAsStream(FRAMEWORK_XML);
		try {
			if (is == null) {
				is = new FileInputStream(new File(FRAMEWORK_XML));
			}

		} catch (IOException e) {
			throw new RuntimeException("Could not load :" + FRAMEWORK_XML, e);
		}
		return is;
	}

	/**
	 * Singleton accessor method.
	 * 
	 * @return
	 */
	public static final AutomationEngine getInstance() {
		if (AutomationEngine.s_instance == null) {
			AutomationEngine.s_instance = new AutomationEngine();
		}
		return AutomationEngine.s_instance;
	}

	/**
	 * Gets the test root.
	 * 
	 * @return
	 */
	public final TestNamespace getTestRoot() {
		return testRoot;
	}

	/**
	 * Builds test model from the specified test library folder and test scripts
	 * folder. By default it would also load the test libraries from the
	 * classpath. The test libraries are assumed to have extension
	 * *.commands.xml and expected to be under testlibrary folder/package
	 * 
	 * @param librarySource
	 * @param testSource
	 */
	public final void buildModel(File librarySource, File testSource) {
		System.out.println("Building model...");
		logger.info("Building model...");

		boolean isFailed = false;

		try {
			commandRegistry = new CommandRegistry();
			commandlibParser.parseCommandLibraries(commandRegistry,
					librarySource, mc);

		} catch (Exception e) {
			mc.error(e.getMessage());
			e.printStackTrace();
			isFailed = true;
		}

		
		interpreter.setCommandRegistry(commandRegistry);
		scriptParser.setCommandRegistry(commandRegistry);
		try {
			testRoot = scriptParser.handleTestSource(testSource, mc);

			// build agenda
			String fileLocation = JTAFPropertyManager.getInstance()
					.getProperty("strategy");
			buildTestAgenda(new File(fileLocation));
			// run post all parsing plugins

			if (postParseAllPlugins != null) {
				for (IPostParseAllPlugin p : postParseAllPlugins) {
					p.execute(new PostAllParserPluginContext(testAgenda,
							commandRegistry));
				}
			}
		} catch (Exception e) {
			mc.error(e.getMessage());
			logger.error(e.getMessage());
			isFailed = true;
		}

		if (isFailed) {
			mc.dump(logger);
			logger.fatal("Failed to build test model.  See logs for details");
			throw new RuntimeException(
					"Failed to build test model.  See logs for details");
		}

		logger.info("Building model... Done");
		System.out.println("Building model... Done");

	}

	private TestAgenda buildTestAgenda(File file) throws ParsingException,
			SAXException, IOException {
		// System.out.println("Building test agenda...");
		logger.info("Building test agenda...");
		testStrategyParser.parse(file);
		// TODO: add step to connect parsed object to test objects and
		// dependency graph
		testStrategyParser.getErrorCollector().dump(logger);
		testAgenda = testStrategyParser.getTestPlan();
		logger.info("Building test agenda... Done.");
		return testAgenda;
	}

	/**
	 * Gets the test agenda or the strategy object. 
	 * @return
	 */
	public TestAgenda getTestAgenda() {
		return testAgenda;
	}

	/**
	 * Gets the test interpreter.
	 * @return
	 */
	public final Interpreter getInterpreter() {

		return interpreter;
	}

	/**
	 * Gets the plugin manager
	 * @return
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * Gets the digraph of tests.
	 * @return
	 */
	public final TestDigraph getTestDigraph() {
		return digraph;
	}

	/**
	 * Gets the command registry
	 * @return
	 */
	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}

	
	/**
	 * Gets the script parser.
	 * @return
	 */
	public ScriptParser getScriptParser() {
		return scriptParser;
	}

}
