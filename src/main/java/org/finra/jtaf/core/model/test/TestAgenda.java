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
package org.finra.jtaf.core.model.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Maintains a list of TestScripts that have been executed and TestScripts that still need to be executed
 * 
 */
public class TestAgenda {
	private final List<TestScript> testScripts;

	private Set<String> automationValues;
	private int maxThreads = 1;

	public TestAgenda() {
		testScripts = new ArrayList<TestScript>();
		automationValues = new HashSet<String>();
	}

	public List<TestScript> getTestScripts() {
		return testScripts;
	}

	/**
	 * This is a set operation and values are converted to lowercase first
	 * 
	 * @param automationValue
	 * @return 
	 */
	public boolean addAutomationValue(String automationValue) {
		return this.automationValues.add(automationValue.toLowerCase());
	}
	
	/**
	 * returns a read only version of the set of automation values for the test agenda
	 * 
	 * @return read only set of automation values
	 */
	public Set<String> getAutomationValues() {
		Set<String> setToWrap = automationValues == null ? (new HashSet<String>()) : automationValues;
		return Collections.unmodifiableSet(setToWrap);
	}
	
	/**
	 * Checks whether the automationValues set contains the given value.
	 * Ensures correct case sensitivity.
	 * 
	 * @param value to check for
	 * @return true if value exists, case insensitive
	 */
	public boolean containsAutomationValue(String value) {
		return value != null && (automationValues == null ? false : automationValues.contains(value.toLowerCase()));
	}
	
	/**
	 * pass through of is empty for the automationValues set .
	 * 
	 * @return true if null or empty
	 */
	public boolean isAutomationValuesEmpty() {
		return automationValues == null ? true : automationValues.isEmpty();
	}

	public void setThreadCount(String threads) {
		maxThreads = Integer.parseInt(threads);
	}
	
	public int getThreadCount() {
		return maxThreads;
	}
}