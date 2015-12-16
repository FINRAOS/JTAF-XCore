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

import org.finra.jtaf.core.model.exceptions.ExpectedFailureException;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * This command surrounds a block of commands to be executed that are expected
 * to throw an exception. If one of them throws an exception, no more commands
 * from the block are executed and the remainder of the script is run through.
 * If no exception is thrown, an ExpectedFailureException to signify this.
 */
public class ExpectFailure extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter. The recordResult variable is set to false because details
     * about the ExpectFailure step aren't needed, unlike the commands within
     * the ExpectFailure block.
     *
     * @param name - The name of the command
     * @throws NameFormatException
     */
    public ExpectFailure(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This method goes through all of the commands within the ExpectFailure
     * block and executes them until they have all been executed or an exception
     * has been thrown. If an exception is thrown and it is not an
     * ExpectedFailureException from a nested ExpectFailure, no more commands in
     * the block are executed and the rest of the script is run. If the
     * exception is an ExpectedFailureException, it is thrown and if no
     * exception is encountered when executing the block, a new
     * ExpectedFailureException is thrown.
     *
     * @param ctx - The current context when this method is executed.
     * @throws Throwable - Either a ExpectedFailureException or the exception thrown
     *                   by a command in the block
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {
        ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");
        if (params != null) {
            for (Object child : params) {
                ctx.putObject("expectFailureBlockFlag", true);
                try {
                    executeInvocation((Invocation) child);
                    ctx.putObject("expectFailureBlockFlag", false);

                } catch (ExpectedFailureException efe) {
                    ctx.putObject("expectFailureBlockFlag", false);
                    throw efe;
                } catch (Throwable e) {
                    ctx.putObject("expectFailureBlockFlag", false);
                    return;
                }

            }
        }
        ctx.putObject("expectFailureBlockFlag", false);

        throw new ExpectedFailureException();

    }

}
