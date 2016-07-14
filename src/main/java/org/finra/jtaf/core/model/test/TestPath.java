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


/**
 * Handles basic path-related tasks:
 * - Removes unnecessary '/' characters
 * - Determines if the path is absolute
 * - Splits the components
 * 
 */
public class TestPath {
	public static final String SEPARATOR    = "/";
	public static final String CURRENT      = ".";
	public static final String PARENT       = "..";

	
	private final String   path;
	private final String   normalizedPath;
	private final String[] components;
	private final boolean  absolute;

	public TestPath(final String path) {
		this.path = path;
		normalizedPath = path.replaceAll("/+", "/");

		String[] temp = normalizedPath.split(TestPath.SEPARATOR);
		if (normalizedPath.charAt(0) == '/') {
			absolute = true;
		}
		else {
			absolute = false;
		}
		components = temp;
	}

	/**
	 * @return True if this is an absolute path; false otherwise
	 */
	public final boolean isAbsolute() {
		return absolute;
	}

	/**
	 * @return True if this is a relative path; false otherwise
	 */
	public final boolean isRelative() {
		return !absolute;
	}

	/**
	 * This is identical to toString
	 * @return
	 */
	public final String getPath() {
		return path;
	}
	
	/**
	 * @return The path wo/ duplicate / characters
	 */
	public final String getNormalizedPath() {
		return normalizedPath;
	}


	/**
	 * TODO: It may be necessary to return a copy of the components
	 * @return
	 */
	public String[] getComponents() {
		return components;
	}


	public String toString() {
		return path;
	}
}
