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
package org.finra.jtaf.core.commands.context;

import java.util.List;
import java.util.Map;

import org.finra.jtaf.core.model.execution.IInvocationContext;


/**
 * Helper class used for context commands
 * <p/>
 * plan for context manipulation:
 * <p/>
 * action = {replace out with in|prepend in to out|append in to out}
 * <p/>
 * valuein
 * indexin|keyin|null
 * foreach = {value in list|key in map|value in map|int|null}
 * change = {expand to singleton list|collapse list to set|expand set to map keys|convert map keys to set|convert set to list|convert map values to list}
 * <p/>
 * valueout
 * indexout|keyout|null
 * foreach = {value in list|key in map|value in map|int|null}
 * change = {collapse list to set|expand set to map keys|convert map keys to set|convert set to list|convert map values to list
 */
public class ManipulateContextHelper {
    public static final String ACTION_ATTRIBUTE = "action";
    public static final String REPLACE_ACTION = "replace";
    public static final String PREPEND_ACTION = "prepend";
    public static final String APPEND_ACTION = "append";
    public static final String VALUE_IN_ATTRIBUTE = "valuein";
    public static final String INDEX_IN_ATTRIBUTE = "indexin";
    public static final String KEY_IN_ATTRIBUTE = "keyin";
    public static final String CHANGE_IN_ATTRIBUTE = "changein";
    public static final String SINGLETON_CHANGE = "singleton";
    public static final String VALUE_OUT_ATTRIBUTE = "valueout";
    public static final String INDEX_OUT_ATTRIBUTE = "indexout";
    public static final String KEY_OUT_ATTRIBUTE = "keyout";
    public static final String NONE_CHANGE = "none";
    private AbstractContextCmd cmd;
    private IInvocationContext ctx;
    private Map<?, ?> globalCtx;

    public ManipulateContextHelper(AbstractContextCmd cmd, Map<?, ?> globalCtx) {
        this.cmd = cmd;
        this.ctx = null;
        this.globalCtx = globalCtx;
    }

    private String name() {
        return cmd.getName();
    }

    private IInvocationContext ctx() {
        if (ctx != null) {
            return ctx;
        }
        return cmd.ctx();
    }

    /**
     * Sets a context to use instead of the global context provided in the constructor
     *
     * @param ctx The context to use
     */
    public void setContext(IInvocationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Checks to make sure the current "action" in context is an acceptable value
     *
     * @return
     */
    public String getVerifiedAction() {
        String action = getStringOrDefault(ACTION_ATTRIBUTE, REPLACE_ACTION);
        if (!(action.equalsIgnoreCase(REPLACE_ACTION) || action.equalsIgnoreCase(APPEND_ACTION) || action.equalsIgnoreCase(PREPEND_ACTION))) {
            throw new IllegalArgumentException(this.name() + ": attribute '" + ACTION_ATTRIBUTE + "' must be '" + REPLACE_ACTION + "' or '" + APPEND_ACTION + "' or '" + PREPEND_ACTION + "' (or left unspecified)");
        }
        return action;
    }

    /**
     * Returns the values stored in an object in the context
     *
     * @param valueInAttribute The location of the object in the context
     * @param index            The index, if valueInAttribute refers to a list
     * @param key              The key if valueInAttribute refers to a map
     * @return
     */
    public Object getValueIn(String valueInAttribute, Integer index, Object key) {
        if (index != null) {
            return getValueIn(valueInAttribute, index);
        } else if (key != null) {
            return getValueIn(valueInAttribute, key);
        } else {
            return getOptionalObject(valueInAttribute);
        }

    }

    /**
     * Returns the value stored in a map in the context
     *
     * @param valueInAttribute The location of the map in the context
     * @param key              the key in the map stored in the context where to retrieve the value
     * @return
     */
    public Object getValueIn(String valueInAttribute, Object key) {
        return ((Map<?, ?>) getOptionalObject(valueInAttribute)).get(key);
    }

    /**
     * Returns the value stored in a list in the context
     *
     * @param valueInAttribute The location of the list in the context
     * @param index            The index in the list stored in the context from where to retrieve the value
     * @return
     */
    public Object getValueIn(String valueInAttribute, Integer index) {
        List<?> list = (List<?>) getOptionalObject(valueInAttribute);
        if (index < 0) {
            index += list.size();
        }
        return list.get(index);
    }

    /**
     * Stores a value in a list in the context
     *
     * @param valueOutAttribute Where to store the resulting list in the context
     * @param list              The list in which to store the value
     * @param value             The value to store in the list
     * @param index             The index in the list in which to store the value
     */
    public void setValueOut(String valueOutAttribute, List<Object> list, Object value, int index) {
        list.add(index, value);
        ctx().putObject(valueOutAttribute, list);
    }

    /**
     * Appends an object to a list stored in the context
     *
     * @param valueOutAttribute Where to store the resulting list in the context
     * @param list              The list in which to store the value
     * @param value             The value to store in the list
     */
    public void setValueOut(String valueOutAttribute, List<Object> list, Object value) {
        list.add(value);
        ctx().putObject(valueOutAttribute, list);
    }

    /**
     * Appends all the values of a list into another list that is stored in the context
     *
     * @param valueOutAttribute Where to store the resulting list in the context
     * @param list              The list in which to store the values
     * @param value             The list of values being stored
     */
    public <T> void setValueOut(String valueOutAttribute, List<T> list, List<T> value) {
        list.addAll(value);
        ctx().putObject(valueOutAttribute, list);
    }

    /**
     * Stores all the values of a list into another list that is stored in the context
     *
     * @param valueOutAttribute Where to store the resulting list in the context
     * @param list              The list in which to store the values
     * @param value             The list of values being stored
     * @param index             The index in the list in which to store the value
     */
    public <T> void setValueOut(String valueOutAttribute, List<T> list, List<T> value, int index) {
        list.addAll(index, value);
        ctx().putObject(valueOutAttribute, list);
    }

    /**
     * Stores a value in a map stored in the context
     *
     * @param valueOutAttribute Where to store the resulting map in the context
     * @param list              The map in which to store the values
     * @param value             The value being stored in the map
     * @param key               The key at which to store the value in the map
     */
    public <T, K> void setValueOut(String valueOutAttribute, Map<? super K, ? super T> list, T value, K key) {
        list.put(key, value);
        ctx().putObject(valueOutAttribute, list);
    }

    /**
     * Stored an object in the context
     *
     * @param valueOutAttribute Where to store the object in the context
     * @param value             The object to store in the context
     */
    public void setValueOut(String valueOutAttribute, Object value) {
        ctx().putObject(valueOutAttribute, value);
    }


    /**
     * Grabs an Object from the Context.
     *
     * @param key
     * @return
     */
    public Object getOptionalObject(String key) {
        // We cannot assume that a parameter is optional just because getOptionalObject was
        // called. Most commands failed to distinguish between optional and required parameters.

        Object result = ctx().getObject(key);

        // process 'contextKey' parameter
        if ((result != null) && (result.getClass().equals(String.class))) {
            String[] resultSplitted = ((String) result).split("\\$contextKey");
            StringBuffer resultString = new StringBuffer(resultSplitted[0]);

            int i = 1;
            while (resultSplitted.length > i) {
                int beginIndex = resultSplitted[i].indexOf("(");
                if (beginIndex >= 0) {
                    int endIndex = resultSplitted[i].indexOf(")", beginIndex);
                    if (endIndex > 0) {
                        String runTimeKeyName = resultSplitted[i].substring(beginIndex + 1, endIndex);
                        runTimeKeyName = runTimeKeyName.replaceAll("'", "").replaceAll("\"", "");

                        if (globalCtx.get(runTimeKeyName) != null) {
                            resultString.append(globalCtx.get(runTimeKeyName) + resultSplitted[i].substring(endIndex + 1));
                        } else {
                            throw new RuntimeException("Oops! Processing '" + key + "'='" + result + "' key fails! Can't find run time parameter '" + runTimeKeyName
                                    + "'! You have put it to global content before...");
                        }
                    }
                }
                i++;
            }
            return resultString.toString();
        }
        return result;
    }


    /**
     * Grabs an object from the Context, or throws an exception
     * if the Object does not exist.
     *
     * @param key
     * @return
     */
    public Object getRequiredObject(String key) {
        Object param = this.getOptionalObject(key);
        if (param == null) {
            throw new IllegalArgumentException(this.name() + " : The parameter with key [" + key + "] is not set.");
        }
        return param;
    }


    /**
     * Returns an integer value, or throws a NullPointerException if the value is not present
     *
     * @param attributeName
     * @param ctx
     * @return
     */
    public int getRequiredInteger(String attributeName) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            throw new NullPointerException(this.name() + ": missing required integer attribute '" + attributeName + "'");
        }
        return Integer.parseInt(value);
    }


    /**
     * Like the above, except it returns a default value if the attribute is not set
     *
     * @param attributeName
     * @param ctx
     * @param defaultValue
     * @return
     */
    public int getIntegerOrDefault(String attributeName, int defaultValue) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * Returns a float stored in the context or throws a NullPointerException is one isn't
     *
     * @param attributeName Where in the context the float is stored
     * @return The float stored in attributeName
     */
    public float getRequiredFloat(String attributeName) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            throw new NullPointerException(this.name() + ": missing required float attribute '" + attributeName + "'");
        }
        return Float.parseFloat(value);
    }

    /**
     * Like the above, except it returns a default value if the attribute is not set
     *
     * @param attributeName Where in the context the float is stored
     * @param defaultValue  The value to return if the context does not contain a value at attributeName
     * @return The float stored in attributeName or defaultValue if one wasn't present
     */
    public float getFloatOrDefault(String attributeName, float defaultValue) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            return defaultValue;
        }
        return Float.parseFloat(value);
    }


    /**
     * Returns a String stored in the context or throws a NullPointerException is one isn't
     *
     * @param attributeName Where in the context the String is stored
     * @return The String stored in attributeName
     */
    public String getRequiredString(String attributeName) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            throw new NullPointerException(this.name() + ": missing required String attribute '" + attributeName + "'");
        }
        return value;
    }


    /**
     * Like the above, except it returns a default value if the attribute is not set
     *
     * @param attributeName attributeName Where in the context the String is stored
     * @param defaultValue  The value to return if the context does not contain a value at attributeName
     * @return The String stored in attributeName or defaultValue if one wasn't present
     */
    public String getStringOrDefault(String attributeName, String defaultValue) {
        String value = (String) this.getOptionalObject(attributeName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Returns a String stored in the context or null if there isn't one
     *
     * @param attributeName Where in the context the String is stored
     * @return The string stored in attributeName or null if there isn't one
     */
    public String getOptionalString(String attributeName) {
        return (String) this.getOptionalObject(attributeName);
    }

    /**
     * Returns a float stored in the context or throws a NullPointerException is one isn't
     *
     * @param attributeName Where in the context the boolean is stored
     * @return The boolean stored in attributeName
     */
    public boolean getRequiredBoolean(String attributeName) {
        String temp = (String) this.getOptionalObject(attributeName);
        if (temp == null) {
            throw new NullPointerException(this.name() + ": missing required boolean attribute '" + attributeName + "'");
        }

        temp = temp.trim();
        if (temp.equalsIgnoreCase("true")) {
            return true;
        } else if (temp.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(this.name() + ": attribute '" + attributeName + "' must be either 'true' or 'false'");
        }
    }

    /**
     * Like the above, except it returns a default value if the attribute is not set
     *
     * @param attributeName Where in the context the boolean is stored
     * @param defaultValue  The value to return if the context does not contain a value at attributeName
     * @return The boolean stored in attributeName or defaultValue if one wasn't present
     */
    public boolean getBooleanOrDefault(String attributeName, boolean defaultValue) {
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
            throw new IllegalArgumentException(this.name() + ": attribute '" + attributeName + "' must be either 'true' or 'false' (or left unspecified)");
        }
    }

    /**
     * Returns an Integer stored in the context or null if there isn't one
     *
     * @param key Where in the context the Integer is stored
     * @return The string stored in key or null if there isn't one
     */
    public Integer getOptionalInteger(String key) {
        String integerAsString = (String) getOptionalObject(key);
        if (integerAsString == null) {
            return null;
        } else {
            return Integer.parseInt(integerAsString);
        }
    }

}
