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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Random String-related functions that are generally useful
 *
 */
public class StringHelper {
	static Logger logger = Logger.getLogger(StringHelper.class.getPackage().getName());
	private static final String       INDENTATION       = "   ";
	private static final List<String> INDENTATION_CACHE = createIndentationArray();

	private static final List<String> createIndentationArray() {
		List<String> retval = new ArrayList<String>();
		retval.add(INDENTATION);
		return retval;
	}


	/**
	 * This method is not thread-safe
	 * @param depth
	 * @return
	 */
	public static final String indent(int depth) {
		StringBuilder largestIndentationBuilder = new StringBuilder(INDENTATION_CACHE.get(INDENTATION_CACHE.size() - 1));
		for(int i = INDENTATION_CACHE.size(); i <= depth; ++i) {
			largestIndentationBuilder.append(INDENTATION);
			INDENTATION_CACHE.add(largestIndentationBuilder.toString());
		}
		return INDENTATION_CACHE.get(depth);
	}
	
	public static final String join(Object[] elements, String glue) {
		ArrayList<Object> temp = new ArrayList<Object>(elements.length);
		for(Object s : elements) {
			temp.add(s);
		}
		return StringHelper.join(temp, glue);
	}
	
	public static final String join(Collection<? extends Object> elements, String glue) {
		if(elements.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Iterator<? extends Object> itr = elements.iterator();
		sb.append(itr.next().toString());
		
		while(itr.hasNext()) {
			sb.append(glue);
			sb.append(itr.next().toString());
		}
		return sb.toString();
	}
	
	public static String getConcatenatedStringFromList(List<String> input, String delimiter) {
		StringBuffer concatenatedString = new StringBuffer();
		for(String data: input) {
			concatenatedString.append(data);
			concatenatedString.append(delimiter);
		}
		return concatenatedString.toString();
	}
	
	public static String getZipAndConcatenated(List<String> input1, List<String> input2, String delimiter) {
		StringBuffer concatenatedString = new StringBuffer();

		for(int i = 0; i < input1.size(); i++) {
			concatenatedString.append(input1.get(i));
			concatenatedString.append("='");
			if (i < input2.size()) {
				concatenatedString.append(input2.get(i));	
			}
			concatenatedString.append("'");
			concatenatedString.append(delimiter);
		}
		return concatenatedString.toString();
	}
	
	public static List<String> ArrayToList(String[] input) {
		if (input == null) {
			return null;
		}
		
		List<String> output = new ArrayList<String>();
		for(int i = 0; i < input.length; i++) {
			output.add(input[i]);
		}
		return output;
	}
}