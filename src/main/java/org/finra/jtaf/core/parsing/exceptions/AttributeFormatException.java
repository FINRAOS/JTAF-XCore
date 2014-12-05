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
package org.finra.jtaf.core.parsing.exceptions;

/**
 * This is thrown, while parsing, when an element's attribute is not in the expected format.
 * For example, when an attribute is expected to be an integer but cannot be parsed as such
 */
public class AttributeFormatException extends ParsingException {

	/**
	 * Auto-generated
	 */
	private static final long serialVersionUID = 4579028460955204606L;

	/**
	 * @param expectedType a String describing the expected format. For example, "Integer"
	 */
	public AttributeFormatException(final String expectedType) {
		super("Illegal attribute format (expected " + expectedType + ")");
	}
}
