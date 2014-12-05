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
package org.finra.jtaf.core.utilities.logging;

import java.util.Stack;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.finra.jtaf.core.utilities.StringHelper;



/**
 * This is used to collect and log error messages in a hierarchical fashion
 */
public class MessageCollector {
	private       Section        root;
	private       Stack<Section> history;
	
	public MessageCollector() {
		this("ErrorCollector Root");
	}
	
	public MessageCollector(String rootName) {
		this.root = new Section(rootName);
		this.history = new Stack<Section>();
		this.history.push(this.root);
	}

	public final void reset(String rootName) {
		this.root = new Section(rootName);
		this.history.clear();
		this.history.push(this.root);
	}

	public final Section getRootSection() {
		return this.root;
	}

	public final Section getCurrentSection() {
		return this.history.peek();
	}


	public final Section push(String sectionName) {
		return this.history.push(this.getCurrentSection().newSection(sectionName));
	}
	
	public final void pop() {
		Section retval = this.history.pop();

		// If the section we just exited didn't contain anything,
		// then delete it.
		if(retval.isEmpty()) {
			// TODO: This could be optimized, but whatever
			this.history.peek().getSections().remove(retval);
		}
	}

	
	
	public final void log(Level level, String message) {
		this.getCurrentSection().addMessage(new Message(level, message));
	}
	
	public final void debug(String message) {
		this.log(Level.DEBUG, message);
	}
	
	public final void info(String message) {
		this.log(Level.INFO, message);
	}
	
	public final void warn(String message) {
		this.log(Level.WARN, message);
	}
	
	public final void error(String message) {
		this.log(Level.ERROR, message);
	}
	
	public final void fatal(String message) {
		this.log(Level.FATAL, message);
	}
	
	public final void dump(Logger log) {
		this.recursiveDump(log, this.getRootSection(), 0);
	}
	
	private final void recursiveDump(Logger log, Section section, int depth) {
		if(!Level.ALL.isGreaterOrEqual(section.getLevel())) {
			log.log(section.getLevel(), StringHelper.indent(depth) + section.getName());
			
			++depth;
			
			for(Message m : section.getMessages()) {
				log.log(m.getLevel(), StringHelper.indent(depth) + m.getText());
			}
			
			for(Section s : section.getSections()) {
				this.recursiveDump(log, s, depth);
			}
		}
	}
	

	
}
