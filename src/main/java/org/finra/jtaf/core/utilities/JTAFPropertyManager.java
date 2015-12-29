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
package org.finra.jtaf.core.utilities;


/**
 * Responsible for reading properties configured in jtaf.properties file.
 */
public class JTAFPropertyManager extends PropertyManager {
    private static JTAFPropertyManager instance;

    public static final String JTAF_PROPERTIES_FILE_NAME = "jtaf.properties";
    public static final String PROPERTY_PREFIX = "jtaf";

    private JTAFPropertyManager() {
        super(JTAF_PROPERTIES_FILE_NAME, PROPERTY_PREFIX);
    }

    public static JTAFPropertyManager getInstance() {
        if (instance == null) {
            instance = new JTAFPropertyManager();
        }
        return instance;
    }
}
