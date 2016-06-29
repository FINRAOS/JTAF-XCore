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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.finra.jtaf.core.exceptions.DependencyException;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestNamespace;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestSuite;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.ClassBasedEdgeFactory;


/**
 * This factory class allows for the addition of nodes and edges into the digraph.
 *
 */
public class DigraphFactory {
	private TestDigraph digraph;
	private MessageCollector mc;
	
	public DigraphFactory(TestDigraph digraph2, MessageCollector mc2){
		digraph = digraph2;
		mc = mc2;	
	}
	
	public void createGraph(TestNamespace testNamespace){
		//First we have to do a replace on all the dependencies that specify a TestSuite name.
		Map<String, TestSuite> tsMap = testNamespace.getTestSuiteMapping();
		//Iterate across all the TestSuites and then assign the dependencies to the correct mapping
		for (String s: tsMap.keySet()){
			Set<String> tsDependenciesAsTests = new HashSet<String>();
			Set<String> tsExclusionsAsTests = new HashSet<String>();
			//For each TestSuite object in the suitemapping, grab dependencies and convert
			
			TestSuite currentSuite = tsMap.get(s);
			Dependencies dependSuite = currentSuite.getDependencies();
			Dependencies excludeSuite = currentSuite.getExclusions();
			
			//Appending all the tests to the list
			tsDependenciesAsTests.addAll(dependSuite.getDependenciesTests());
			tsExclusionsAsTests.addAll(excludeSuite.getDependenciesTests());
			
			//Grabbing the TestSuite dependencies and converting
			for (String dependentSuites: dependSuite.getDependenciesSuites()){
				//Given a supplied dependent suite go into the actual suite and get the test info
				if (tsMap.get(dependentSuites) != null){
					for (TestComponent ts: tsMap.get(dependentSuites).getComponentList()){
						tsDependenciesAsTests.add(ts.getName());
					}
				}
				else{
					throw new DependencyException("The TestSuite '" + dependentSuites + "' specified for TestSuite '" + s + "' dependencies does not exist!");
				}
			}
			//Grabbing the TestSuite exclusions and converting
			for (String excludedSuites: excludeSuite.getDependenciesSuites()){
				//Given a supplied dependent suite go into the actual suite and get the test info
				if (tsMap.get(excludedSuites) != null){
					for (TestComponent ts: tsMap.get(excludedSuites).getComponentList()){
						tsExclusionsAsTests.add(ts.getName());
					}
				}else{
					throw new DependencyException("The TestSuite '" + excludedSuites + "' specified for TestSuite '" + s + "' exclusions does not exist!");
				}
			}
			//Now all the TestSuite dependencies have been converted and placed into a list.
			//Go down into the Test level and grab its dependencies.
			for (TestComponent tc: currentSuite.getComponentList()){
				Dependencies dependTest = ((TestScript) tc).getDependencies();
				Dependencies excludeTest = ((TestScript) tc).getExclusions();
				Set<String> testScriptDependencies = dependTest.getDependenciesTests();
				Set<String> testScriptExclusions = excludeTest.getDependenciesTests();

				for (String tsDepend: dependTest.getDependenciesSuites()){
					//Given tsDepend as the testSuite
					if (tsMap.get(tsDepend) != null){
						for(TestComponent tcTest: tsMap.get(tsDepend).getComponentList()){
							testScriptDependencies.add(tcTest.getName());
						}
					}
					else{
						throw new DependencyException("The TestSuite '" + tsDepend + "' specified @ '" + ((TestScript) tc).getFullName() + "' dependencies does not exist!");
					}
				}
				for (String tsExclude: excludeTest.getDependenciesSuites()){
					//Given tsDepend as the testSuite
					if (tsMap.get(tsExclude) != null){
						for(TestComponent tcTest: tsMap.get(tsExclude).getComponentList()){
							testScriptExclusions.add(tcTest.getName());
						}
					}
					else{
						throw new DependencyException("The TestSuite '" + tsExclude + "' specified @ '" + ((TestScript) tc).getFullName() + "' exclusions does not exist!");
					}
				}
				testScriptDependencies.addAll(tsDependenciesAsTests);
				testScriptExclusions.addAll(tsExclusionsAsTests);
				addEdges(digraph.getVertex(tc.getName()), testScriptDependencies, testScriptExclusions);
			}
		}
		checkDependencyCycles();
	}
	private void checkDependencyCycles(){
		TestDigraph forCyclesGraph = new TestDigraph(new ClassBasedEdgeFactory<DiNode, DiEdge>(DiEdge.class));
		for (DiNode d: digraph.vertexSet()){
			forCyclesGraph.addVertex(d);
		}
		Set<DiEdge> tempEdgeSet = new HashSet<DiEdge>();
		tempEdgeSet.addAll(digraph.edgeSet());
		for(DiEdge e: tempEdgeSet){
			if (e.isDependency()){
				forCyclesGraph.addEdge(forCyclesGraph.getVertex(digraph.getEdgeSource(e).getTestName()), forCyclesGraph.getVertex(digraph.getEdgeTarget(e).getTestName()));
			}
		}
		CycleDetector<DiNode, DiEdge> cd = new CycleDetector<DiNode, DiEdge>(forCyclesGraph);
		Set<DiNode> cycleList = cd.findCycles();
		if (cycleList.size() > 0){
			mc.fatal("Cycle(s) detected");
			for (DiNode d: cycleList){
				mc.fatal("@" + d.getTestScript().getFullName());
			}
			throw new DependencyException("Found cycle(s)");
		}
	}
	private void addEdges(DiNode edgeFrom, Set<String> dependencies, Set<String> exclusions){
		for (String d: dependencies){
			if (digraph.getVertex(d) != null){
				digraph.addEdge(edgeFrom, digraph.getVertex(d)).setDependencyEdge();
			}
			else{
				throw new DependencyException("The Test dependency: '" + d + "' does not exist at '" + edgeFrom.getTestScript().getFullName() + "'");
			}
		}
		for (String e: exclusions){
			if (digraph.getVertex(e) != null){
				digraph.addEdge(edgeFrom, digraph.getVertex(e)).setExclusionEdge();				
			}
			else{
				throw new DependencyException("The Test exclusion: '" + e + "' does not exist at '" + edgeFrom.getTestScript().getFullName() + "'");
			}
		}
	}
}
