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

import org.finra.jtaf.core.model.statement.Invocation;


/**
 * This will be used by the Interpreter to report broken invocation
 * targets during test execution.  
 * 
 *
 */
public class MissingInvocationTargetException extends Exception {

	
	private static final long serialVersionUID = 1616653146326672452L;

	private final Invocation invocation;
	
	/**
	 * @param invocation
	 * This will be called from interpreter when invocation target is missing during test execution
	 */
	public MissingInvocationTargetException(Invocation invocation) {
		super("The invocation " + invocation.toString() + " refers to a non-existent InvocationTarget");
		this.invocation = invocation;
	}

	/**
	 * @return The Invocation associated with this exception
	 */
	public final Invocation getInvocation() {
		return this.invocation;
	}
}
