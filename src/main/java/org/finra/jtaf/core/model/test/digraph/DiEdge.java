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
package org.finra.jtaf.core.model.test.digraph;

import org.jgrapht.graph.DefaultEdge;

/**
 * This class represents an edge in the digraph
 *
 */
public class DiEdge extends DefaultEdge {
	private static final long serialVersionUID = 1L;
	
	private boolean isExclusionEdge = false;
	private boolean isDependencyEdge = false;

	public boolean isDependency() {
		return isDependencyEdge;
	}
	public boolean isExclusion() {
		return isExclusionEdge;
	}
	public void setDependencyEdge() {
		isDependencyEdge = true;
	}
	public void setExclusionEdge() {
		isExclusionEdge = true;
	}
}
