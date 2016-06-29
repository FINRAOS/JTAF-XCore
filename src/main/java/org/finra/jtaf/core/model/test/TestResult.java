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

import java.util.List;

/**
 * This class represents the test result for the test
 */
public class TestResult {
	private final List<TestStepsDetails> testStepsDetails;
	private Throwable failureReason;
	private TestStatus testStatus;
	
	
	/**
	 * Initialize the TestResult with the current list of test steps, and the test status
	 * @param testStepsDetails
	 * @param teststatus
	 */
	public TestResult(List<TestStepsDetails> testStepsDetails, TestStatus teststatus) {
		this.testStepsDetails = testStepsDetails;
		this.testStatus = teststatus;
	}	
	
	/**
	 * Initialize the TestResult with the current list of test steps, and the test status, and the failure reason
	 * @param testStepsDetails
	 * @param teststatus
	 * @param failureReason
	 */
	public TestResult(List<TestStepsDetails> testStepsDetails, TestStatus teststatus, Throwable failureReason) {
		this.testStepsDetails = testStepsDetails;
		this.testStatus = teststatus;
		this.failureReason  = failureReason;
	}	
	
	
	/**
	 * Return if the current test passed
	 * @return
	 */
	public boolean isTestPassed() {
		if (failureReason == null) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * Return the list of test steps for the current test
	 * @return
	 */
	public List<TestStepsDetails> getTestStepsDetails() {
		return testStepsDetails;
	}
	
	/**
	 * Set the failure reason for the current test
	 * @param failureReason
	 */
	public void setFailureReason(Throwable failureReason){
		this.failureReason = failureReason;
	}
	
	/**
	 * Return the failure reason for the current test
	 * @return
	 */
	public Throwable getFailureReason() {
		return failureReason;
	}
	
	/**
	 * Update the test status
	 * @param testStatus
	 */
	public void updateTestStatus(TestStatus testStatus){
		this.testStatus = testStatus;
	}
	
	/**
	 * Get the test status
	 * @return
	 */
	public TestStatus getTestStatus(){
		return this.testStatus;
	}
		
}
