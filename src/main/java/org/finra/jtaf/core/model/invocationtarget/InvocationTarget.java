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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.Interpreter;

/**
 * Common base class for any form of invocation target.
 * 
 * TODO: Throw an exception if a parameter has already been defined?
 * 
 * 
 */
public abstract class InvocationTarget {
    // TODO: Come to a consensus on what should be a valid name, potentially
    // matching TestComponent
    private static final Pattern VALID_NAME = Pattern.compile("^\\w((\\w|\\s)*\\w)?$");

    private final String name;
    private String usage;

    private final Set<String> requiredParameters;
    private final Set<String> optionalParameters;
    // TODO: Determine if allParameters is really better as a separately managed
    // set.
    private final Set<String> allParameters; // union of required and optional
                                             // parameters
    private final Set<String> productions;

    static Map<String, Object> globalContext = new HashMap<String, Object>();

    protected static Map<String, Object> getGlobalContext() {
        return globalContext;
    }

    /**
     * Used to set global context to be used by the command.
     * 
     * @param contextGlobal
     *            - The map to be used as the global context.
     */
    protected static void setGlobalContext(Map<String, Object> contextGlobal) {
        Command.globalContext = contextGlobal;
    }

    /**
     * Used to add values to the global context of the command.
     * 
     * @param key
     *            - the key to be used in the map.
     * @param value
     *            - the value to be stored in the context.
     */
    protected static void putToGlobalContext(String key, Object value) {
        Command.globalContext.put(key, value);
    }

    /**
     * Used to get an object from the global context that is associated with the
     * passed in key.
     * 
     * @param key
     *            - the key associated with the value in the global context
     * @return the value in the context.
     */
    public static Object getFromGlobalContext(String key) {
        return globalContext.get(key);
    }

    /**
     * Used to clear all values from the global context.
     */
    protected static void cleanGlobalContext() {
        globalContext.clear();
    }

    /**
     * This is called when the InvocationTarget is instantiated by the
     * interpreter. All sets used to keep track of the invocation targets
     * parameters are instantiated as well.
     * 
     * @param name
     *            - name of the invocation target.
     * @throws NameFormatException
     */
    public InvocationTarget(String name) throws NameFormatException {
        if (!InvocationTarget.VALID_NAME.matcher(name).matches()) {
            throw new NameFormatException(name, InvocationTarget.VALID_NAME);
        }
        this.name = name;
        this.usage = "";

        this.requiredParameters = new HashSet<String>();
        this.optionalParameters = new HashSet<String>();
        this.allParameters = new HashSet<String>();
        this.productions = new HashSet<String>();
    }

    /**
     * Get the name of this InvocationTarget
     * 
     * @return the name of the target.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Get the usage of this InvocationTarget
     * 
     * @return the usage of the target.
     */
    public final String getUsage() {
        return this.usage;
    }

    /**
     * Set the usage of this InvocationTarget
     * 
     * @param usage
     *            - the new value for this InvocationTargets usage
     */
    public final void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Get all of the required parameters of this InvocationTarget
     * 
     * @return A read-only set of required parameters
     */
    public final Set<String> getRequiredParameters() {
        return Collections.unmodifiableSet(this.requiredParameters);
    }

    /**
     * Add a new value as a required parameter.
     * 
     * @param name
     *            - the name to be used for this required parameter.
     */
    public final void addRequiredParameter(String name) {
        name = name.toLowerCase();
        this.requiredParameters.add(name);
        this.allParameters.add(name);
    }

    /**
     * Get all of the registered optional parameters of this InvocationTarget.
     * 
     * @return the set containing all of the optional parameters.
     */
    public final Set<String> getOptionalParameters() {
        return Collections.unmodifiableSet(this.optionalParameters);
    }

    /**
     * Add a new value as an optional parameter.
     * 
     * @param name
     *            - the name to be used for this optional parameter.
     */
    public final void addOptionalParameter(String name) {
        name = name.toLowerCase();
        this.optionalParameters.add(name);
        this.allParameters.add(name);
    }

    /**
     * Get all of the parameters of this InvocationTarget.
     * 
     * @return the set containing all of the parameters.
     */
    public final Set<String> getAllParameters() {
        return Collections.unmodifiableSet(this.allParameters);
    }

    /**
     * Get all of the productions of this InvocationTarget.
     * 
     * @return the set containing all of the productions.
     */
    public final Set<String> getProductions() {
        return Collections.unmodifiableSet(this.productions);
    }

    /**
     * Add a new value to the production set.
     * 
     * @param name
     *            - the name to be used for this production.
     */
    public final void addProduction(String name) {
        this.productions.add(name.toLowerCase());
    }

    /**
     * This is used by the interpreter to either execute the command or the function.
     * @param v - The interpreter that is executing this InvocationTarget.
     * @throws Throwable
     */
    public abstract void acceptInvocationTargetVisitor(Interpreter v) throws Throwable;
}
