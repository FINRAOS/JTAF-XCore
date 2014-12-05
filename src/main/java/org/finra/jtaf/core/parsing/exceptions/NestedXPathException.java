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

import javax.xml.xpath.XPathException;

/**
 * This is a wrapper for XPath exceptions.
 * 
 */
public class NestedXPathException extends ParsingException {

	/**
	 * AUTO-GENERATED
	 */
	private static final long serialVersionUID = -6483364508583432000L;

	/**
	 * @param e The wrapped XPath
	 */
	public NestedXPathException(XPathException e) {
		super(e);
	}
}
