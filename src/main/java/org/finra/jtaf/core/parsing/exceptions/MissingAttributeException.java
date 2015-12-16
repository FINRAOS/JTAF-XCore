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

import org.w3c.dom.Element;

/**
 * Thrown during parsing when an XML element is missing an attribute. For example,
 * when a test element does not have a name.
 */

public class MissingAttributeException extends ParsingException {


    private static final long serialVersionUID = -3806789951904835575L;

    private static final String createMessage(Element e, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("The element '");
        sb.append(e.getNodeName());
        sb.append("' is missing the required attribute '");
        sb.append(name);
        sb.append("'");
        return sb.toString();
    }

    /**
     * @param e    The element that is missing the attribute
     * @param name The attribute that is missing from the element
     */
    public MissingAttributeException(Element e, String name) {
        super(MissingAttributeException.createMessage(e, name));
    }
}
