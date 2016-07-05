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
package org.finra.jtaf.core.model.test.digraph;

import java.util.HashSet;
import java.util.Set;

/**
 * Set of Test Case and Test Suite Dependencies
 *
 */
public class Dependencies {

	private Set<String> dependentSuites = new HashSet<String>();
	private Set<String> dependentTests = new HashSet<String>();
	
	public Dependencies(Set<String> suite, Set<String> test) {
		if (suite != null) {
			dependentSuites = suite;
		}
		if (test != null) {
			dependentTests = test;
		}
	}
	
	public Set<String> getDependenciesSuites() {
		return dependentSuites;
	}

	public Set<String> getDependenciesTests() {
		return dependentTests;
	}

}
