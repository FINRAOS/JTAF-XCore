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
 * This is used when an InvocationTarget's acceptInvocationTargetVisitor method
 * is called by the interpreter.
 * 
 */
public interface IInvocationTargetVisitor {

    /**
     * Operates on the Function object
     * 
     * @param c
     *            - The function to be executed
     * @throws Throwable
     */
    void visitFunction(Function c) throws Throwable;

    /**
     * Operates on a Command object
     * 
     * @param abstractCommand
     *            - The command to be executed
     * @throws Throwable
     */
    void visitCommand(Command abstractCommand) throws Throwable;
}
