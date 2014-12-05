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

import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.plugins.parsing.IPostParseAllPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostAllParserPluginContext;


/**
 * Core plugin that filters testscripts to run based on the automation value provided in the strategy file
 *
 */
public class AutomationValueFilterPlugin implements IPostParseAllPlugin
{
	@Override
	public void execute(PostAllParserPluginContext ctx) throws ParserPluginException
	{
		TestAgenda testAgenda = ctx.getTestAgenda();
		if(testAgenda.isAutomationValuesEmpty())
			return;
		
		List<TestScript> testScripts = ctx.getTestAgenda().getTestScripts();
		List<TestScript> testsToRemove = new ArrayList<TestScript>();
		for(TestScript testScript : testScripts)
		{
			String automationValue = testScript.getAutomationValue();
			if(!testAgenda.containsAutomationValue(automationValue))
				testsToRemove.add(testScript);
		}
		
		testScripts.removeAll(testsToRemove);
	}
}
