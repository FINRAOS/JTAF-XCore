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
 * This is thrown when we expect an XPath query to return one value
 * but it returns several values.
 */
public class MultipleMatchesException extends ParsingException {

	/**
	 * AUTO-GENERATED
	 */
	private static final long serialVersionUID = 51467102839218606L;

	private final String xpath;

	/**
	 * @param xpath The XPath that is giving multiple matches
	 */
	public MultipleMatchesException(String xpath) {
		super("The XPath query \"" + xpath + "\" matched multiple nodes (only one match was expected)");
		this.xpath = xpath;
	}
	
	/**
	 * @param xpath The XPath that is giving multiple matches
	 */
	public final String getXPath() {
		return this.xpath;
	}
}
