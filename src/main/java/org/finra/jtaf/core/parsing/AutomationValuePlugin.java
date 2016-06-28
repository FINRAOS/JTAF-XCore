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
 * Plugin that runs post parse of the strategy. Takes care of parsing the automation value in the agenda.
 */
public class AutomationValuePlugin implements IPostParseStrategyElementPlugin {
	private static final String AUTOMATION_VALUE_NAME = "automationValue";
	
	@Override
	public void execute(PostStrategyElementParserPluginContext ctx) throws ParserPluginException {
		Element element = ctx.getElement();
		if (element.getNodeName().equalsIgnoreCase(AUTOMATION_VALUE_NAME)) {
			ctx.getTestAgenda().addAutomationValue(element.getTextContent());
		}
	}
}
