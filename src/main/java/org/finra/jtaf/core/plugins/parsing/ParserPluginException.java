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

package org.finra.jtaf.core.plugins.parsing;

/***
 * This exception is thrown by the parser plugins
 */
public class ParserPluginException extends Exception {

    // automatically generated
    private static final long serialVersionUID = -195286095131824362L;

    public ParserPluginException() {
        super();
    }

    public ParserPluginException(String message) {
        super(message);
    }

    public ParserPluginException(Throwable throwable) {
        super(throwable);
    }

    public ParserPluginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
