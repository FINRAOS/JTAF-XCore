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

import org.apache.log4j.Level;


/**
 * Message w/ associated logging level.  Used to print logging
 * messages in a hierarchical fashion (useful for printing messages
 * from parsers, etc)
 */
public class Message {
    private final Level level;
    private final String text;


    public Message(Level level, String text) {
        this.level = level;
        this.text = text;
    }

    /**
     * @return Log level associated w/ this message
     */
    public final Level getLevel() {
        return this.level;
    }

    /**
     * @return The text of the message
     */
    public final String getText() {
        return this.text;
    }
}
