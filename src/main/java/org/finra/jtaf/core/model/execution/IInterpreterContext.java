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

package org.finra.jtaf.core.model.execution;

import java.util.Map;

import org.finra.jtaf.core.model.exceptions.MissingInvocationTargetException;
import org.finra.jtaf.core.model.invocationtarget.InvocationTarget;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.test.TestScript;

/**
 * The interpreter pushes and pops Invocation Context frames during execution
 */
public interface IInterpreterContext extends IInvocationContext {

    /**
     * Prepares the context for the invocation. In most cases, this means
     * building the parameter set, filtering unwanted parameters, and marking
     * the productions.
     * <p/>
     * Throws MissingInvocationTargetException if no target exists for the
     * invocation.
     *
     * @param inv
     * @throws MissingInvocationTargetException
     */
    void pushInvocation(Invocation inv, InvocationTarget target)
            throws MissingInvocationTargetException;

    /**
     * Used by ReplaceContext to set the values within the Context. This does
     * not impact the Productions
     *
     * @param vars
     */
    void setParameters(Map<String, ? extends Object> vars);

    /**
     * Pops the current frame. Anything that was listed in the set of
     * productions will be moved into the previous frame.
     */
    void popFrame();

    /**
     * Sets the TestScript that is being executed.
     *
     * @param ts
     */
    void setTestScript(TestScript ts);
}
