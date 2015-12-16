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
 * This exception is thrown by the interpreter when no exception is thrown within an
 * <expectFailure> block.  This allows us to reverse the normal
 * logic of our testing and verify that our validation commands
 * are doing what they are supposed to do.
 */
public class ExpectedFailureException extends ModelException {


    private static final long serialVersionUID = -8114949982502430018L;

    public ExpectedFailureException() {
        super("Expected this script to fail, but it did not");
    }
}
