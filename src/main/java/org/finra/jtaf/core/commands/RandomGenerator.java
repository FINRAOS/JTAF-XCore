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
package org.finra.jtaf.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.parsing.helpers.AttributeHelper;

import com.mifmif.common.regex.Generex;


/**
 * This class is used to generate a random string, phone number, regex, US
 * state, or US state abbreviation.
 * 
 */
public class RandomGenerator extends Command {
	private static final int DEFAULT_LENGTH = 5;
	static Logger logger = Logger.getLogger(RandomGenerator.class.getPackage().getName());

	/**
     * This called when instantiating the command before it is executed by the
     * interpreter.The recordResult variable is set to false because details
     * about the RandomGenerator step aren't needed, unlike the commands within
     * the RandomGenerator block.
     * 
     * @param name
     *            - name of the command
     * @throws NameFormatException
     */
	public RandomGenerator(String name) throws NameFormatException {
		super(name);
	}

	public static final List<String> ABBREV_US_STATE = Collections.unmodifiableList(Arrays.asList("AL", "AK", "AS", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FM", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MH", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
			"NM", "NY", "NC", "ND", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VI", "VA", "WA", "WV", "WI", "WY"));
	public static final List<String> US_STATE = Collections.unmodifiableList(Arrays.asList("Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Federated States of Micronesia", "Florida", "Georgia", "Guam", "Hawaii",
			"Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Marshall Islands", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
			"North Carolina", "North Dakota", "Northern Mariana Islands", "Ohio", "Oklahoma", "Oregon", "Palau", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virgin Island", "Virginia", "Washington", "West Virginia",
			"Wisconsin", "Wyoming"));
	public static final String CHARS_FOR_RANDOM_STRING = "qwertyuioplkjhgfdsazxcvbnm";

	/**
     * Use a regular expression to generate a string or use methods to select US
     * state name or abbreviations and generate phone numbers and strings.
     * 
     * @param method
     *            - the method to be used. Either abbrevUSState, USState, phone,
     *            number, or string
     * @param length
     *            - length of number or string to be generated. lengthMin,
     *            lengthMax, min, and max should not be specified when using
     *            this parameter.
     * @param lengthMin
     *            - used to specify minimum length of random string generated
     * @param lengthMax
     *            - used to specify maximum length of random string generated
     * @param min
     *            - used to specify minimum length of random number generated
     * @param max
     *            - used to specify maximum length of random number generated
     * @param regexp
     *            - regular expression to be used for string generation. This
     *            should not be specified if method parameter is.
     * @return the randomly generated string
     */
	public static String generate(String method, String length, String lengthMin, String lengthMax, String min, String max, String regexp) {
		String valueGenerated = null;

		if (regexp != null && !regexp.equalsIgnoreCase("")) {
			if (method != null && !method.equalsIgnoreCase("")) {
				logger.fatal("Oops! Specified 'method' ('" + method + "') and 'regexp' ('" + regexp + "') simulteneously. Use only one of it, please. Will use 'regexp' here...");
			}
			valueGenerated = generateRegexp(regexp);
		} else {
			if (method != null && !method.equalsIgnoreCase("")) {
				if (method.equalsIgnoreCase("abbrevUSState")) {
					valueGenerated = generateAbbrevUSState();
				} else if (method.equalsIgnoreCase("USState")) {
					valueGenerated = generateUSState();
				} else if (method.equalsIgnoreCase("phone")) {
					valueGenerated = generatePhone();
				} else if (method.equalsIgnoreCase("number")) {
					// verify input parameters
					if ((length != null && !length.equalsIgnoreCase("")) && (min != null && !min.equalsIgnoreCase("")) && (max != null && !max.equalsIgnoreCase(""))) {
						logger.fatal("Oops! Specified 'length', 'min' and 'max' parameters... You can use 'length' or 'max + min' only. Fix your test script, please! Will use 'length' here only...");
						min = null;
						max = null;
					}
					if (((min != null && !min.equalsIgnoreCase("")) && (max == null)) || ((min == null) && (max != null && !max.equalsIgnoreCase("")))) {
						logger.fatal("Oops! Specified only one of 'min', 'max' parameters... You have specify 'max' and 'min' both. Fix your test script, please! Will use default 'length' here...");
						min = null;
						max = null;
					}
					if ((length == null || length.equalsIgnoreCase("")) && (min == null || min.equalsIgnoreCase("")) && (max == null || max.equalsIgnoreCase(""))) {
						length = "5";
					}

					try {
						int maxInt = Integer.parseInt(max);
						int minInt = Integer.parseInt(min);
						valueGenerated = String.valueOf(minInt + (int) (Math.random() * (maxInt - minInt)));
					} catch (Exception e) {
						Generex generex = new Generex("[0-9]{" + length + "}");
						valueGenerated = generex.random();
					}
				} else if (method.equalsIgnoreCase("string")) {
					// verify input parameters
					if ((length != null && !length.equalsIgnoreCase("")) && (lengthMin != null && !lengthMin.equalsIgnoreCase("")) && (lengthMax != null && !lengthMax.equalsIgnoreCase(""))) {
						logger.fatal("Oops! Specified 'length', 'lengthMin' and 'lengthMax' parameters... You can use 'length' or 'lengthMax + lengthMin' only. Fix your test script, please! Will use 'length' here only...");
						lengthMin = null;
						lengthMax = null;
					}
					if (length == null && (((lengthMin != null && !lengthMin.equalsIgnoreCase("")) && (lengthMax == null)) || ((lengthMin == null) && (lengthMax != null && !lengthMax.equalsIgnoreCase(""))))) {
						logger.fatal("Oops! Specified only one of 'lengthMin', 'lengthMax' parameters... You have specify 'lengthMax' and 'lengthMin' both. Fix your test script, please! Will use default 'length' here...");
						lengthMin = null;
						lengthMax = null;
					}

					int lengthInt;
					try {
						int lengthMinInt = Integer.parseInt(lengthMin);
						int lengthMaxInt = Integer.parseInt(lengthMax);
						lengthInt = lengthMinInt + (int) (Math.random() * (lengthMaxInt - lengthMinInt));
					} catch (NumberFormatException numberFormatException) {
						try {
							lengthInt = Integer.parseInt(length);
						} catch (NumberFormatException numberFormatException2) {
							lengthInt = DEFAULT_LENGTH;
						}
					}

					valueGenerated = generateString(CHARS_FOR_RANDOM_STRING, lengthInt);
				}
			}
		}
		return valueGenerated;
	}

	   /**
     * Generate a string using a specified regular expression.
     * 
     * @param regexp
     *            - regular expression to be used to generate the string
     * @return the randomly generated string
     */
	public static String generateRegexp(String regexp) {
		Generex generex = new Generex(regexp);
		return generex.random();
	}

	/**
     * Generates a random phone number.
     * 
     * @return the randomly generated phone number.
     */
	public static String generatePhone() {
		Generex generex = new Generex("[0-9]{10}");
		return generex.random();
	}

	/**
     * Randomly selects a US state abbreviation from the list.
     * 
     * @return the randomly selected state abbreviation
     */
	public static String generateAbbrevUSState() {
		return ABBREV_US_STATE.get((new Random()).nextInt(ABBREV_US_STATE.size()));
	}

	/**
     * Randomly selects a US state from the list.
     * 
     * @return the randomly selected state
     */
	public static String generateUSState() {
		return US_STATE.get((new Random()).nextInt(US_STATE.size()));
	}

	/**
     * This randomly generates a string given a set of characters and a desired
     * length.
     * 
     * @param chars
     *            - The characters to be used
     * @param length
     *            - The length of the resulting string
     * @return The generated string
     */
	public static String generateString(String chars, int length) {
		Generex generex = new Generex("[" + chars + "]{" + length + "}");
		return generex.random();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
	    recordResult = true;
		String name = getOptionalString("name");
		String method = getOptionalString("method");
		String length = getOptionalString("length");
		String lengthMin = getOptionalString("lengthMin");
		String lengthMax = getOptionalString("lengthMax");
		String min = getOptionalString("min");
		String max = getOptionalString("max");
		String regexp = getOptionalString("regexp");
		String saveToGlobalContextAs = getOptionalString("saveToGlobalContextAs");

		// process simple case
		
		String randomValue = generate(method, length, lengthMin, lengthMax, min, max, regexp);
		
		if (randomValue != null) {
			ctx.putObject(name, randomValue);
			if (saveToGlobalContextAs != null) {
				Command.putToGlobalContext(saveToGlobalContextAs, randomValue);
			}
			logger.info("Generated new value: " + name + "='" + randomValue + "' (" + method + ", " + length + ", " + lengthMin + ", " + lengthMax + ", " + min + ", " + max + ", " + regexp + ", " + saveToGlobalContextAs + ")");

		}

		// process List
		Map<String, Object> allObjects = ctx.getAllObjects();
		Map<String, Object> newObjects = new HashMap<String, Object>();
		for(Map.Entry<String, Object> allObjectsEntry : allObjects.entrySet()) {
			final Object obj = allObjectsEntry.getValue();

			if (obj.getClass().equals(ArrayList.class)) {
				ArrayList<Object> ar = (ArrayList<Object>) obj;
				ArrayList<Object> arNew = new ArrayList<Object>();
				Iterator<Object> listIterator = ar.iterator();

				while (listIterator.hasNext()) {
					Object value = listIterator.next();
					if (value.getClass().equals(AttributeHelper.class)) {
						AttributeHelper ah = (AttributeHelper) value;
						if (ah.containsKey("method")) {
							method = ah.getOptionalString("method");
						}
						if (ah.containsKey("length")) {
							length = ah.getOptionalString("length");
						}
						if (ah.containsKey("lengthMin")) {
							lengthMin = ah.getOptionalString("lengthMin");
						}
						if (ah.containsKey("lengthMax")) {
							lengthMax = ah.getOptionalString("lengthMax");
						}
						if (ah.containsKey("min")) {
							min = ah.getOptionalString("min");
						}
						if (ah.containsKey("max")) {
							max = ah.getOptionalString("max");
						}
						if (ah.containsKey("regexp")) {
							regexp = ah.getOptionalString("regexp");
						}
						arNew.add(generate(method, length, lengthMin, lengthMax, min, max, regexp));
					} else {
						arNew.add(value);
					}
				}
				newObjects.put(allObjectsEntry.getKey(), arNew);
				ctx.putObject(allObjectsEntry.getKey(), arNew);
				
				if (saveToGlobalContextAs != null) {
					Command.putToGlobalContext(saveToGlobalContextAs, arNew);
				}
			} else if (obj.getClass().equals(HashMap.class)) {
				Map<String, Object> ar = (Map<String, Object>) obj;
				Map<String, Object> arNew = new HashMap<String, Object>();
				for (Map.Entry<String, Object> arEntry : ar.entrySet()) {
					Object value = arEntry.getValue();
					if (value.getClass().equals(AttributeHelper.class)) {
						AttributeHelper ah = (AttributeHelper) value;
						if (ah.containsKey("method")) {
							method = ah.getOptionalString("method");
						}
						if (ah.containsKey("length")) {
							length = ah.getOptionalString("length");
						}
						if (ah.containsKey("lengthMin")) {
							lengthMin = ah.getOptionalString("lengthMin");
						}
						if (ah.containsKey("lengthMax")) {
							lengthMax = ah.getOptionalString("lengthMax");
						}
						if (ah.containsKey("min")) {
							min = ah.getOptionalString("min");
						}
						if (ah.containsKey("max")) {
							max = ah.getOptionalString("max");
						}
						if (ah.containsKey("regexp")) {
							regexp = ah.getOptionalString("regexp");
						}
						arNew.put(arEntry.getKey(), generate(method, length, lengthMin, lengthMax, min, max, regexp));
					} else {
						arNew.put(arEntry.getKey(), value);
					}
				}
				
				newObjects.put(allObjectsEntry.getKey(), arNew);
				ctx.putObject(allObjectsEntry.getKey(), arNew);
				
				if (saveToGlobalContextAs != null) {
					Command.putToGlobalContext(saveToGlobalContextAs, arNew);
				}
			}
			
			if (obj instanceof ArrayList) {
				logger.info("Generated new List: " + obj + "'");
			}
		}
		allObjects.putAll(newObjects);
	}
}
