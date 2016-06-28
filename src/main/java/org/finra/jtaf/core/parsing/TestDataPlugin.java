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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.exceptions.NameCollisionException;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.test.TestComponent;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.finra.jtaf.core.plugins.parsing.IPostParseTestPlugin;
import org.finra.jtaf.core.plugins.parsing.ParserPluginException;
import org.finra.jtaf.core.plugins.parsing.PostTestParserPluginContext;
import org.finra.jtaf.core.utilities.ExcelFileParser;
import org.finra.jtaf.core.utilities.StringHelper;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parser plugin to handle testdata element in a test script.
 */
public class TestDataPlugin implements IPostParseTestPlugin
{
	protected TestDigraph testDigraph;
	
	public TestDataPlugin(AutomationEngine automationEngine)
	{
		this.testDigraph = automationEngine.getTestDigraph();
	}
	
	@Override
	public String getTagName()
	{
		return "testdata";
	}

	@Override
	public void execute(PostTestParserPluginContext ctx) throws ParserPluginException
	{
		String testDataFile = getTestDataAttribute(ctx, "file");
		String testDataSheet = getTestDataAttribute(ctx, "sheet");
		List<List<String>> testData = getTestData(testDataFile, testDataSheet);
		int tsNamefromFilePosition = getTSNameFromDataFilePosition(testData);

		List<TestComponent> testComponents = ctx.getTestSuite().getComponentList();
		testComponents.remove(testComponents.size() - 1);
		for(int testDataRow = 1; testDataRow < testData.size(); testDataRow++)
		{
//			TestScript ts = null;
			TestScript ts = reparseScript(ctx);
			renameScript(ts, tsNamefromFilePosition, testData.get(testDataRow), testDataRow);
			
			setTestDataToTestScript(testData.get(0), testData.get(testDataRow), ts);
			
			addToSuite(ctx, ts);
			

//			DigraphPlugin dependenciesPlugin = DigraphPlugin.getInstance();
//			TestDigraph testDigraph = dependenciesPlugin.getTestDigraph();
			testDigraph.addVertex(new DiNode(ts));
		}
	}

	private String getTestDataAttribute(PostTestParserPluginContext ctx, String attributeName)
	{
		NodeList children = ctx.getRootNodeTest().getChildNodes();
		String result = null;

		for(int i = 0; i < children.getLength(); i++)
		{
			if(children.item(i).getNodeName().equalsIgnoreCase("testData"))
			{
				Node attribute = children.item(i).getAttributes().getNamedItem(attributeName);
				if (attribute == null) {
					continue;
				}
				result = attribute.getNodeValue();
			}
		}

		return result;
	}

	private List<List<String>> getTestData(String file, String sheet) throws ParserPluginException
	{
		if(file.endsWith(".xlsx"))
		{
			return getExcelDataFromFile(file, sheet, true);
		}

		if(file.endsWith(".xls"))
		{
			return getExcelDataFromFile(file, sheet, false);
		}

		if(file.endsWith(".csv"))
		{
			return getCSVDataFromFile(file);
		}

		throw new ParserPluginException("Oops! can't parse test data file ('" + file + "'). Supported 'xls', 'xlsx' and 'csv' extentions.");
	}

	private int getTSNameFromDataFilePosition(List<List<String>> testData)
	{
		// try to findout 'JTAF.test.name' column. Value from this column
		// necessary to add to ts name.
		if(testData != null && testData.size() > 0)
		{
			List<String> firstLine = testData.get(0);
			int pos = 0;
			for(String firstLineItem : firstLine)
			{
				if(firstLineItem != null && firstLineItem.equalsIgnoreCase("JTAF.test.name"))
				{
					return pos;
				}
				else
				{
					pos++;
				}
			}
		}
		return -1;
	}

	private final List<List<String>> getExcelDataFromFile(String testDataFile, String sheetName, boolean isXlsx) throws ParserPluginException
	{
		if(testDataFile != null && testDataFile.length() > 0)
		{
			ExcelFileParser excelFileParser = null;
			try
			{
				if(sheetName != null)
				{
					excelFileParser = new ExcelFileParser(testDataFile, sheetName, isXlsx);
				}
				else
				{
					excelFileParser = new ExcelFileParser(testDataFile, isXlsx);
				}
				return excelFileParser.parseExcelFile(isXlsx);
			}
			catch(Exception e)
			{
				throw new ParserPluginException("Oops! Can't parse excel file '" + testDataFile + "'!");
			}
		}
		return null;
	}

	private List<List<String>> getCSVDataFromFile(String file) throws ParserPluginException
	{
		List<List<String>> result = new ArrayList<List<String>>();
		CSVReader reader = null;
		try
		{
			reader = new CSVReader(new FileReader(file));
			List<String> nextLine;

			while ((nextLine = StringHelper.ArrayToList(reader.readNext())) != null)
			{
				if((nextLine != null) && (nextLine.size() > 0) && (!nextLine.get(0).startsWith("#")))
				{
					result.add(nextLine);
				}
			}
		}
		catch(Exception exception)
		{
			throw new ParserPluginException("Oops! Can't open file '" + file + "'!", exception);
		}
		finally
		{
			try
			{
				if (reader != null) {
					reader.close();
				}
			}
			catch(IOException ioException)
			{
				throw new ParserPluginException("Problem closing CSVReader", ioException);
			}
		}
		return result;
	}

	private TestScript reparseScript(PostTestParserPluginContext ctx) throws ParserPluginException
	{
		ScriptParser sp = AutomationEngine.getInstance().getScriptParser();
		try
		{
			return sp.processTestScript((Element) ctx.getRootNodeTest(), new MessageCollector());
		}
		catch(Exception exception)
		{
			throw new ParserPluginException(exception);
		}
	}

	private void addToSuite(PostTestParserPluginContext ctx, TestScript ts) throws ParserPluginException
	{
		try
		{
			ctx.getTestSuite().add(ts);
		}
		catch(NameCollisionException nameCollisionException)
		{
			throw new ParserPluginException(nameCollisionException);
		}
	}

	private void setTestDataToTestScript(List<String> title, List<String> data, TestScript ts)
	{
		for(Invocation statement : ts.getBody())
		{
			if(statement instanceof Invocation)
			{
				Map<String, Object> parameters = ((Invocation) statement).getParameters();
				for(int i = 0; i < title.size(); i++)
				{
					if(i < data.size())
					{
						parameters.put(title.get(i), data.get(i));
					}
					else
					{
						parameters.put(title.get(i), "");
					}
				}
			}
		}
	}

	private void renameScript(TestScript ts, int tsNamefromFilePosition, List<String> row, int rowNumber)
	{
		// TODO Auto-generated method stub
		
		String tsNamefromFile = "";
		if(tsNamefromFilePosition >= 0)
		{
			tsNamefromFile = "testNameFromDataFile-" + row.get(tsNamefromFilePosition);
		}
		
		String newName = ts.getName();
		newName += " [data file row #" + rowNumber + "] ; " + tsNamefromFile;
		
		ts.setName(newName);
	}
}
