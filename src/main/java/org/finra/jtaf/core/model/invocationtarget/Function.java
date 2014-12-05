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
package org.finra.jtaf.core.model.invocationtarget;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.statement.InvocationList;

/**
 * This allows for a collection of commands to be defined and easily called in
 * the test script.
 */
public final class Function extends InvocationTarget {

    private InvocationList invocations;

    /**
     * This called when instantiating the function before it is executed by the
     * interpreter.
     * 
     * @param name
     *            - name of the function
     * @throws NameFormatException
     */
    public Function(String name) throws NameFormatException {
        super(name);
        this.invocations = new InvocationList();
    }

    /**
     * Get the invocations that make up this function.
     * 
     * @return The InvocationList associated with this function
     */
    public final InvocationList getBody() {
        return this.invocations;
    }

    /**
     * Set the body of this function.
     * 
     * @param invocations
     *            - the invocation list that will be used as the function body.
     */
    public final void setBody(InvocationList invocations) {
        this.invocations = invocations;
    }

    /**
     * @param v - the interpreter used that is executing the function.
     * @throws - an exception thrown by the execution of the function
     */
    public void acceptInvocationTargetVisitor(Interpreter v) throws Throwable {
        v.visitFunction(this);
    }

}
