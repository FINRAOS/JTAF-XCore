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

import java.util.HashMap;
import java.util.Map;

import org.finra.jtaf.core.model.exceptions.NameFormatException;

/**
 * Gets an object from context and stores that object into a map in context
 */
public class PutObjectInMapCmd extends AbstractContextCmd {
    public static final String VALUE_IN_ATTRIBUTE = "object";
    public static final String KEY_ATTRIBUTE = "key";
    public static final String VALUE_OUT_ATTRIBUTE = "map";

    public PutObjectInMapCmd(String name) throws NameFormatException {
        super(name);
    }

    @Override
    protected void execute() throws Throwable {
        Object valueIn = getRequiredObject(getRequiredString(VALUE_IN_ATTRIBUTE));
        String valueOutAttribute = getRequiredString(VALUE_OUT_ATTRIBUTE);
        @SuppressWarnings("unchecked")
        Map<Object, Object> valueOut = (Map<Object, Object>) getOptionalObject(valueOutAttribute);
        if (valueOut == null) {
            Map<Object, Object> suppression = new HashMap<Object, Object>();
            valueOut = suppression;
        }
        Object key = mch.getRequiredObject(KEY_ATTRIBUTE);
        mch.setValueOut(valueOutAttribute, valueOut, valueIn, key);
    }

}
