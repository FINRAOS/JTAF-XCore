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
package org.finra.jtaf.core.asserts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 
 * This class can be used to collect errors.
 *
 */
public class ErrorAccumulator {

	private String name;
	private ArrayList<Throwable> errors;

	// needed for ignore errors at script level:
	boolean errorsThrown = false;

	/**
	 * Create a named error accumulator
	 * @param name
	 */
	public ErrorAccumulator(String name) {
		this.name = name;
		errors = new ArrayList<Throwable>();
	}

	

	/**
	 * Gets the name of the accumulator
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the number of errors.
	 * @return
	 */
	public int getNumErrors() {
		return errors.size();
	}

	/**
	 * Adds and Error to accumulate
	 * @param th
	 * @return
	 */
	public boolean addError(Throwable th) {
		return errors.add(th);
	}

	/**
	 * returns true if no errors
	 * @return
	 */
	public boolean isEmpty() {
		return errors.isEmpty();
	}

	/**
	 * Returns error messages
	 * @return
	 */
	public String getErrorMessages() {
		StringBuilder errorInfoBuilder = new StringBuilder();

		for (int i = 0; i < errors.size(); i++) {
			errorInfoBuilder.append("\n " + errors.get(i).getLocalizedMessage());
		}

		return errorInfoBuilder.toString();
	}

	/**
	 * Returns error stack traces
	 * @return
	 */
	public String getErrorStackTraces() {
		StringBuilder errorInfoBuilder = new StringBuilder();
		// TODO: use static access
		// TODO: determine if this dependency is really necessary, do we need to
		// depend on commons-lang to make stack trace printing easier?
		for (int i = 0; i < errors.size(); i++) {
			errorInfoBuilder.append("\n " + ExceptionUtils.getStackTrace(errors.get(i)));
		}

		return errorInfoBuilder.toString();
	}

	/**
	 * Returns list of stack trace elements
	 * @return
	 */
	public List<StackTraceElement[]> getErrorStackTraceElementsList() {
		List<StackTraceElement[]> list = new ArrayList<StackTraceElement[]>();
		ArrayList<Throwable> errors = getErrors();
		for (Throwable th : errors) {
			list.add(th.getStackTrace());
		}

		return list;
	}

	/**
	 * Throws accumulated errors
	 */
	public void throwErrors() {
		AssertionFailedError e = getWrappedErrors();
		if (e != null) {
			errorsThrown = true;
			throw e;
		}
	}

	public boolean hasThrown() {
		return errorsThrown;
	}

	public ArrayList<Throwable> getErrors() {
		return errors;
	}

	public AssertionFailedError getWrappedErrors() {

		if (isEmpty())
			return null;

		String message = getErrorStackTraces();

		if (message.equals(""))
			return null;

		return new AssertionFailedError(
				"Error accumulator found and ignored the following errors in "
						+ getName() + ":" + message);

	}
}
