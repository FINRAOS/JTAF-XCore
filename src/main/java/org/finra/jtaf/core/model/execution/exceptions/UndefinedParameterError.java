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

package org.finra.jtaf.core.model.execution.exceptions;

/**
 * Some implementations of IInvocationContext will throw this error
 * when a Command tries to retrieve a parameter that was not specified
 * in the Script Contract.
 *
 */
public class UndefinedParameterError extends Error {

	private static final long serialVersionUID = 364767996067850085L;

	private final String parameter;
	
	/**
	 * @param name
	 */
	
	public UndefinedParameterError(String name) {
		super("The parameter '" + name + "' was not specified in the Script Contract");
		this.parameter = name;
	}
	
	public final String getParameter() {
		return this.parameter;
	}
}
