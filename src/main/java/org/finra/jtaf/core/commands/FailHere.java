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

import org.finra.jtaf.core.asserts.AssertionFailedError;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;

/**
 * This command is used to force a failure. This is mainly used for testing
 * other commands to make sure that they work correctly.
 */
public class FailHere extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter
     *
     * @param name - The name of the command
     * @throws NameFormatException
     */
    public FailHere(String name) throws NameFormatException {
        super(name);

    }

    /**
     * This method throws an AssertionFailedException to force a failure.
     *
     * @param ctx - The current context when this method is executed.
     * @throws Throwable - The AssertionFailedException
     */
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {

        throw new AssertionFailedError("Assertion Failure in FailHere Command");

    }

}
