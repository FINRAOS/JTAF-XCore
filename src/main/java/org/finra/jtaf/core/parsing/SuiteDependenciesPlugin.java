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
 * Parser plugin to parse test suite dependencies
 */
public class SuiteDependenciesPlugin implements IPostParseSuitePlugin {
    private static final String DEPENDENCIES_NAME = "dependencies";
    private static Logger logger = Logger.getLogger(SuiteDependenciesPlugin.class.getPackage().getName());

    @Override
    public void execute(PostSuiteParserPluginContext ctx) throws ParserPluginException {
        ExceptionAccumulator acc = new ExceptionAccumulator();
        Set<String> dependentTests = ctx.getTestSuite().getDependencies().getDependenciesTests();
        Set<String> dependentTestSuites = ctx.getTestSuite().getDependencies().getDependenciesSuites();
        Element dependenciesElement = ParserHelper.getFirstChildElementCaseInsensitive((Element) ctx.getRootNodeSuite(), DEPENDENCIES_NAME);
        for (Element e : ParserHelper.getChildren(dependenciesElement)) {
            try {
                AttributeHelper depAttributeHelper = new AttributeHelper(e);
                String dependentValue = depAttributeHelper.getRequiredString("name");
                if (e.getNodeName().equalsIgnoreCase("test")) {
                    dependentTests.add(dependentValue);
                } else if (e.getNodeName().equalsIgnoreCase("testsuite")) {
                    dependentTestSuites.add(dependentValue);
                } else {
                    throw new UnexpectedElementException(e);
                }
            } catch (Throwable th) {
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
        return DEPENDENCIES_NAME;
    }
}
