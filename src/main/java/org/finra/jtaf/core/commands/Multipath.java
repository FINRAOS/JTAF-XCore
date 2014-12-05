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
import java.util.Random;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * Used to specify control paths that have the same end result. This allows
 * testers to write their scripts at a higher level of abstraction rather than
 * paying attention to an explicit set of actions that need to be performed.
 * 
 * TODO: Add an optional weight to each of the paths. This will cause certain
 * paths to be executed w/ a higher probability.
 * 
 * 
 */
public class Multipath extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the Multipath step aren't needed, unlike the commands within the
     * Multipath block.
     * 
     * @param name - name of the command
     * @throws NameFormatException
     */
    public Multipath(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This randomly picks a path and executes all commands in the block. For
     * this command, 'Path' is a named Block command.
     * 
     * @param ctx
     *            - The current context when this method is executed.
     * 
     * @throws Throwable
     *             - Any exception thrown by a command within the Repeat block.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {
        ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");
        if (params != null) {

            Random rand = new Random();
            int index = Math.abs(rand.nextInt()) % params.size();
            // This gets the 'path'
            Invocation path = (Invocation) params.get(index);
            // This then gets the block in the path that contains all the
            // commands to be executed

            executeInvocation(path);

        }

    }
}
