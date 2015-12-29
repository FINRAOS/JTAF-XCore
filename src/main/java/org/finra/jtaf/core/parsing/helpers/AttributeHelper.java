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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.finra.jtaf.core.parsing.exceptions.AttributeFormatException;
import org.finra.jtaf.core.parsing.exceptions.MissingAttributeException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


/**
 * Helper class to fetch attributes of an element.
 */
public class AttributeHelper {
    /**
     * I want to remove this class once I figure out how to treat node names
     * in a case-insensitive manner.  Until this, this is a necessary evil :-D
     */

    private static final Pattern integerRegex = Pattern.compile("\\s*\\d+\\s*");

    private final Map<String, String> attrs;
    private final Element source;
    private final Set<String> collisions;

    /**
     * Collect the attributes, make sure their names are all cast to lowercase,
     * and throw an exception if there are name collisions.
     *
     * @param qName
     * @param attributes
     */
    public AttributeHelper(Element e) {
        attrs = new HashMap<String, String>();
        source = e;
        collisions = new HashSet<String>();
        NamedNodeMap origAttrs = source.getAttributes();

        for (int i = 0; i < origAttrs.getLength(); ++i) {
            final String name = origAttrs.item(i).getNodeName().toLowerCase();
            if (attrs.containsKey(name)) {
                collisions.add(name);
            }
            attrs.put(name, origAttrs.item(i).getNodeValue());
        }
    }


    /**
     * @return A copy of the collision set
     */
    public final Set<String> getCollisions() {
        return new HashSet<String>(this.collisions);
    }

    /**
     * @return The entrySet used internally
     */
    public Set<Entry<String, String>> entrySet() {
        return this.attrs.entrySet();
    }


    /**
     * Make sure you don't modify this return value
     *
     * @return The map maintained internally
     */
    public final Map<String, String> getMap() {
        return this.attrs;
    }

    /**
     * Search for the given key, ignoring case
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return this.attrs.containsKey(key.toLowerCase());
    }

    /**
     * Search for the given value
     *
     * @param value
     * @return
     */
    public boolean containsValue(String value) {
        return this.attrs.containsValue(value);
    }


    /**
     * Throws an exception if the requested attribute is undefined or an empty string
     *
     * @param key
     * @return
     * @throws MissingAttributeException
     */
    public String getRequiredString(String key) throws MissingAttributeException {
        String retval = this.getOptionalString(key);
        if ((retval == null) || (retval.trim().equals(""))) {
            throw new MissingAttributeException(this.source, key);
        }
        return retval;
    }

    /**
     * Search for the given key, ignoring case
     *
     * @param key
     * @return
     */
    public String getOptionalString(String key) {
        return this.attrs.get(key.toLowerCase());
    }


    /**
     * If the requested attribute
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public final String getStringOrDefault(String key, String defaultValue) {
        String retval = this.getOptionalString(key);
        if (retval == null) {
            return defaultValue;
        }
        return retval;
    }


    /**
     * Ensures that the attribute is a properly-formatted integer
     *
     * @param key
     * @return
     * @throws MissingAttributeException
     * @throws AttributeFormatException
     */
    public final int getRequiredInteger(String key) throws MissingAttributeException, AttributeFormatException {
        final String retval = this.getOptionalString(key);
        if ((retval == null) || (retval.equals(""))) {
            throw new MissingAttributeException(this.source, key);
        }

        if (integerRegex.matcher(retval) == null) {
            throw new AttributeFormatException("Integer");
        }

        return Integer.parseInt(retval);
    }

}
