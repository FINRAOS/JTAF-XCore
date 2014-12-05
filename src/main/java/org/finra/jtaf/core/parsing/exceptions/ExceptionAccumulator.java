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
package org.finra.jtaf.core.parsing.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * This is used to collect several parsing exceptions and report them
 * all at once.
 */
public class ExceptionAccumulator extends ParsingException {

	/**
	 * AUTO-GENERATED
	 */
	private static final long serialVersionUID = 2524703384072863880L;

	private final List<Throwable> exceptions;
	
	public ExceptionAccumulator() {
		super("Multiple failures reported.");
		this.exceptions = new ArrayList<Throwable>();
	}


	/**
	 * @return The exceptions that have been accumulated
	 */
	public final List<Throwable> getExceptions() {
		return this.exceptions;
	}
	
	/**
	 * @param th A Throwable (Exception, Error, etc.) to add to the accumulator
	 */
	public final void add(Throwable th) {
		this.getExceptions().add(th);
		Logger.getLogger(ExceptionAccumulator.class).debug("Exception accumulated", th);
	}
	
	/**
	 * @return if true, then the accumulator has not accumulated any exceptions
	 */
	public final boolean isEmpty() {
		return this.getExceptions().isEmpty();
	}
}
