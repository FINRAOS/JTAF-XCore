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

import java.util.HashMap;
import java.util.Map;

import org.finra.jtaf.core.utilities.StringHelper;


/**
 * A class representing an invocable test step.
 * Primarily used to validate a Command's registration in the command library
 */
public class Invocation {	
	private final String              targetName;
	private       Map<String, Object> parameters;
	
	public Invocation(String targetName) {
		this.targetName = targetName;
		this.parameters = new HashMap<String, Object>();
	}

	/***
	 * 
	 * @return targetName - name of the target
	 */
	public final String getTargetName() {
		return this.targetName;
	}

	/***
	 * 
	 * @return parameters - map of parameter key-values
	 */
	public final Map<String, Object> getParameters() {
		return this.parameters;
	}
	
	/***
	 * 
	 * @param parameters - map of parameter key-values
	 */
	public final void setParameters(Map<String, ? extends Object> parameters) {
		this.parameters = new HashMap<String, Object>();
		this.parameters.putAll(parameters);
	}

	/***
	 * 
	 * @return a String representation of the Invocation in the following format:
	 * 	Target Name(key1=value1, key2=value2)
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getTargetName());
		sb.append("(");
		sb.append(StringHelper.join(this.getParameters().entrySet(), ", "));
		sb.append(")");
		
		return sb.toString();
	}

}