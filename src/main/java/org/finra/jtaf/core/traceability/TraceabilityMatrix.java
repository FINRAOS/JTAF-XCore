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
package org.finra.jtaf.core.traceability;

import java.io.FileWriter;
import java.io.IOException;

import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestScript;


/**
 * Responsible for generating the tracability matrix csv file with test suite name, coverage, test case ID, test name, file name and automation value.
 */
public class TraceabilityMatrix {
	
	public static final String OUTPUT_FILE = "Traceability_Matrix.txt";
	
	public static void produceTraceabilityMatrix(TestAgenda ta) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(OUTPUT_FILE);
			fw.write("Test Scenario;Coverage;Test Case ID;Test Name;Automation Script;Automation Value" + "\n");
			
			for(TestScript s : ta.getTestScripts()) {
				if(!ta.isAutomationValuesEmpty()) {
					if(ta.containsAutomationValue(s.getAutomationValue())) {
						String test = s.getTestSuiteName()
						+ ";" + s.getCoverage()
						+ ";" + s.getTestCaseID()
						+ ";" + s.getName()
						+ ";" + s.getFileName()
						+ ";" + s.getAutomationValue();
						fw.write(test + "\n");
					}	
				}else {
					String test = s.getTestSuiteName()
					+ ";" + s.getCoverage()
					+ ";" + s.getTestCaseID()
					+ ";" + s.getName()
					+ ";" + s.getFileName()
					+ ";" + s.getAutomationValue();
					fw.write(test + "\n");
				}
			}
		} catch (IOException ie) {
			// Ignore since we want to still execute the tests regardless of traceability matrix
			System.err.println("Problem while trying to create traceability matrix: " + ie.toString());
			ie.printStackTrace();
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException ie) {
					// Ignore since we want to still execute the tests regardless of traceability matrix
					System.err.println("Problem while trying to create traceability matrix: " + ie.toString());
					ie.printStackTrace();
				}
			}
		}
	}
}
