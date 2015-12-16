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

package org.finra.jtaf.core.model.execution.exceptions;

/**
 * This is thrown when an InvocationTarget tries to place something in
 * the Context without explicitly stating that the value can be returned.
 * In other words: this forces people to annotate their Commands properly :-D
 */
public class UndefinedProductionError extends Error {


    private static final long serialVersionUID = 8548760217888120440L;

    private final String productionName;

    /**
     * ProductionName associated with the InvocationTarget
     */

    public UndefinedProductionError(String productionName) {
        super("'" + productionName + "' was not specified in the invocation target's definition");
        this.productionName = productionName;
    }

    public final String getProductionName() {
        return this.productionName;
    }
}
