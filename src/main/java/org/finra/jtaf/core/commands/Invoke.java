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
import java.util.List;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * This class allows functions to invoke blocks of code. It gives some degree of
 * functional programming within a test script.
 * 
 */
public class Invoke extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the Invoke step aren't needed, unlike the commands within the
     * Invoke block.
     * 
     * @param name
     *            - name of the command
     * @throws NameFormatException
     */
    public Invoke(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This goes through all commands in the context and executes the block that
     * has the same name.
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
        String param = (String) this.getOptionalObject("block");

        List<Object> params = (ArrayList<Object>) ctx.getAllObjects().get("blockparam");
        if (params != null) {
            for (Object elem : params) {
                Invocation invocation = (Invocation) elem;
                if (invocation.getTargetName().equals("block")
                        && invocation.getParameters().containsKey("name")
                        && invocation.getParameters().get("name").equals(param)) {
                    // get its params and see if it has name and if it is
                    // equal
                    // to the given block name
                    executeInvocation(invocation);
                }

            }
        }

    }

}
