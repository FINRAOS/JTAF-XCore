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
package org.finra.jtaf.core.commands.context;

import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.core.model.exceptions.NameFormatException;

/**
 * Gets a list from context and adds all elements to another list stored in context
 *
 */
public class AddListToListCmd extends AbstractContextCmd {
	public static final String VALUE_IN_ATTRIBUTE = "addlist";
	public static final String INDEX_ATTRIBUTE = "index";
	public static final String VALUE_OUT_ATTRIBUTE = "tolist";

	public AddListToListCmd(String name) throws NameFormatException {
		super(name);
	}

	@Override
	protected void execute() throws Throwable {
		@SuppressWarnings("unchecked")
		List<Object> valueIn = (List<Object>) getRequiredObject(getRequiredString(VALUE_IN_ATTRIBUTE));
		String valueOutAttribute = getRequiredString(VALUE_OUT_ATTRIBUTE);
		@SuppressWarnings("unchecked")
		List<Object> valueOut = (List<Object>) getOptionalObject(valueOutAttribute);
		if(valueOut == null) {
			List<Object> suppression = new ArrayList<Object>();
			valueOut = suppression;
		}
		Integer index = mch.getOptionalInteger(INDEX_ATTRIBUTE);
		if(index == null || index < 0) {
			index = valueOut.size(); 
		} else if(index > 0) { // for one-based
			index -= 1;
		}
		valueOut.addAll(index, valueIn);
		mch.setValueOut(valueOutAttribute, valueOut);
	}

}
