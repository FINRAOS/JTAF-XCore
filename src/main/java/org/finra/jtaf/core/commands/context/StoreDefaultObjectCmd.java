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
package org.finra.jtaf.core.commands.context;

import org.finra.jtaf.core.model.exceptions.NameFormatException;

/**
 * Stores a default value into context unless the object already exists in context
 */
public class StoreDefaultObjectCmd extends AbstractContextCmd {
    public static final String DEFAULT_ATTRIBUTE = "defaultKey";
    public static final String DEFAULT = "default";
    public static final String VALUE_OUT_ATTRIBUTE = "contextKey";

    public StoreDefaultObjectCmd(String name) throws NameFormatException {
        super(name);
    }

    @Override
    protected void execute() {
        String valueOutAttribute = getRequiredString(VALUE_OUT_ATTRIBUTE);
        Object valueOut = getOptionalObject(valueOutAttribute);
        if (valueOut == null) {
            String defaultAttribute = getStringOrDefault(DEFAULT_ATTRIBUTE, DEFAULT);
            valueOut = getRequiredObject(defaultAttribute);
        }
        mch.setValueOut(valueOutAttribute, valueOut);
    }

}
