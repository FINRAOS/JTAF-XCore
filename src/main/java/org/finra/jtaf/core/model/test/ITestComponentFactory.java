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

/**
 * Retrieves test model elements from some backend. The backend is the file
 * system and certain XML files
 * 
 */
public interface ITestComponentFactory {

	/**
	 * Retrieves a component given its TestPath. TODO: Perhaps this should just
	 * be a URI?
	 * 
	 * @param path
	 * @return
	 */
	TestComponent getComponent(TestPath path);
}
