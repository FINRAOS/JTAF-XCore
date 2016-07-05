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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;


/**
 * Serves as a container for messages and other message sections.
 * 
 * TODO: Sections and Messages should have the same base class, but
 *       I can't think of a good word for it.
 * 
 */
public class Section {
	private final String        text;
	private final Section       parent;
	
	private       List<Section> subsections;
	private       List<Message> messages;
	private       Level         level;
	

	/**
	 * External classes are only allowed to instantiate root
	 * message sections.  Subsections are created automatically
	 * upon request.
	 * @param name
	 */
	public Section(String name) {
		this(name, null);
	}
	
	/**
	 * Private constructor used to create subsections
	 * @param text
	 * @param parent
	 */
	private Section(String text, Section parent) {
		this.text        = text;
		this.parent      = parent;
		this.subsections = new ArrayList<Section>();
		this.messages    = new LinkedList<Message>();
		this.level       = Level.ALL;
	}

	/**
	 * A text label for the section.  Something like "In file XYZ"
	 * @return
	 */
	public final String getName() {
		return this.text;
	}
	
	/**
	 * Does this Section contain anything useful?
	 * @return
	 */
	public final boolean isEmpty() {
		return (this.subsections.isEmpty() && this.messages.isEmpty());
	}

	/**
	 * Retrieves the section w/ the given name, or creates a new
	 * section if none w/ the given name exists
	 * @param name
	 * @return
	 */
	public Section newSection(String name) {
		Section retval = new Section(name, this);
		this.subsections.add(retval);
		return retval;
	}

	
	/**
	 * @return A list of subsections
	 */
	public List<Section> getSections() {
		return this.subsections;
	}

	
	/**
	 * Inserts the message into this section.  If the message's level
	 * is greater than this section's level, the section set its level
	 * equal to the message's level and inform its parent.
	 * @param message
	 */
	public final void addMessage(Message message) {
		this.messages.add(message);
		this.tryRaiseLevel(message.getLevel());
	}
	
	
	/**
	 * 
	 * @param level
	 */
	private void tryRaiseLevel(Level level) {
		if (!this.getLevel().isGreaterOrEqual(level)) {
			this.level = level;
			if (this.getParent() != null) {
				this.getParent().tryRaiseLevel(level);
			}
		}
	}

	/**
	 * @return A list of the messages within this section
	 */
	public final List<Message> getMessages() {
		return new ArrayList<Message>(this.messages);
	}
	
	/**
	 * A section's level is Max(max level of subsection, max level of message).
	 * In other words, the level is the greatest level of a child element.
	 * @return
	 */
	public final Level getLevel() {
		return this.level;
	}

	
	/**
	 * @return The parent of this section, or null if this section is the root
	 */
	public final Section getParent() {
		return this.parent;
	}
}