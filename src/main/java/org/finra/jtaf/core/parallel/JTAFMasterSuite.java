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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.xml.sax.SAXException;


/**
 * The test suite of the tests that will run together
 *
 */
public class JTAFMasterSuite extends Suite{

	public JTAFMasterSuite(Class<?> klass, RunnerBuilder builder)
			throws InitializationError, ParsingException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		super(klass, (new MasterSuiteRunnersBuilder().runners()));
		setScheduler(new ConcurrentScheduler());

	}

	@Override
	public Description getDescription() {
		Description description = Description.createSuiteDescription("Automated Tests");
		for (Runner child : getChildren()) {
			description.addChild(describeChild(child));
		}
		return description;
	}

}
