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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.finra.jtaf.core.model.exceptions.NameCollisionException;
import org.finra.jtaf.core.model.exceptions.NameFormatException;

/**
 * These are the internal nodes of our testing model.
 * 
 */
public class TestNamespace extends TestComponent implements Iterable<TestComponent> {

	// The set of all objects and namespaces.  Used for efficiency
	private final Map<String, TestComponent> componentMap;
	private final List<TestComponent>        componentList;
	//This might not be necessary after TestSuite.java has been done
	// TODO: determine if suiteMap is still necessary
	// TODO: if the above is true, implement suiteMap as singleton
	private final Map<String, TestSuite> suiteMap;
	
	protected static TestNamespace root = null;
	/**
	 * Instantiates a new TestGroup without a parent.  The
	 * parent is assigned when the TestGroup is added to
	 * 
	 * @param name
	 * @throws NameFormatException
	 */
	public TestNamespace(String name) throws NameFormatException {
		super(name);

		componentMap  = new TreeMap<String, TestComponent>();
		componentList = new LinkedList<TestComponent>();
		//This logic is so that only the TestNamespace root has the suiteMapping.
		if (root == null) {
			suiteMap = new HashMap<String, TestSuite>();
			root = this;
		} else {
			suiteMap = null;
		}
	}

	
	/**
	 * @param name
	 * @return True if this namespace contains a component w/ the supplied name
	 */
	public final boolean isComponentMapContain(String name) {
		return componentMap.containsKey(name);
	}
	
	/**
	 * @param c
	 * @return True if this contains the given component
	 */
	public final boolean contains(TestComponent c) {
		return componentMap.containsValue(c);
	}
	
	
	public final void add(TestComponent testComponent) throws NameCollisionException {
		// Don't allow multiple components to have the same name
		if (isComponentMapContain(testComponent.getName())) {
			throw new NameCollisionException(testComponent.getName());
		}
		componentMap.put(testComponent.getName(), testComponent);
		componentList.add(testComponent);
		testComponent.setParent(this);
		
		if (this instanceof TestNamespace && testComponent instanceof TestSuite) {
			setSuiteMapping((TestSuite) testComponent);
		}
	}

	//MULTITHREAD 2012.4--START-----------------------------------
	//This needs to find the testScripts TestNamespace and then propogate the testSuiteName 
	//and the TestScript name up to that level
	//This should only set the suitemapping on the main root
	private void setSuiteMapping(TestSuite ts) throws NameCollisionException {
		String suiteName = ts.getTestSuiteName();
		if (root.suiteMap.containsKey(suiteName)) {
			//this exists already throw exception
			throw new NameCollisionException(suiteName);
		}
		root.suiteMap.put(suiteName, ts);
	}

	public Map<String, TestSuite> getTestSuiteMapping() {
		return root.suiteMap;
	}
	
	//MULTITHREAD 2012.4-------END------------------------------
	
	@Override
	public final List<TestComponent> getNeighbor(String name) {
		List<TestComponent> newComponentMap = new ArrayList<TestComponent>();
		List<TestComponent> neighbors = super.getNeighbor(name);
		if (neighbors != null && neighbors.size() > 0) {
			newComponentMap.addAll(neighbors);
		} else {
			newComponentMap.addAll(getWithOutTestDataInfo(name));
		}
		return newComponentMap;
	}

	
	public final List<TestComponent> getWithOutTestDataInfo(String name) {
		List<TestComponent> newComponentMap = new ArrayList<TestComponent>();
		
		Set<String> keys = componentMap.keySet();
		for (String key: keys) {
			if (removeTestDataAdditionalInfo(key).equalsIgnoreCase(name)) {
				newComponentMap.add(componentMap.get(key));
			}
		}
		return newComponentMap;
	}
	
	
	private String removeTestDataAdditionalInfo(String name) {
		// remove ' [<test data info>]' section
		String[] names = name.split("[ ]*\\[");
		return names[0];
	}
	

	/**
	 * This returns a copy of the component list in order to
	 * prevent people from muddling w/ the internal state of
	 * the class.
	 * @return A copy of the component list
	 */
	public final List<TestComponent> getComponentList() {
		return componentList;
	}

	// TODO: remove if truly not used
	/**
	 * FIXME: This should return a read-only iterator
	 * NOT USED
	 */
	public final Iterator<TestComponent> iterator() {
		return this.getComponentList().iterator();
	}

	public final void acceptTestVisitor(ITestVisitor v) throws Exception {
		v.visitTestNamespace(this);
	}

}
