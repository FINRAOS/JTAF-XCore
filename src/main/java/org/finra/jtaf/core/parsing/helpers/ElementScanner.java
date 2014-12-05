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

import java.util.List;

import org.w3c.dom.Element;

/**
 * Convenience class that can be used to process a sequence of
 * Elements.  This will most likely be used in the preProcessing
 * stage.
 * 
 *
 */
public class ElementScanner {

	private final List<Element> elements;
	private       int           index;

	public ElementScanner(List<Element> elements) {
		this.elements = elements;
		this.index    = 0;
	}

	public final boolean hasNext() {
		return this.index < this.elements.size();
	}

	public Element peek() {
		return this.elements.get(this.index);
	}


	
	/**
	 * Shortcut method
	 * @param name
	 * @return
	 */
	public Element tryMatch(String name) {
		if(this.hasNext()) {
			Element next = this.peek();
			if(next.getNodeName().equalsIgnoreCase(name)) {
				++(this.index);
				return next;
			}
		}
		return null;
	}
	
	
	/**
	 * Convenience method: Returns this.peek() and moves to the next element
	 * @return
	 */
	public Element match() {
		if(this.hasNext()) {
			Element retval = this.peek();
			++(this.index);
			return retval;
		}
		else {
			return null;
		}
	}
}
