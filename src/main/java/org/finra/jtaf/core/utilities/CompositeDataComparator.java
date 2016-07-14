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
package org.finra.jtaf.core.utilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.finra.jtaf.core.asserts.AssertionFailedError;


/**
 * Utility class to perform comparison of lists and maps
 * 
 */
public class CompositeDataComparator {

	private List<String> errorList;
	private boolean accumulateErrors;

	public CompositeDataComparator() {
		accumulateErrors = false;
	}

	public CompositeDataComparator(boolean accumulateErrors) {
		this.accumulateErrors = accumulateErrors;
		errorList = new ArrayList<String>();
	}

	public void compareMap(String title, String element, Map<?, ?> expComp,
			Map<?, ?> actualComp) {
		String prefix = getPrefix(title, element);
		Set<?> keySet = expComp.keySet();

		for (Iterator<?> iter = keySet.iterator(); iter.hasNext();) {
			String obj = (String) iter.next();

			Object eValue = expComp.get(obj);

			if (!actualComp.containsKey(obj)) {
				reportError(prefix + " Couldn't find Element [" + obj
						+ "] in Actual Data");

			}
			Object aValue = actualComp.get(obj);

			assessAndCompare(prefix, obj, eValue, aValue);
		}
	}
	
	public void compareList(String title, String element, List<?> expList,
			List<?> actualList) {
		String prefix = getPrefix(title, element);

		if (compareSize(prefix, expList, actualList)) {

			for (int i = 0; i < expList.size(); i++) {
				Object expected = expList.get(i);
				Object actual = actualList.get(i);

				assessAndCompare(prefix + "[position : " + (i + 1) + "]", null,
						expected, actual);
			}
		}
	}
	
	/*
	 * for comparing untyped objects that may be instances of Boolean, String, String[], Timestamp, List, or Map
	 * lists and maps are internally tested for the same types recursively
	 * 
	 * any other types on the basic or internal levels are compared as type Object
	 * 
	 * if basic or internal Objects are both null they are considered equal
	 */
	public void compareObject(String title, String element, Object expected, Object actual) {
		assessAndCompare(title, element, expected, actual);
	}

	public List<String> getErrorList() {
		return errorList;
	}
	
	/**
	 * Sets the accumulate errors flag
	 * 
	 * @param accumulateErrors determines if the comparator will throw
	 *     an exception with errors found, or accumulate them for later reporting
	 */
	public void setAccumulateErrors(boolean accumulateErrors) {
	    this.accumulateErrors = accumulateErrors;
	    if (accumulateErrors && errorList == null) {
	        errorList = new ArrayList<String>();
	    }
	}
	
	/**
	 * Gets the accumulate errors flag
	 * 
	 * @return the accumulate errors flag
	 */
	public boolean getAccumulateErrors() {
	    return this.accumulateErrors;
	}

	private void reportError(String error) {
		if (!accumulateErrors) {
			throw new AssertionFailedError(error);
		}
		else {
			errorList.add(error);
		}
	}

	private String getPrefix(String title, String element) {
		if (element == null && !"".equals(element)) {
			return title;
		} else {
			return title + "-> " + element + " : ";
		}
	}

	private void assessAndCompare(String title, String element,
			Object expected, Object actual) {
		String prefix = getPrefix(title, element);

		if (expected == null && actual == null) {
			return;
		} else if (expected == null || actual == null) {
			if ("".equals(expected) || "".equals(actual)) {
				return;
			}
			reportError(prefix + " didn't match. Expected Value[" + expected
					+ "] and Actual Value [" + actual + "].");
		} else if (expected instanceof Map) {
			compareMap(title, element, (Map<?, ?>) expected, (Map<?, ?>) actual);
		} else if (expected instanceof List) {
			compareList(title, element, (List<?>) expected, (List<?>) actual);
		} else if (expected instanceof Timestamp) {
			Timestamp expTimestamp = (Timestamp) expected;
			if (!(actual instanceof Timestamp)) {
				reportError(prefix + " Expected value [" + expTimestamp
						+ "] and Actual Value [" + actual + "].");
			}
			else {
				compareObject(prefix, expTimestamp, (Timestamp) actual);
			}
		} else if (expected instanceof String[]) {
			String e[] = (String[]) expected;
			String a[] = (String[]) actual;

			if (e.length != a.length) {
				reportError(prefix
						+ " Length of string array didn't match. Expected ["
						+ e.length + "] and Actual [" + a.length + "].");
			}

			for (int i = 0; i < e.length; i++) {
				try {
					Double expD = Double.valueOf((String) e[i]);
					Double actD = Double.valueOf((String) a[i]);

					compareObject(prefix, expD, actD);
				} catch (NumberFormatException ignore) {
					compareObject(prefix, e[i], a[i]);
				}
			}
		} else if (expected instanceof String) {

			try {
				Double expD = Double.valueOf((String) expected);
				Double actD = Double.valueOf((String) actual);

				compareObject(prefix, expD, actD);
			} catch (NumberFormatException ignore) {
				compareObject(prefix, expected, actual);
			}
		} else if (expected instanceof Boolean && actual instanceof String) {

			try {

				String actualString = Boolean.FALSE.toString();
				if ("y".equalsIgnoreCase((String) actual)
						|| "true".equalsIgnoreCase((String) actual)) {
					actualString = Boolean.TRUE.toString();
				}

				compareObject(prefix, expected.toString(), actualString);
			} catch (NumberFormatException ignore) {
				compareObject(prefix, expected, actual);
			}
		} else if (expected instanceof Integer && actual instanceof BigDecimal) {
			BigDecimal expectedBigDecimal = new BigDecimal((Integer) expected);
			compareObject(prefix, expectedBigDecimal, actual);

		} else {
			compareObject(prefix, expected, actual);
		}
	}

	private void compareObject(String prefix, Object expected, Object actual) {
		if (!expected.equals(actual)) {
			reportError(prefix + " didn't match. Expected Value[" + expected
					+ "] and Actual Value [" + actual + "].");
		}
	}

	private boolean compareSize(String comp, List<?> expSessions, List<?> actualSessions) {
		boolean equal = true;
		if (expSessions.size() != actualSessions.size()) {
			reportError(comp + "didn't match; Expected List size: "
					+ expSessions.size() + " -- Actual List size: "
					+ actualSessions.size());
			equal = false;
		}
		return equal;
	}
}
