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
package org.finra.jtaf.core.parsing.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.finra.jtaf.core.parsing.exceptions.MissingRequiredElementException;
import org.finra.jtaf.core.parsing.exceptions.MultipleMatchesException;
import org.finra.jtaf.core.parsing.exceptions.NestedXPathException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




/**
 * Helper class for parsing XML file. 
 */
public class ParserHelper {

	/**
	 * This class is mostly here because I cannot figure out how to
	 * disable case-sensitivity of node names.  It also provides a
	 * handful of other useful methods
	 */
	private static XPathFactory                     xpathFactory = null;
	private static HashMap<String, XPathExpression> compiledMap  = new HashMap<String, XPathExpression>();
	
	private static XPathFactory getXPathFactory() {
		if (ParserHelper.xpathFactory == null) {
			ParserHelper.xpathFactory = XPathFactory.newInstance();
		}
		return ParserHelper.xpathFactory;
	}
	

	
	/**
	 * A list of the immediate children of elem
	 * @param elem
	 * @return
	 */
	public static final List<Element> getChildren(Element elem) {
		ArrayList<Element> retval = new ArrayList<Element>();
		NodeList nl = elem.getChildNodes();
		for (int i = 0; i < nl.getLength(); ++i) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				retval.add((Element) n);
			}
			
		}
		return retval;
	}
	
	public static final Element getOptionalElement(Element root, String xpath) throws MultipleMatchesException, NestedXPathException {
		try {
			XPathExpression expr = null;
			
			if ((expr = ParserHelper.compiledMap.get(xpath)) == null) {
				expr = getXPathFactory().newXPath().compile(xpath);
				ParserHelper.compiledMap.put(xpath, expr);
			}
			
			// I use a NodeList here instead of an Element because I want to ensure
			// there are not multiple return values
			NodeList nl = (NodeList) expr.evaluate(root, XPathConstants.NODESET);
			
			if (nl.getLength() > 1) {
				throw new MultipleMatchesException(xpath);
			}

			// TODO: Ensure the return value is an Element?
			return (Element) nl.item(0);
		}
		catch (XPathException e) {
			throw new NestedXPathException(e);
		}
	}
	
	public static final Element getRequireElement(Element root, String xpath) throws MultipleMatchesException, MissingRequiredElementException, NestedXPathException {
		Element retval = getOptionalElement(root, xpath);
		if (retval == null) {
			throw new MissingRequiredElementException(xpath);
		}
		return retval;
	}
	
	public static final Element getFirstChildElementCaseInsensitive(Element root, String elementName) {
		NodeList nl = root.getChildNodes();
		if (nl.getLength() > 1) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && (node.getNodeName().equalsIgnoreCase(elementName))) {
					return (Element) node;
				}
			}
		}
			
		return null;
	}
}