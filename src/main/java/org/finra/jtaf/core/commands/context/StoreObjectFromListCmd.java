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
 * Gets an object from a list in context and stores that object into context
 */
public class StoreObjectFromListCmd extends AbstractContextCmd {
    public static final String VALUE_IN_ATTRIBUTE = "list";
    public static final String INDEX_ATTRIBUTE = "index";
    public static final String VALUE_OUT_ATTRIBUTE = "object";

    public StoreObjectFromListCmd(String name) throws NameFormatException {
        super(name);
    }

    @Override
    protected void execute() throws Throwable {
        Integer index = mch.getRequiredInteger(INDEX_ATTRIBUTE);
        if (index <= 0) {
            throw new Exception("Index is " + index
                    + ". Please provide a valid index. (Index starts at 1)");
        }
        index = index - 1; // for one-based
        Object valueIn = mch.getValueIn(getRequiredString(VALUE_IN_ATTRIBUTE), index);
        String valueOutAttribute = getRequiredString(VALUE_OUT_ATTRIBUTE);
        mch.setValueOut(valueOutAttribute, valueIn);
    }

}
