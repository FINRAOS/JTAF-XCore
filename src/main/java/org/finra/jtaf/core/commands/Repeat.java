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

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * This command surrounds a block of commands to be repeatedly executed based on
 * the value of the iterations parameter.
 */
public class Repeat extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the Repeat step aren't needed, unlike the commands within the
     * Repeat block.
     *
     * @param name - name of the command
     * @throws NameFormatException
     */
    public Repeat(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This simply loops through and executes all commands within the Repeat
     * block. This is repeated for the specified iterations.
     *
     * @param ctx - The current context when this method is executed.
     * @throws Throwable - Any exception thrown by a command within the Repeat block.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {
        ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");
        int iterations = Integer.parseInt(this.getRequiredString("iterations"));
        if (params != null) {
            for (int x = 0; x < iterations; x++) {
                for (Object child : params) {
                    executeInvocation((Invocation) child);

                }
            }
        }
    }
}
