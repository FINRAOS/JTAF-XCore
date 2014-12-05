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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DirectedMultigraph;


/**
 * This is a container class that houses the dependencies and exclusions across the tests.
 *
 */
public class TestDigraph extends DirectedMultigraph<DiNode, DiEdge> implements DirectedGraph<DiNode, DiEdge>{
	private static final long serialVersionUID = 1L;
	private Map<String, DiNode> digraphVertexMapping = new HashMap<String, DiNode>();
	
	public TestDigraph(EdgeFactory<DiNode, DiEdge> arg0) {
		super(arg0);
	}
	
	
	public void updateTestStatus(String testName, String status){
		digraphVertexMapping.get(testName).setTestStatus(status);
	}
	
	public DiNode getVertex(String testName){
		return digraphVertexMapping.get(testName);
	}
	
	@Override
	public boolean addVertex(DiNode d) {
		if (getVertex(d.getTestName()) != null){
			return false;
		}
		digraphVertexMapping.put(d.getTestName(), d);
		return super.addVertex(d);
	}
	
	@Override
	public boolean removeVertex(DiNode d) {
		if (getVertex(d.getTestName()) != null){
			return false;
		}
//		digraphVertexMapping.put(d.getTestName(), d);
		digraphVertexMapping.remove(d.getTestName());
		return super.removeVertex(d);
	}
	
	public List<DiNode> getAllDependencies(String testName){
		DiNode associatedNode = getVertex(testName);
		List<DiNode> dependentNodes = new ArrayList<DiNode>();
		for (DiEdge edge: outgoingEdgesOf(associatedNode)){
			if (edge.isDependency()){
				dependentNodes.add(getEdgeTarget(edge));
			}
		}
		return dependentNodes;
	}
	public List<DiNode> getAllExclusions(String testName){
		DiNode associatedNode = getVertex(testName);
		List<DiNode> exclusionNodes= new ArrayList<DiNode>();
		for (DiEdge edge: outgoingEdgesOf(associatedNode)){
			if (edge.isExclusion()){
				exclusionNodes.add(getEdgeTarget(edge));
			}
		}
		return exclusionNodes;
	}
}
