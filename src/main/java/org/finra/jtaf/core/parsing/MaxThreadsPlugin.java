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

import org.finra.jtaf.core.plugins.parsing.IPostParseStrategyElementPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostStrategyElementParserPluginContext;
import org.w3c.dom.Element;


/**
 * Strategy parser plugin that parses maximum parallel threads in strategy.
 */
public class MaxThreadsPlugin implements IPostParseStrategyElementPlugin {
	private static final String MAX_THREADS_NAME = "maxThreads";
	
	@Override
	public void execute(PostStrategyElementParserPluginContext ctx) throws ParserPluginException {
		Element element = ctx.getElement();
		if (element.getNodeName().equalsIgnoreCase(MAX_THREADS_NAME)) {
			ctx.getTestAgenda().setThreadCount(element.getTextContent());
		}
	}
}
