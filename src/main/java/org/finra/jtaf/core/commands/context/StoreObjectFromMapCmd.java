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
 * Gets an object from a map in context and stores that object into context
 */
public class StoreObjectFromMapCmd extends AbstractContextCmd {
    public static final String VALUE_IN_ATTRIBUTE = "map";
    public static final String INDEX_ATTRIBUTE = "mapKey";
    public static final String VALUE_OUT_ATTRIBUTE = "objectKey";

    public StoreObjectFromMapCmd(String name) throws NameFormatException {
        super(name);
    }

    @Override
    protected void execute() throws Throwable {
        Object key = mch.getRequiredObject(INDEX_ATTRIBUTE);
        Object valueIn = mch.getValueIn(getRequiredString(VALUE_IN_ATTRIBUTE), key);
        String valueOutAttribute = getRequiredString(VALUE_OUT_ATTRIBUTE);
        mch.setValueOut(valueOutAttribute, valueIn);
    }

}
