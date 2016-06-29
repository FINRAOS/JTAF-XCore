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
package org.finra.jtaf.core.model.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.statement.InvocationList;
import org.finra.jtaf.core.model.test.digraph.Dependencies;


/**
 * This class represents a test script object
 * 
 */
public final class TestScript extends TestComponent {
	private InvocationList body;

	// TODO: Build generatic annotation support
	private String issue;
	private List<String> crs;
	// TODO: Status should be lists/sets/not Strings
	private String status;
	private String testCaseID;
	private String automationValue;
	private String coverage;
	private ArrayList<Requirement> requirements;
	private String description;
	private String fileName;
	private String testSuiteName;
	private static boolean isCaptureSystemInformationGlobally = false;
	private boolean isCaptureSystemInformation = false;
	private Dependencies dependencies;
	private Dependencies exclusions;
	
	private String exception="";
	
	public TestScript(String name, boolean isCaptureSystemInformation) throws NameFormatException {
		super(name);
		this.body = new InvocationList();
		this.crs     = new ArrayList<String>();
		this.status = null;
		this.requirements = new ArrayList<Requirement>();
		this.isCaptureSystemInformation = isCaptureSystemInformation;
		if (isCaptureSystemInformation) {
			isCaptureSystemInformationGlobally = true;
		}
	}

	/**
	 * These are the primary statements executed by the Test
	 * @return
	 */
	public final InvocationList getBody() {
		return this.body;
	}
	
	/**
	 * Sets this test's body to the given statement list
	 * @param statements
	 */
	public final void setBody(InvocationList statements) {
		this.body = statements;
	}
	
	public final List<String> getCRs() {
		return this.crs;
	}
	
	public void setCRs(final List<String> crs) {
		this.crs = crs;
	}
	
	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getIssue() {
		return issue;
	}

	public final String getStatus() {
		return this.status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	public void setAutomationValue(String automationValue) {
		this.automationValue = automationValue;
	}

	public String getAutomationValue() {
		return automationValue;
	}
	
	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setTestCaseID(String testCaseID) {
		this.testCaseID = testCaseID;
	}

	public String getTestCaseID() {
		return testCaseID;
	}	
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	
	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void acceptTestVisitor(ITestVisitor v) throws Exception {
		v.visitTestScript(this);
	}	
	public ArrayList<Requirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(ArrayList<Requirement> requirements) {
		this.requirements = requirements;
	}
	
	public static boolean isCaptureSystemInformationGlobally() {
		return isCaptureSystemInformationGlobally;
	}
	
	public boolean isCaptureSystemInformation() {
		return isCaptureSystemInformation;
	}
	
	public Dependencies getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<String> suite, Set<String> test) {
		dependencies = new Dependencies(suite, test);
	}

	public Dependencies getExclusions() {
		return exclusions;
	}

	public void setExclusions(Set<String> suite, Set<String> test) {
		exclusions = new Dependencies(suite, test);
	}
	
	
	public void setException(String expectedException){
	    exception = expectedException;
	}
	
	public String getException(){
        return exception;
    }
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof TestScript)){
			return false;
		}
		else {
			TestScript ts = (TestScript) o;
			if (this.getName().equals(ts.getName())
				&& this.getBody().equals(ts.getBody())
				&& this.getFileName().equals(ts.getFileName())
				&& this.getFullName().equals(ts.getFullName())){
					return true;
				}
			else{
				return false;
			}
		}		
	}
}
