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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.finra.jtaf.core.model.execution.IInvocationContext;

/**
 * Used to replace keys in the context.
 * 
 */
public class ContextKeyHandler {

    private IInvocationContext context;
    private static final int MAX_NUMBER_OF_RESOLVES = 200;
    private int resolveCount = 0;

    /**
     * Called when instantiated
     * 
     * @param context
     *            - the current context when this is executed.
     */
    public ContextKeyHandler(IInvocationContext context) {
        this.context = context;
    }

    /**
     * 
     * @param value - the key 
     * @return -
     */
    @SuppressWarnings("unchecked")
	public Object replaceContextKey(Object value) {
        // This is to avoid replaceContext from getting into an infinite loop of
        // recursive calls.
        resolveCount++;
        if (resolveCount > MAX_NUMBER_OF_RESOLVES)
            return value;

        if ((value != null) && (value.getClass().equals(String.class))) {
            String[] resultSplitted = ((String) value).split("\\$contextKey");
            StringBuffer resultString = new StringBuffer(resultSplitted[0]);

            int i = 1;
            while (resultSplitted.length > i) {
                int beginIndex = resultSplitted[i].indexOf("(");
                if (beginIndex >= 0) {
                    int endIndex = resultSplitted[i].indexOf(")", beginIndex);
                    if (endIndex > 0) {
                        String runTimeKeyName = resultSplitted[i].substring(beginIndex + 1,
                                endIndex);
                        runTimeKeyName = runTimeKeyName.replaceAll("'", "").replaceAll("\"", "");

                        if (getContext().getObject(runTimeKeyName) != null) {
                            resultString.append(getContext().getObject(runTimeKeyName)
                                    + resultSplitted[i].substring(endIndex + 1));
                        } else if (InvocationTarget.getGlobalContext().get(runTimeKeyName) != null) {
                            resultString.append(InvocationTarget.getGlobalContext().get(
                                    runTimeKeyName)
                                    + resultSplitted[i].substring(endIndex + 1));
                        } else {
                            throw new ContextKeyNotFoundException("Oops! Processing '" + value
                                    + "' key fails! Can't find run time parameter '"
                                    + runTimeKeyName
                                    + "'! You have put it to global content before...");
                        }
                    }
                }
                i++;
            }
            if (resultString.toString().toLowerCase().indexOf("$contextkey") != -1) {
                return replaceContextKey(resultString.toString());
            }
            return resultString.toString();

        } else if ((value != null) && (value instanceof List<?>)) {
            List<Object> list = (List<Object>) value;
            ArrayList<Object> resultList = new ArrayList<Object>();
            for (Object o : list) {
                resultList.add(replaceContextKey(o));
            }
            return resultList;
        } else if ((value != null) && (value instanceof Map<?, ?>)) {
            Map<String, Object> map = (Map<String, Object>) value;
            HashMap<String, Object> resultMap = new HashMap<String, Object>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                resultMap.put((String) replaceContextKey(entry.getKey()), replaceContextKey(entry.getValue()));
            }
            return resultMap;
        }
        return value;
    }

    /**
     * Get the current context.
     * 
     * @return the current context.
     */
    public IInvocationContext getContext() {
        return context;
    }
}
