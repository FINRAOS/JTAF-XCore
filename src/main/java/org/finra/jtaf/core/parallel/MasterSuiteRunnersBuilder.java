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
package org.finra.jtaf.core.parallel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.xml.sax.SAXException;


/**
 * Builds the list of runners for the tests in Test Agenda
 *
 */
public class MasterSuiteRunnersBuilder extends RunnerBuilder {
	
	public MasterSuiteRunnersBuilder() throws IllegalAccessException
	{
		
	}
	
	public List<Runner> runners()
			throws InitializationError, ParsingException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		
		
		AutomationEngine engine = AutomationEngine.getInstance();
		ArrayList<Runner> tests = new ArrayList<Runner>();
		
		engine.buildModel(new File("testlibrary"), new File("testscripts"));
		TestAgenda testAgenda = engine.getTestAgenda();

		for (TestScript testScript : testAgenda.getTestScripts()) {
			TestCaseRunner theRunner = new TestCaseRunner(ParallelScriptRunner.class, new ParallelScriptRunner(testScript));
			tests.add(theRunner);
			ConcurrentScheduler.registerTestName(testScript.getName());
		}
		
		return tests;
	}

	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
