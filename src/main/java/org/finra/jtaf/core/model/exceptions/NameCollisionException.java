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
package org.finra.jtaf.core.model.exceptions;

/**
 * This is thrown whenever an element with the same name
 * already exists
 */
public class NameCollisionException extends Exception {


    private static final long serialVersionUID = -3361740674217851439L;

    private final String name;

    /**
     * @param name This is called when same test name exist during building model
     */
    public NameCollisionException(String name) {
        super("The name '" + name + "' is already in use");
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
