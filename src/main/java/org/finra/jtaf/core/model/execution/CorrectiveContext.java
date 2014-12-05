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

package org.finra.jtaf.core.model.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.finra.jtaf.core.model.exceptions.MissingInvocationTargetException;
import org.finra.jtaf.core.model.execution.exceptions.UndefinedParameterError;
import org.finra.jtaf.core.model.execution.exceptions.UndefinedProductionError;
import org.finra.jtaf.core.model.invocationtarget.InvocationTarget;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.test.TestScript;

/**
 * This Context can be used to produce a TestScript Contract
 */
public class CorrectiveContext implements IInterpreterContext {

	private static final Set<InvocationTarget> modifications = new HashSet<InvocationTarget>();

	private static class ContextFrame {
		public final InvocationTarget target;
		public Map<String, Object> parameters;
		public Map<String, Object> produced;

		public ContextFrame(InvocationTarget target) {
			this.target = target;
			this.parameters = new HashMap<String, Object>();
			this.produced = new HashMap<String, Object>();
		}
	}

	private final Stack<ContextFrame> frameStack;
	private TestScript testScript;

	public CorrectiveContext() {
		this.frameStack = new Stack<ContextFrame>();
		this.frameStack.push(new ContextFrame(null));
		this.testScript = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInterpreterContext#pushInvocation
	 * (org.finra.jtaf.core.model.statement.Invocation,
	 * org.finra.jtaf.core.model.invocationtarget.InvocationTarget)
	 */
	public void pushInvocation(Invocation inv, InvocationTarget target)
			throws MissingInvocationTargetException {

		final ContextFrame next = new ContextFrame(target);
		next.parameters = new HashMap<String, Object>(
				this.frameStack.peek().parameters);
		for (Entry<String, Object> e : inv.getParameters().entrySet()) {
			next.parameters.put(e.getKey().toLowerCase(), e.getValue());
		}
		this.frameStack.push(next);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInterpreterContext#pushInvocation
	 * (java.util.Map<String, ? extends Object>)
	 */
	public void setParameters(Map<String, ? extends Object> vars) {
		HashMap<String, Object> p = new HashMap<String, Object>();
		for (Entry<String, ? extends Object> e : vars.entrySet()) {
			p.put(e.getKey().toLowerCase(), e.getValue());
		}
		this.frameStack.peek().parameters = p;
	}

	/**
	 * Clears the current context for all the invocations in the stack
	 */
	public void clearContext() {
		int size = this.frameStack.size();
		ArrayList<ContextFrame> stack = new ArrayList<ContextFrame>();
		for (int x = 0; x < size; x++) {
			ContextFrame previousCommand = this.frameStack.pop();
			ContextFrame newFrame = new ContextFrame(previousCommand.target);
			stack.add(newFrame);
		}

		for (int x = size - 1; x >= 0; x--) {
			this.frameStack.push(stack.get(x));

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.finra.jtaf.core.model.execution.IInterpreterContext#popFrame()
	 */
	public void popFrame() {
		final ContextFrame popped = this.frameStack.pop();
		final ContextFrame top = this.frameStack.peek();
		for (Entry<String, Object> e : popped.produced.entrySet()) {
			top.parameters.put(e.getKey(), e.getValue());

			if ((top.target != null)
					&& !top.target.getProductions().contains(e.getKey())) {
				modifications.add(top.target);
				top.target.addProduction(e.getKey());
			}
			top.produced.put(e.getKey(), e.getValue());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInvocationContext#contains(java.
	 * lang.String)
	 */
	public boolean contains(String name) {
		return this.frameStack.peek().parameters
				.containsKey(name.toLowerCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInvocationContext#getObject(java
	 * .lang.String)
	 */
	public Object getObject(String name) throws UndefinedParameterError {
		final String lookupName = name.toLowerCase();
		final InvocationTarget t = frameStack.peek().target;
		if (!t.getAllParameters().contains(lookupName)) {
			modifications.add(t);
			// Assume the parameter is required until I'm shown otherwise
			t.addRequiredParameter(lookupName);
		}

		return this.frameStack.peek().parameters.get(lookupName);
	}

	/**
	 * Return all objects in context based on a certain type
	 * 
	 * @param objectType
	 *            => Object of the type you are looking for
	 * @return => list of all objects of type passed in the parameter
	 * @throws UndefinedParameterError
	 */
	public List<Object> getObjectByType(Object objectType)
			throws UndefinedParameterError {
		List<Object> results = new ArrayList<Object>();
		for (String key : frameStack.peek().parameters.keySet()) {
			if (frameStack.peek().parameters.get(key).getClass()
					.equals(objectType.getClass())) {
				results.add(frameStack.peek().parameters.get(key));
			}
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInvocationContext#getAllObjects()
	 */
	public Map<String, Object> getAllObjects() {
		return frameStack.peek().parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInvocationContext#putObject(java
	 * .lang.String, java.lang.Object)
	 */
	public void putObject(String name, Object value)
			throws UndefinedProductionError {
		final String lookupName = name.toLowerCase();
		final InvocationTarget t = frameStack.peek().target;
		if (!t.getProductions().contains(lookupName)) {
			modifications.add(t);
			t.addProduction(lookupName);
		}

		this.frameStack.peek().produced.put(lookupName, value);
		this.frameStack.peek().parameters.put(lookupName, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInvocationContext#removeObject(java
	 * .lang.String)
	 */
	public void removeObject(String name) {
		this.frameStack.peek().parameters.remove(name.toLowerCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInvocationContext#getTestScript()
	 */
	public TestScript getTestScript() {
		return this.testScript;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.finra.jtaf.core.model.execution.IInterpreterContext#setTestScript
	 * (org.finra.jtaf.core.model.test.TestScript)
	 */
	public void setTestScript(TestScript ts) {
		this.testScript = ts;
	}

}
