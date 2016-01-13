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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.utilities.CompositeDataComparator;

/**
 * Compares two objects from context
 */
public class VerifyObjectInContextCmd extends Command {

    public VerifyObjectInContextCmd(String name) throws NameFormatException {
        super(name);
    }

    @Override
    protected void execute(IInvocationContext arg0) throws Throwable {
        String actualKey = getRequiredString("actualKey");
        String expectedKey = getRequiredString("expectedKey");
        String title = getOptionalString("title");
        boolean failOnNotEqual = getBooleanOrDefault("failOnNotEqual", true);
        Object actual = getRequiredObject(actualKey);
        Object expected = getRequiredObject(expectedKey);
        CompositeDataComparator cdc = new CompositeDataComparator();
        cdc.setAccumulateErrors(true);
        cdc.compareObject(title, null, expected, actual);
        List<String> errorList = cdc.getErrorList();
        if (failOnNotEqual && errorList != null && !errorList.isEmpty()) {
            if (title != null) {
                throw new AssertionError("Expected " + title + " did not match actual:\n" + StringUtils.join(errorList, "\n"));
            }
            throw new AssertionError("Expected did not match actual:\n" + StringUtils.join(errorList, "\n"));
        }
        arg0.putObject("errorList", errorList);
    }
}
