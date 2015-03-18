/*
 * (C) Copyright 2015 Java Test Automation Framework Contributors.
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

import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * Exception thrown in a script when a tearDown exists and an exception is thrown in the main body of the script
 *
 */
public class ErrorBeforeTearDownException extends Exception
{
	private static final long serialVersionUID = -4243946867513666916L;
	
	private Invocation cleanupInvocation;
	private IInvocationContext invocationContext;
	
	/**
	 * 
	 * @param cause The exception or error thrown in the body of the script
	 * @param cleanupInvocation The body of the tearDown block as an Invocation
	 * @param invocationContext The context of the TryRecoverCleanup command wrapping the entire script
	 */
	public ErrorBeforeTearDownException(Throwable cause, Invocation cleanupInvocation, IInvocationContext invocationContext)
	{
		super(cause);
		this.cleanupInvocation = cleanupInvocation;
		this.invocationContext = invocationContext;
	}
	
	public Invocation getCleanupInvocation()
	{
		return cleanupInvocation;
	}
	
	public IInvocationContext getInvocationContext()
	{
		return invocationContext;
	}
}
