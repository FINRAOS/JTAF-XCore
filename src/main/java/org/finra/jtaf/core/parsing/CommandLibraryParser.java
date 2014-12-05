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
package org.finra.jtaf.core.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.CommandRegistry;
import org.finra.jtaf.core.IAutomationClassLoader;
import org.finra.jtaf.core.model.exceptions.NameCollisionException;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.invocationtarget.Function;
import org.finra.jtaf.core.model.invocationtarget.InvocationTarget;
import org.finra.jtaf.core.parsing.exceptions.ExceptionAccumulator;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.parsing.helpers.AttributeHelper;
import org.finra.jtaf.core.parsing.helpers.ParserHelper;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * The class is responsible for discovering and parsing commands.xml files from
 * the classpath and under the project folder
 * 
 */
public class CommandLibraryParser {
	private static final String READ_LIBRARY_WARN = "A suspected library %s' was not loaded due to %s";
	private StatementParser stmtParser;
	static Logger logger = Logger.getLogger(ScriptParser.class.getPackage()
			.getName());

	private final DocumentBuilder db;

	private IAutomationClassLoader automationClassLoader;

	public CommandLibraryParser() throws ParserConfigurationException {
		db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		stmtParser = new StatementParser();
	}

	public void setAutomationClassLoader(
			IAutomationClassLoader automationClassLoader) {
		this.automationClassLoader = automationClassLoader;
	}

	public CommandRegistry parseCommandLibraries(
			CommandRegistry commandRegistry, File additionalLibrarySource,
			MessageCollector mc) throws NameFormatException,
			NameCollisionException, SAXException, IOException,
			ParsingException, URISyntaxException {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("commandNames.txt");
			// process Core commands
			for (InvocationTarget t : handleCoreLibrarySource(mc)) {
				fileWriter.write(t.getName()
						+ System.getProperty("line.separator"));
				if (commandRegistry.containsInvocationTarget(t.getName()
						.toLowerCase())) {
					logger.warn("Oops! We have more then one command with same name ('"
							+ t.getName()
							+ "') (case insensitive)! Fix your test commands, please.");
					mc.push("Oops! We have more then one command with same name ('"
							+ t.getName()
							+ "') (case insensitive)! Fix your test commands, please.");
				}
				commandRegistry.registerInvocationTarget(t.getName()
						.toLowerCase(), t);
			}

			// process additional library source folder commands which are not
			// in the classpath
			for (InvocationTarget t : handleLibrarySource(
					additionalLibrarySource, mc)) {
				fileWriter.write(t.getName()
						+ System.getProperty("line.separator"));
				if (commandRegistry.containsInvocationTarget(t.getName()
						.toLowerCase())) {
					logger.warn("Oops! We have more then one command with same name ('"
							+ t.getName()
							+ "') (case insensitive)! Fix your test commands, please.");
					mc.push("Oops! We have more then one command with same name ('"
							+ t.getName()
							+ "') (case insensitive)! Fix your test commands, please.");
				}
				commandRegistry.registerInvocationTarget(t.getName()
						.toLowerCase(), t);
			}

			return commandRegistry;
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private List<InvocationTarget> handleCoreLibrarySource(MessageCollector mc)
			throws NameFormatException, NameCollisionException, SAXException,
			IOException, ParsingException, URISyntaxException {
		List<InvocationTarget> retval = new ArrayList<InvocationTarget>();

		Resource[] resources = getResources();

		for (Resource resource : resources) {
			logger.debug("Parsing library " + resource.getDescription());
			List<InvocationTarget> library = readLibrary(mc,
					resource.getInputStream(), resource.getDescription());
			if ((library != null) && (library.size() > 0)) {
				// try to read jars (maven or ant setup)
				retval.addAll(library);
			} else {
				logger.debug("Found no commands");
			}
		}
		return retval;
	}

	/**
	 * for all elements of java.class.path get a Collection of resources under
	 * testlibrary
	 * 
	 * @return the resources in the order they are found
	 */

	private Resource[] getResources() throws IOException, URISyntaxException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		ArrayList<Resource> result = new ArrayList<Resource>();

		// We want all commands.xml files in testlibrary folder anywhere in the
		// hierarchy in the classpath
		// There is no single regex to address our requirement. Hence we have
		// two regex.
		// The first one gets all entries where testLibrary is under root.
		// The second gets all
		Resource[] firstResultSet = resolver
				.getResources("classpath*:/testlibrary/**/*commands.xml");
		if (firstResultSet != null) {
			Collections.addAll(result, firstResultSet);
		}
		// This path does not address testlibrary being directly under the root.
		resolver = new PathMatchingResourcePatternResolver();
		Resource[] secondResultSet = resolver
				.getResources("classpath*:/**/testlibrary/**/*commands.xml");

		if (secondResultSet != null) {
			if (firstResultSet != null) {
				for (Resource secondResource : secondResultSet) {
					// check to see if it was already found.
					boolean found = false;
					for (Resource firstResource : firstResultSet) {
						if (firstResource.getDescription().equals(
								secondResource.getDescription())) {
							found=true;
							break;
						}
					}
					if(!found){
						result.add(secondResource);
					}
				}
			} else {
				Collections.addAll(result, secondResultSet);
			}
		}

		return result.toArray(new Resource[] {});
	}

	public final List<InvocationTarget> readLibrary(MessageCollector mc,
			InputStream is, String description) {

		List<InvocationTarget> retval = new ArrayList<InvocationTarget>();

		try {
			mc.push("In library '" + description + "'");
			Document d = db.parse(is);
			retval.addAll(processLibrary(d.getDocumentElement(), mc));

		} catch (MalformedURLException e) {
			logger.warn(String.format(READ_LIBRARY_WARN, description,
					"the url being malformed!"), e);
		} catch (IOException e) {
			logger.warn(String.format(READ_LIBRARY_WARN, description,
					"an io exception while reading it!"), e);
		} catch (SAXException e) {
			logger.warn(String.format(READ_LIBRARY_WARN, description,
					"a SAX exception!"), e);
		} catch (ParsingException e) {
			logger.warn(String.format(READ_LIBRARY_WARN, description,
					"a parsing exception!"), e);
		} finally {
			mc.pop();
		}
		return retval;
	}

	private final List<InvocationTarget> handleLibrarySource(File f,
			MessageCollector mc) throws NameFormatException,
			NameCollisionException, SAXException, IOException, ParsingException {

		List<InvocationTarget> retval = new ArrayList<InvocationTarget>();
		try {
			if (!f.exists()) {
				throw new FileNotFoundException(f.getAbsolutePath());
			}

			if (f.isFile() && f.getName().endsWith(".xml")) {
				mc.push("In library " + f.getAbsolutePath());

				try {
					Document d = db.parse(f);
					retval.addAll(processLibrary(d.getDocumentElement(), mc));
				} finally {
					mc.pop();
				}
			} else if (f.isDirectory()) {
				ExceptionAccumulator acc = new ExceptionAccumulator();
				for (File child : f.listFiles()) {
					try {
						retval.addAll(handleLibrarySource(child, mc));
					} catch (Throwable th) {
						mc.error(th.getMessage());
						acc.add(th);
					}
				}

				if (!acc.isEmpty()) {
					throw acc;
				}
			}
		} catch (FileNotFoundException e) {
			logger.warn("A suspected library file '" + f.getAbsolutePath()
					+ "' was not found!");
			logger.debug(e); // debug this to slim down the log files
		}
		return retval;
	}

	private final List<InvocationTarget> processLibrary(Element elem,
			MessageCollector mc) throws ParsingException {
		if (elem.getNodeName().equalsIgnoreCase("library")) {
			List<InvocationTarget> invocationTargets = new ArrayList<InvocationTarget>();
			ExceptionAccumulator acc = new ExceptionAccumulator();

			for (Element child : ParserHelper.getChildren(elem)) {
				try {
					invocationTargets.add(processInvocationTarget(child, mc));
				} catch (Throwable th) {
					mc.error(th.getMessage());
					acc.add(th);
				}
			}

			if (!acc.isEmpty()) {
				throw acc;
			}

			return invocationTargets;
		} else {
			throw new UnexpectedElementException(elem);
		}
	}

	// This is just called when processing the library.
	// It basically just gets what are designated as parameters there and stores
	// the names of them
	// in the invocation target param list
	private final InvocationTarget processInvocationTarget(Element elem,
			MessageCollector mc) throws ParsingException {
		InvocationTarget retval = null;
		final String name = elem.getNodeName().toLowerCase();
		if (name.equals("command")) {
			retval = processCommand(elem, mc);
		} else if (name.equals("function")) {
			retval = processFunction(elem, mc);
		} else {
			throw new UnexpectedElementException(elem);
		}

		final Element usage = ParserHelper.getOptionalElement(elem, "usage");
		if (usage != null) {
			retval.setUsage(usage.getTextContent());
		}

		final Element requiredParameters = ParserHelper.getOptionalElement(
				elem, "requiredParameters");
		if (requiredParameters != null) {
			for (Element child : ParserHelper.getChildren(requiredParameters)) {
				AttributeHelper ah = new AttributeHelper(child);
				retval.addRequiredParameter(ah.getRequiredString("name"));
			}
		}

		final Element optionalParameters = ParserHelper.getOptionalElement(
				elem, "optionalParameters");
		if (optionalParameters != null) {
			for (Element child : ParserHelper.getChildren(optionalParameters)) {
				AttributeHelper ah = new AttributeHelper(child);
				retval.addOptionalParameter(ah.getRequiredString("name"));
			}
		}

		final Element produces = ParserHelper.getOptionalElement(elem,
				"produces");
		if (produces != null) {
			for (Element child : ParserHelper.getChildren(produces)) {
				AttributeHelper ah = new AttributeHelper(child);
				retval.addProduction(ah.getRequiredString("name"));
			}
		}

		return retval;
	}

	private final Command processCommand(Element elem, MessageCollector mc)
			throws ParsingException {
		try {
			AttributeHelper ah = new AttributeHelper(elem);
			final String commandName = ah.getRequiredString("name");
			final String commandClass = ah.getRequiredString("class");

			Class<?> targetClass = automationClassLoader
					.loadClass(commandClass);
			if (Command.class.isAssignableFrom(targetClass)) {
				Constructor<?> constructor = targetClass
						.getConstructor(String.class);
				return (Command) constructor.newInstance(commandName);
			} else {
				throw new IllegalArgumentException(
						"All Commands must extend Command");
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private final Function processFunction(Element elem, MessageCollector mc)
			throws ParsingException {
		try {

			AttributeHelper ah = new AttributeHelper(elem);
			Function retval = new Function(ah.getRequiredString("name")
					.toLowerCase());

			retval.setBody(stmtParser.processStatementList(
					ParserHelper.getRequireElement(elem, "body"), mc));
			return retval;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

}
