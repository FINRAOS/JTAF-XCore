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

import java.util.Set;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.parsing.exceptions.ExceptionAccumulator;
import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.parsing.helpers.AttributeHelper;
import org.finra.jtaf.core.parsing.helpers.ParserHelper;
import org.finra.jtaf.core.plugins.parsing.IPostParseSuitePlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostSuiteParserPluginContext;
import org.w3c.dom.Element;

/**
 * Parser plugin to handle exclusions defined in a test suite.
 *
 */
public class SuiteExclusionsPlugin implements IPostParseSuitePlugin {
	private static final String EXCLUSIONS_NAME = "exclusions";
	private static Logger logger = Logger.getLogger(SuiteExclusionsPlugin.class.getPackage().getName());
	
	@Override
	public void execute(PostSuiteParserPluginContext ctx) throws ParserPluginException {
		ExceptionAccumulator acc = new ExceptionAccumulator();
		Set<String> exclusionTests = ctx.getTestSuite().getExclusions().getDependenciesTests();
		Set<String> exclusionTestSuites = ctx.getTestSuite().getExclusions().getDependenciesSuites();
		Element exclusionsElement = ParserHelper.getFirstChildElementCaseInsensitive((Element) ctx.getRootNodeSuite(), EXCLUSIONS_NAME);
		for (Element e : ParserHelper.getChildren(exclusionsElement)) {
			try {
				AttributeHelper excAttributeHelper = new AttributeHelper(e);
				String exclusionValue = excAttributeHelper.getRequiredString("name");
				if (e.getNodeName().equalsIgnoreCase("test")) {
					exclusionTests.add(exclusionValue);
				}
				else if (e.getNodeName().equalsIgnoreCase("testsuite")) {
					exclusionTestSuites.add(exclusionValue);
				}
				else {
					throw new UnexpectedElementException(e);
				}
			}
			catch (Throwable th) {
				logger.fatal(th.getMessage());
//				mc.error(th.getMessage());
				acc.add(th);
			}
		}
		if (!acc.isEmpty()) {
			throw new ParserPluginException(acc);
		}
	}
	
	@Override
	public String getTagName() {
		return EXCLUSIONS_NAME;
	}
}
