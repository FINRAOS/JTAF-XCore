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
 * This command surrounds a block of commands to be executed. If one of them
 * throws an exception, it is stored to be reported at the end of the test and
 * any commands after this block are then executed.
 */
public class IgnoreErrors extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter. The recordResult variable is set to false because details
     * about the IgnoreErrors step aren't needed, unlike the commands within the
     * IgnoreErrors block.
     *
     * @param name - The name of the command
     * @throws NameFormatException
     */
    public IgnoreErrors(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This method goes through all of the commands within the IgnoreErrors
     * block and executes them until they have all been executed or an exception
     * has been thrown. If an exception is thrown, it is stored in the
     * ErrorAccumulator to be reported at the end of the test.
     *
     * @param ctx - The current context when this method is executed.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void execute(IInvocationContext ctx) {
        ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");
        if (params != null) {
            for (Object child : params) {
                ctx.putObject("ignoreErrorsBlockFlag", true);

                try {
                    executeInvocation((Invocation) child);

                } catch (Throwable e) {

                    interpreter.addError(e);
                    // logger.info("Error encountered with IgnoreErrors block for statement: "
                    // + child.toString());
                    System.err.println("Error encountered with IgnoreErrors.");

                    ctx.putObject("ignoreErrorsBlockFlag", false);
                    return;
                }

            }
            ctx.putObject("ignoreErrorsBlockFlag", false);

        }
    }

}
