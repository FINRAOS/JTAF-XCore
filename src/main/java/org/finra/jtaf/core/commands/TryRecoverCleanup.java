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
 * Allows for try-catch-finally type logic to be performed within a test script.
 */
public class TryRecoverCleanup extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the TryRecoverCleanup step aren't needed, unlike the commands
     * within the TryRecoverCleanup block.
     *
     * @param name - name of the command
     * @throws NameFormatException
     */
    public TryRecoverCleanup(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This goes through and executes all the commands within the try block. If
     * an exception is thrown, it will then try to execute all commands in the
     * recover block, if it exists, and finally execute all commands in the
     * cleanup block if it exists.
     *
     * @param ctx - The current context when this method is executed.
     * @throws Throwable - Any exception thrown by a command within one of the blocks.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {
        ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");

        Throwable error = null;
        int cleanup = 1;
        if (params != null) {
            try { // Try
                executeInvocation((Invocation) params.get(0));

            } catch (Throwable t) { // Recover
                error = t;

                try {
                    if (((Invocation) params.get(1)).getTargetName().equals("recover")) {
                        cleanup = 2;
                        executeInvocation((Invocation) params.get(1));
                    }
                } catch (Throwable ignore) {
                }
            } finally { // Cleanup
                try {
                    Invocation cleanupInvocation = (Invocation) params.get(cleanup);
                    if (cleanupInvocation.getTargetName().equalsIgnoreCase("cleanup")) {
                        // handle tearDown block
                        Object isTearDownParameter = cleanupInvocation.getParameters().get("isteardown");
                        boolean isTearDown = isTearDownParameter != null && isTearDownParameter.toString().equalsIgnoreCase("true");
                        if (isTearDown)
                            interpreter.executeTearDownPlugins(error, ctx);
                        executeInvocation(cleanupInvocation);
                    }
                } catch (Throwable ignore) {
                    // Only handle cleanup exceptions if there was no other
                    // exception
                    if (error == null && cleanup < params.size()) {
                        error = ignore;
                    }
                }
            }
        }
        if (error != null) {
            throw error;
        }

    }
}
