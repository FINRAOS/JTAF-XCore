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
package org.finra.jtaf.core;

import java.util.HashMap;
import java.util.Map;

import org.finra.jtaf.core.model.invocationtarget.InvocationTarget;

/**
 * This class is for the commands registered in the *.commands.xml files in
 * testlibrary folder. It is a property of the Automation Engine. It saves
 * <CommandName, TargetClass> pair for each command.
 * 
 */
public class CommandRegistry {
	private final Map<String, InvocationTarget> invocationTargetMap;

	public CommandRegistry() {
		this.invocationTargetMap = new HashMap<String, InvocationTarget>();
	}

	
	
	/**
	 * Registers the specified command/invocation target.
	 * @param name
	 * @param invocationTarget
	 */
	public void registerInvocationTarget(String name, InvocationTarget invocationTarget) {
		this.invocationTargetMap.put(name, invocationTarget);
	}

	/**
	 * Returns true if the invocation target is present
	 * @param name
	 * @return
	 */
	public final boolean containsInvocationTarget(String name) {
		return invocationTargetMap.containsKey(name.toLowerCase());
	}

	/**
	 * Gets the specified invocation target
	 * @param name
	 * @return
	 */
	public final InvocationTarget getInvocationTarget(String name) {
		return invocationTargetMap.get(name.toLowerCase());
	}

}
