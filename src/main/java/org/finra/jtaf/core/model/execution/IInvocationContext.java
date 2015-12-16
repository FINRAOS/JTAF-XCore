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

import org.finra.jtaf.core.model.execution.exceptions.UndefinedParameterError;
import org.finra.jtaf.core.model.execution.exceptions.UndefinedProductionError;
import org.finra.jtaf.core.model.test.TestScript;

/**
 * InvocationTargets can access objects currently inside the context. They are not allowed to push or pop frames.
 */
public interface IInvocationContext {

    /**
     * Returns an Object associated w/ the given name, or null if no such Object
     * is available in the current Context
     *
     * @param name
     * @return
     * @throws UndefinedParameterError
     */
    Object getObject(String name) throws UndefinedParameterError;

    /**
     * Returns map<name, value> of all objects in the current Context
     *
     * @return
     */
    Map<String, Object> getAllObjects();

    /**
     * Inserts the specified Object into the current Context.
     *
     * @param name
     * @param value
     */
    void putObject(String name, Object value) throws UndefinedProductionError;

    /**
     * TODO: This should probably throw some type of exception if the name is
     * not specified in the script contract
     *
     * @param name
     * @return True if the given name is associated w/ an Object; False
     * otherwise
     */
    boolean contains(String name);

    /**
     * Removes the object w/ the given name TODO: This should probably throw
     * some kind of exception if the name is not specified in the script
     * contract
     *
     * @param name
     */
    void removeObject(String name);

    /**
     * @return The TestScript that is being executed.
     */
    TestScript getTestScript();

}
