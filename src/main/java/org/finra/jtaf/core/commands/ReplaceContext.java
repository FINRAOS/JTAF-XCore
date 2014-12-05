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

import java.util.Map;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.CorrectiveContext;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * This command overwrites the values in the current context.
 * 
 */
public class ReplaceContext extends Command {

    /**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the ReplaceContext step aren't needed, unlike the commands within
     * the ReplaceContext block.
     * 
     * @param name
     *            - name of the command
     * @throws NameFormatException
     */
    public ReplaceContext(String name) throws NameFormatException {
        super(name);
        recordResult = false;

    }

    /**
     * This clears the current context and then puts all values from within the
     * ReplaceContext block into the context.
     * 
     * @param ctx
     *            - The current context when this method is executed.
     * 
     * @throws Throwable
     *             - Any exception thrown by a command within the ReplaceContext
     *             block.
     */
    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {
        boolean expectFailure = false;
        boolean ignoreErrors = false;

        expectFailure = (Boolean) ctx.getObject("expectFailureBlockFlag");
        ignoreErrors = (Boolean) ctx.getObject("ignoreErrorsBlockFlag");
        Map<String, Object> params = ctx.getAllObjects();

        ((CorrectiveContext) ctx).popFrame();
        Map<String, Object> previousParams = ctx.getAllObjects();

        ((CorrectiveContext) ctx).pushInvocation(new Invocation("ReplaceContext"), this);

        clearContext();

        if (params != null) {
            for (String key : params.keySet()) {
                // Replace all non-invocation targets in the context (meaning
                // strings, maps, lists)
                if (params.get(key) instanceof Invocation) {
                    System.out.println("This happens" + params.get(key));
                    ((CorrectiveContext) ctx).putObject(key, params.get(key));
                } else if (!previousParams.keySet().contains(key)
                        || (previousParams.keySet().contains(key) && previousParams.get(key) != params
                                .get(key))) {
                    ((CorrectiveContext) ctx).putObject(key, params.get(key));
                    // ((CorrectiveContext) ctx).getAllObjects().p
                }
            }
        }
        ctx.putObject("expectFailureBlockFlag", expectFailure);
        ctx.putObject("ignoreErrorsBlockFlag", ignoreErrors);

    }
}
