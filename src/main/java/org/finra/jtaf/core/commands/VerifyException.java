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

package org.finra.jtaf.core.commands;

import java.util.ArrayList;

import junit.framework.AssertionFailedError;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * This command is used to verify that a block of commands throws a specific
 * exception. This is mainly used for testing other commands.
 */
public class VerifyException extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the VerifyException step aren't needed, unlike the commands within
     * the VerifyException block.
     *
     * @param name - name of the command
     * @throws NameFormatException
     */
    public VerifyException(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This goes through all commands in the context and executes them. If an
     * exception is encountered, it is checked against the exceptionType
     * parameter. If it is not the same, an exception is thrown. If no exception
     * is encountered, an exception will be thrown. Otherwise, no exception is
     * thrown.
     *
     * @param ctx - The current context when this method is executed.
     * @throws Throwable - Any exception thrown by a command within the Repeat block.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {

        ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");
        String exception = (String) this.getRequiredObject("exceptionType");
        boolean found = false;
        if (params != null) {
            for (Object child : params) {
                try {
                    executeInvocation((Invocation) child);
                } catch (Throwable e) {
                    if (!e.getClass().getSimpleName().equalsIgnoreCase(exception)) {
                        throw new AssertionFailedError("The exception thrown was "
                                + e.getClass().getSimpleName() + ", not " + exception);
                    } else {
                        found = true;
                    }
                }

            }
        }
        if (!found) {
            throw new AssertionFailedError("The exception " + exception + " was not encountered");
        }
    }

}
