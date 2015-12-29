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

/**
 * This is an exception thrown when a key, that hasn't been stored in the
 * context, is used to get a value from the context.
 */
public class ContextKeyNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * A constructor used without specifying the message or throwable.
     */
    public ContextKeyNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * A constructor used with specifying the message or throwable.
     *
     * @param message - The message to be thrown.
     * @param cause   - The exception encountered that will be thrown.
     */
    public ContextKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * A constructor used without specifying the message.
     *
     * @param message - The message to be thrown.
     */
    public ContextKeyNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * A constructor used without specifying the throwable.
     *
     * @param cause - The exception encountered that will be thrown.
     */
    public ContextKeyNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
