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

import org.apache.log4j.Logger;
import org.finra.jtaf.core.commands.RandomGenerator;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.CorrectiveContext;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.test.TestStepsDetails;

/**
 * Implements basic methods, but leaves execution phase abstract
 * 
 */
public abstract class Command extends InvocationTarget {
    private static Logger logger = Logger.getLogger(Command.class.getPackage().getName());

    // Store an internal copy of the Context so it does not need to
    // be passed between methods explicitly.
    private IInvocationContext context;
    protected boolean recordResult = true;

    public Interpreter interpreter;

    public Command(String name) throws NameFormatException {
        super(name);
        this.context = null;
    }

    /**
     * Internal method: Used to share the current Context between methods
     * without having to pass it explicitly. The current Context is set
     * automatically when launch() is called.
     * 
     * @return the context of this method
     */
    protected final IInvocationContext getContext() {
        return this.context;
    }

    /**
     * Sets up the context before executing the actual command. Also handles the
     * updating of the TestStepDetails to record information on step execution.
     * 
     * @param ctx
     *            - the current context when the command is being executed
     * @param interpreter
     *            - the interpreter that is executing the command
     * @throws Throwable
     */
    public final void launch(IInvocationContext ctx, Interpreter interpreter) throws Throwable {
        if (ctx.getObject("expectFailureBlockFlag") == null) {
            ctx.putObject("expectFailureBlockFlag", false);
        }

        if (ctx.getObject("ignoreErrorsBlockFlag") == null) {
            ctx.putObject("ignoreErrorsBlockFlag", false);

        }
        this.context = ctx;
        this.interpreter = interpreter;
        TestStepsDetails step = new TestStepsDetails(getName(), getUsage());

        try {
            this.initialize(this.getContext());
            boolean expectFailureFlag = false;
            boolean ignoreErrorsFlag = false;
            expectFailureFlag = (Boolean) this.context.getObject("expectFailureBlockFlag");

            ignoreErrorsFlag = (Boolean) this.context.getObject("ignoreErrorsBlockFlag");

            if (expectFailureFlag) {

                step.setExpectedResult(false);
                step.setType("expectFailure");
            } else if (ignoreErrorsFlag) {
                step.setType("ignoreErrors");
            }

            this.execute(this.getContext());

            if (recordResult) {
                step.setActualResult(true);
                logger.debug("Running Command '" + getName() + "'");
                logger.debug("Updating Command '" + step.getName() + "'");
                interpreter.addDetails(step);

            }
        } catch (Throwable th) {
            if (recordResult) {
                step.setActualResult(false);
                interpreter.addDetails(step);
            }
            throw th;

        } finally {
        	
            try {
            	this.deinitialize(this.getContext());
            } catch (Throwable th) {
            	logger.error("Error in Command deinitialization", th);
            }

        }
    }

    /**
     * Optional method to initialize a command before execution
     * 
     * @param ctx
     */
    protected void initialize(IInvocationContext ctx) throws Throwable {
        // DO NOTHING
    }

    /**
     * This method should be overridden by every Command object.
     * 
     * @param ctx
     * @throws Exception
     */
    protected abstract void execute(IInvocationContext ctx) throws Throwable;

    /**
     * Optional method to deinitialize a command after execution.
     * 
     * @param ctx
     */
    protected void deinitialize(IInvocationContext ctx) throws Throwable {
        // DO NOTHING
    }

    /**
     * Grabs an Object from the Context.
     * 
     * @param key
     * @return
     */
    protected Object getOptionalObject(String key) {
        // TODO: discuss comment (and remove?)
        // We cannot assume that a parameter is optional just because
        // getOptionalObject was
        // called. Most commands failed to distinguish between optional and
        // required parameters.

        Object result = getContext().getObject(key);

        // process 'contextKey' parameter
        result = replaceContextKey(result);

        // process '$randomGenerator' command parameter
        if(result instanceof String && result.toString().toLowerCase().startsWith("$randomgenerator")) {
            result = processRandomGenerator(result.toString());
        }

        return result;
    }

    /**
     * Processing the paramters for RandomGenerator before execution.
     * @param params the paramters of the RandomGenerator instance.
     * @return the randomly generated string.
     */
    private Object processRandomGenerator(String params) {
        params = params.substring(params.indexOf("(") + "(".length(), params.indexOf(")"));
        String[] paramsSplitted = params.split("[ ,]");
        String method = null;
        String length = null;
        String lengthMin = null;
        String lengthMax = null;
        String min = null;
        String max = null;
        String regexp = null;
        String saveToGlobalContextAs = null;
        for (String paramsSplittedCurrent : paramsSplitted) {
            String[] paramsCurrent = paramsSplittedCurrent.split("=");
            if (paramsCurrent.length == 2) {
                if (paramsCurrent[0].equalsIgnoreCase("method")) {
                    method = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("length")) {
                    length = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("lengthMin")) {
                    lengthMin = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("lengthMax")) {
                    lengthMax = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("min")) {
                    min = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("max")) {
                    max = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("regexp")) {
                    regexp = paramsCurrent[1].replace("'", "");
                } else if (paramsCurrent[0].equalsIgnoreCase("saveToGlobalContextAs")) {
                    saveToGlobalContextAs = paramsCurrent[1].replace("'", "");
                } else {
                    logger.fatal("Oops! Can't parse '$randomGenerator' command parameters. Unknown parameter '"
                            + paramsCurrent[0]
                            + "'! This command supports these parameters: String method, String length, String lengthMin, String lengthMax, String min, String max, String regexp, String saveToGlobalContextAs. Example: $randomGenerator(method='string' length='10'). Fix your testscript, please!");
                }
            } else {
                logger.fatal("Oops! Can't parse '$randomGenerator' command parameters. This command supports these parameters: String method, String length, String lengthMin, String lengthMax, String min, String max, String regexp. Example: $randomGenerator(method='string' length='10'). Fix your testscript, please!");
            }
        }

        String result = RandomGenerator.generate(method, length, lengthMin, lengthMax, min, max,
                regexp);

        if (saveToGlobalContextAs != null && saveToGlobalContextAs.length() > 0) {
            putToGlobalContext(saveToGlobalContextAs, result);
        }

        return result;
    }

    /**
     * Replace a key of an object in the context.
     * 
     * @param result
     *            - the new key to be used to replace
     * @return
     */
    // TODO: add $ escape
    private Object replaceContextKey(Object result) {
        ContextKeyHandler contextKeyHandler = new ContextKeyHandler(getContext());
        return contextKeyHandler.replaceContextKey(result);
    }

    /**
     * Get an object that was stored in the context as an required parameter.
     * 
     * @param key
     *            - key of object in context
     * @return the object value
     */
    protected Object getRequiredObject(String key) {
        Object param = this.getOptionalObject(key);
        if (param == null) {
            throw new IllegalArgumentException(getName() + " : The parameter with key [" + key
                    + "] is not set.");
        }
        return param;
    }

    /**
     * Get an integer that was stored in the context as an required parameter.
     * 
     * @param attributeName
     *            - key of object in context
     * @return the integer value
     */
    protected final int getRequiredInteger(String attributeName) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            throw new NullPointerException(this.getName()
                    + ": missing required integer attribute '" + attributeName + "'");
        }
        return Integer.parseInt(value);
    }

    /**
     * Get an integer stored in the context or return a default value.
     * 
     * @param attributeName
     *            - key of object in context
     * @param defaultValue
     *            - the default value if the attributeName has no associated
     *            value in the context.
     * @return the default value or the value from the context.
     */
    protected final int getIntegerOrDefault(String attributeName, int defaultValue) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * Get a float that was stored in the context as an required parameter.
     * 
     * @param attributeName
     *            - key of object in context
     * @return the float value
     */
    protected final float getRequiredFloat(String attributeName) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            throw new NullPointerException(this.getName() + ": missing required float attribute '"
                    + attributeName + "'");
        }
        return Float.parseFloat(value);
    }

    /**
     * Get a float stored in the context or return a default value.
     * 
     * @param attributeName
     *            - key of object in context
     * @param defaultValue
     *            - the default value if the attributeName has no associated
     *            value in the context.
     * @return the default value or the value from the context.
     */
    protected final float getFloatOrDefault(String attributeName, float defaultValue) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            return defaultValue;
        }
        return Float.parseFloat(value);
    }

    /**
     * Get a string that was stored in the context as an required parameter.
     * 
     * @param attributeName
     *            - key of object in context
     * @return the string value
     */
    protected final String getRequiredString(String attributeName) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {

            throw new NullPointerException(this.getName() + ": missing required String attribute '"
                    + attributeName + "'");
        }
        return value;
    }

    /**
     * Get a string stored in the context or return a default value.
     * 
     * @param attributeName
     *            - key of object in context
     * @param defaultValue
     *            - the default value if the attributeName has no associated
     *            value in the context.
     * @return the default value or the value from the context.
     */
    protected final String getStringOrDefault(String attributeName, String defaultValue) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Get a string that was stored in the context as an optional parameter.
     * 
     * @param attributeName
     *            - key of object in context
     * @return the string value
     */
    protected final String getOptionalString(String attributeName) {
        return (String) this.getOptionalObject(attributeName);
    }

    /**
     * Get a boolean that was stored in the context as a required parameter.
     * 
     * @param attributeName
     *            - key of object in context
     * @return the boolean value
     */
    protected final boolean getRequiredBoolean(String attributeName) {
        String temp = (String) this.getOptionalObject(attributeName);
        if (temp == null) {
            throw new NullPointerException(this.getName()
                    + ": missing required boolean attribute '" + attributeName + "'");
        }

        temp = temp.trim();
        if (temp.equalsIgnoreCase("true")) {
            return true;
        } else if (temp.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(this.getName() + ": attribute '" + attributeName
                    + "' must be either 'true' or 'false'");
        }
    }

    /**
     * Get a boolean stored in the context or return a default value.
     * 
     * @param attributeName
     *            - key of object in context
     * @param defaultValue
     *            - the default value if the attributeName has no associated
     *            value in the context.
     * @return the default value or the value from the context.
     */
    protected final boolean getBooleanOrDefault(String attributeName, boolean defaultValue) {
        String temp = (String) this.getOptionalObject(attributeName);
        if (temp == null) {
            return defaultValue;
        }

        temp = temp.trim();
        if (temp.equalsIgnoreCase("true")) {
            return true;
        } else if (temp.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(this.getName() + ": attribute '" + attributeName
                    + "' must be either 'true' or 'false' (or left unspecified)");
        }
    }

    /**
     * This is used by the interpreter so that the visitCommand method is
     * called, allowing the interpreter to not have to know what kind of
     * InvocationTarget it is executing.
     * 
     * @param v
     *            - the interpreter executing this command
     */
    public final void acceptInvocationTargetVisitor(Interpreter v) throws Throwable {
        v.visitCommand(this);
    }

    /**
     * Used to check whether or not this command needs to have its details
     * recorded in testStepDetails.
     * 
     * @return
     */
    protected boolean recordResult() {
        return recordResult;
    }

    /**
     * Used by other commands to execute invocations in their bodies.
     * 
     * @param child
     *            - the command in the block
     * @throws Throwable
     */
    protected void executeInvocation(Invocation child) throws Throwable {

        interpreter.visitInvocation(child);
    }

    /**
     * Used to clear the context.
     */
    public void clearContext() {
        ((CorrectiveContext) context).clearContext();
    }

}
