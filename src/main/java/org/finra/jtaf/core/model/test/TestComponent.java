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
import java.util.List;
import java.util.regex.Pattern;

import org.finra.jtaf.core.model.exceptions.NameFormatException;

/**
 * Base class of all test-related components within the model, includes only TestScripts and TestNamespaces.
 * 
 * This class serves a few primary tasks: 1. It ensures that all subcomponents have proper names 2. It provides generic methods that can be used to crawl the model
 * 
 */
public abstract class TestComponent {

	// TODO: This needs to be stricter
	private static final Pattern VALID_NAME = Pattern.compile("[^/]+");

	private String name;
	private TestNamespace parent;

	/**
	 * 
	 * @param name
	 * @param parent
	 * @throws NameFormatException
	 */
	public TestComponent(String name) throws NameFormatException {
		if (!TestComponent.VALID_NAME.matcher(name).matches()) {
			throw new NameFormatException(name, TestComponent.VALID_NAME);
		}
		this.name = name;
		this.parent = null;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The full name of the component, including names of parent components
	 */
	public final String getFullName() {
		StringBuilder sb = new StringBuilder();
		getFullNameHelper(sb);
		return sb.toString();
	}

	private void getFullNameHelper(StringBuilder sb) {
		if (getParent() != null) {
			TestComponent p = getParent();
			p.getFullNameHelper(sb);
		}
		sb.append("/");
		sb.append(getName());
	}

	/**
	 * @return The TestGroup object that owns this TestTarget, or null if none exists
	 */
	public final TestNamespace getParent() {
		return parent;
	}

	/**
	 * Invoked by TestNamespace.addChild(). TestNamespace handles the details of disconnecting the previous parent.
	 * 
	 * @param parent
	 */
	protected final void setParent(TestNamespace parent) {
		this.parent = parent;
	}

	/**
	 * Returns a parent or child of the current node depending upon the token provided. Subclasses should invoke super.getNeighbor() before implementing their own logic
	 * 
	 * @param token
	 * @return
	 */
	public List<TestComponent> getNeighbor(String token) {
		List<TestComponent> newComponentMap = new ArrayList<TestComponent>();
		
		if ((token == null) || (token.equals("")) || (token.equals(TestPath.CURRENT))) {
			newComponentMap.add(this);
		} else if (token.equals(TestPath.PARENT)) {
			newComponentMap.add(getParent());
		} else {
			return null;
		}
		return newComponentMap;
	}

	/**
	 * Travel from this component to another component via the given path. This just calls getNeighbor() repeatedly against the components of the path until the destination is reached.
	 * 
	 * @param path
	 * @return
	 */
	public final List<TestComponent> toComponent(TestPath path) {
		List<TestComponent> newComponentMap = new ArrayList<TestComponent>();

		TestComponent current = this;
		// If this is an absolute path, then we need to start from the root.
		if (path.isAbsolute()) {
			while (current.getParent() != null) {
				current = current.getParent();
			}
		}
		//This is the root namespace ("testscripts")
		newComponentMap.add(current);

		//This cycles through the provided components of the TestPath delimited by "/" to get to the right componentmap
		if (path.getComponents().length > 0) {
			for (String c : path.getComponents()) {
				newComponentMap = newComponentMap.get(0).getNeighbor(c);
				if (newComponentMap == null) {
					return null;
				}
			}
		} else {
			newComponentMap.add(current);
		}
		return newComponentMap;
	}

	/**
	 * Convenience method: Converts a String to a TestPath
	 * 
	 * @param path
	 * @return
	 */
	public final List<TestComponent> toComponent(String path) {
		return toComponent(new TestPath(path));
	}

	public abstract void acceptTestVisitor(ITestVisitor v) throws Exception;

}
