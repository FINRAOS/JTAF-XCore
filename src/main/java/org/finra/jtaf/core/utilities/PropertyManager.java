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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;

/**
 * This class is similar to java Properties reader. It takes care of reading properties from the specified file.
 * The difference however is, it allows to override any property by specifying it as a System
 * property.
 */
public class PropertyManager {

    protected String fileName;
    protected String propertyPrefix;
    protected PropertyResourceBundle propertyResourceBundle;

    public PropertyManager(String fileName, String propertyPrefix) {
        this.fileName = fileName;
        this.propertyPrefix = propertyPrefix;
        propertyResourceBundle = getPropertyResourceBundle();
        if (propertyResourceBundle != null) {
            loadProperties();
        }
    }

    public String getProperty(String propertyName) {
        return System.getProperty(propertyPrefix + "." + propertyName);
    }

    protected PropertyResourceBundle getPropertyResourceBundle() {
        try {
            InputStream inputStream = PropertyManager.class.getClassLoader()
                    .getResourceAsStream(fileName);
            if (inputStream == null) {
                File propertiesFile = new File(fileName);
                inputStream = new FileInputStream(propertiesFile);
            }
            return new PropertyResourceBundle(inputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            return null;
        } catch (Exception exception) {
            throw new RuntimeException("Problem loading properties", exception);
        }
    }

    protected void loadProperties() {
        Enumeration<String> keys = propertyResourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String newProperty = propertyResourceBundle.getString(key);
            String newKey = propertyPrefix + "." + key;
            if (System.getProperty(newKey) == null) {
                System.setProperty(newKey, newProperty);
            }
        }
    }
}
