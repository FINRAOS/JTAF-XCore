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
package org.finra.jtaf.core.parsing;

import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.w3c.dom.NodeList;


/**
 * This is internal class meant for verification of expected exception in the script
 */
public class ExceptionPlugin implements IPostParseTestPlugin
{
	public static final String NODE_NAME = "expectedException";
	
	@Override
	public void execute(PostTestParserPluginContext ctx) throws ParserPluginException
	{
		NodeList childNodes = ctx.getRootNodeTest().getChildNodes();
		for(int childNodeIndex = 0; childNodeIndex < childNodes.getLength(); childNodeIndex++)
		{
			if(childNodes.item(childNodeIndex).getNodeName().equalsIgnoreCase(NODE_NAME))
			{
			    int size = ctx.getTestSuite().getComponentList().size();
			    TestScript script= (TestScript) ctx.getTestSuite().getComponentList().get(size-1);
			    script.setException(childNodes.item(childNodeIndex).getTextContent());
	            //ctx.getTestAgenda().setThreadCount(maxThreads);
			}
		}
		
	}

    @Override
    public String getTagName() {
        
        return NODE_NAME;
    }
}
