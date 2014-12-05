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
package org.finra.jtaf.core.parsing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




/**
 * I plan to scrap these parsers within a few releases to allow for a more
 * flexible model.  In particular, I want the plugins to be able to extend
 * the behavior of the parser.  For example, if we create a StarTeam plugin,
 * then we will want to pull CR information from the tests and commands.
 * 
 * 
 *
 */
public abstract class BaseParser {
	private final DocumentBuilder  db;
	private       Document         currentDocument;
	private final MessageCollector errorCollector;
	
	public BaseParser() throws ParserConfigurationException {
		this.db              = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.currentDocument = null;
		this.errorCollector  = new MessageCollector();
	}
	

	public final void parse(File f) throws ParsingException, SAXException, IOException {
		currentDocument = db.parse(f);
		getErrorCollector().reset("In file " + f.getAbsolutePath());
		handleRoot(getCurrentDocument().getDocumentElement());
		currentDocument = null;
	}


	/**
	 * Do whatever to the document root
	 * @param root
	 */
	protected abstract void handleRoot(Element root) throws ParsingException ;



	protected final Document getCurrentDocument() {
		return this.currentDocument;
	}
	
	/**
	 * It is the caller's responsibility to print the log, if desired
	 * @return The ErrorCollector used by this parser
	 */
	public final MessageCollector getErrorCollector() {
		return this.errorCollector;
	}
	
	
	
	
	/**
	 * TODO: There is probably a more effective way to do this, but this does the trick for now
	 * @param elem
	 * @return
	 */
	protected final List<Element> getChildElements(Element elem) {
		ArrayList<Element> retval = new ArrayList<Element>();
		final NodeList children = elem.getChildNodes();
		for(int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				retval.add((Element) child);
			}
		}
		return retval;
	}


	
	
	/**
	 * Generic method for printing unexpected element errors
	 * @param elem
	 */
	protected final void reportUnexpectedElement(Element elem) {
		this.getErrorCollector().error("Unexpected element: " + elem.getNodeName());
	}
	
	

}
