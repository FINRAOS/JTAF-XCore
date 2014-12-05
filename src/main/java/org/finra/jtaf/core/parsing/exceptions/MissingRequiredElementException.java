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
 * Thrown during parsing when a required XML element does not exist. For example,
 * when a function does not have a body element
 */
public class MissingRequiredElementException extends ParsingException {

	/**
	 * AUTO-GENERATED
	 */
	private static final long serialVersionUID = 3859741174693262134L;

	/**
	 * @param xpath An XPath that describes the missing element
	 */
	public MissingRequiredElementException(String xpath) {
		super("The XPath \"" + xpath + "\" did not match any nodes");
	}
}
