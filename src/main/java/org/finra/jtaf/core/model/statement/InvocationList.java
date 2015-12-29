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
package org.finra.jtaf.core.model.statement;

import java.util.ArrayList;

import org.finra.jtaf.core.model.IScriptVisitor;

/**
 * This is a object to manage an ordered list of IStatement nodes
 */
public class InvocationList extends ArrayList<Invocation> {
    private static final long serialVersionUID = -7720009887819987235L;

    /***
     * Accept an automation node visitor
     *
     * @param v - IScriptVisitor object of any type
     * @throws Throwable
     */
    public void acceptAutomationNodeVisitor(IScriptVisitor v) throws Throwable {
        v.visitStatementList(this);
    }
}