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
package org.finra.jtaf.core;


/**
 * Implementation of the {@link IAutomationClassLoader}
 */
public class DefaultAutomationClassLoader implements IAutomationClassLoader {

    /* (non-Javadoc)
     * @see org.finra.jtaf.core.IAutomationClassLoader#loadClass(java.lang.String)
     */
    @Override
    public Class<?> loadClass(String commandClass) throws ClassNotFoundException {

        try {
            return Class.forName(commandClass);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Unable to Load command class " + commandClass, e);
        } catch (NoClassDefFoundError e1) {
            throw new NoClassDefFoundError("Unable to Load command class " + commandClass + "Exception " + e1);
        }
    }

}
