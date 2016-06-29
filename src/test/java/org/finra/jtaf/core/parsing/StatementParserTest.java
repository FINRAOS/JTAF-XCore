package org.finra.jtaf.core.parsing;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.finra.jtaf.core.model.statement.InvocationList;
import org.finra.jtaf.core.parsing.exceptions.ExceptionAccumulator;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.parsing.helpers.ParserHelper;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Only handles cases not covered by other tests
public class StatementParserTest
{
	private static final String TEST_ELEMENT = "test";
	private static final String TEST_STEPS_ELEMENT = "teststeps";
	private static final String TRY_RECOVER_CLEANUP_NAME = "TryRecoverCleanup";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String FILE_NAME = "parser_testing/StatementParserTesting.xml";
	
	private static final String NO_TEST_STEPS_SCRIPT_NAME = "No test steps";
	private static final String ONE_TEST_STEP_SCRIPT_NAME = "One test step";
	private static final String TRY_RECOVER_BLOCKS_SCRIPT_NAME = "Only try and recover at top level";
	private static final String RECOVER_FIRST_BLOCK_SCRIPT_NAME = "Only recover at top level";
	private static final String CLEANUP_FIRST_BLOCK_SCRIPT_NAME = "Only cleanup at top level";
	private static final String EMPTY_TRY_RECOVER_CLEANUP_SCRIPT_NAME = "Empty TryRecoverCleanup";
	private static final String BLOCK_PARAM_EXCEPTION_SCRIPT_NAME = "Block parameter has parsing exception";
	private static final String LIST_PARAM_NO_EXCEPTION_SCRIPT_NAME = "List parameter has no parsing exception";
	private static final String LIST_PARAM_EXCEPTION_SCRIPT_NAME = "List parameter has parsing exception";
	
	private static DocumentBuilder documentBuilder = null;
	
	private StatementParser statementParser = null;
	
	@BeforeClass
	public static void beforeClass() throws Exception
	{
		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	
	@Before
	public void before()
	{
		statementParser = new StatementParser();
	}
	
	@Test
	public void testProcessStatementListNoTestSteps() throws Exception
	{
		Element testElement = getTestStepsElement(NO_TEST_STEPS_SCRIPT_NAME);
		InvocationList invocationList = statementParser.processStatementList(testElement, new MessageCollector());
		Assert.assertEquals(0, invocationList.size());
	}
	
	@Test
	public void testProcessStatementListOneTestStep() throws Exception
	{
		Element testElement = getTestStepsElement(ONE_TEST_STEP_SCRIPT_NAME);
		InvocationList invocationList = statementParser.processStatementList(testElement, new MessageCollector());
		Assert.assertEquals(1, invocationList.size());
	}
	
	@Test
	public void testProcessStatementListTryRecoverFirstBlocks() throws Exception
	{
		Element testElement = getTestStepsElement(TRY_RECOVER_BLOCKS_SCRIPT_NAME);
		InvocationList invocationList = statementParser.processStatementList(testElement, new MessageCollector());
		Assert.assertEquals(1, invocationList.size());
	}
	
	@Test(expected = UnexpectedElementException.class)
	public void testProcessStatementListRecoverFirstBlock() throws Exception
	{
		Element testElement = getTestStepsElement(RECOVER_FIRST_BLOCK_SCRIPT_NAME);
		statementParser.processStatementList(testElement, new MessageCollector());
	}
	
	@Test(expected = UnexpectedElementException.class)
	public void testProcessStatementListCleanupFirstBlock() throws Exception
	{
		Element testElement = getTestStepsElement(CLEANUP_FIRST_BLOCK_SCRIPT_NAME);
		statementParser.processStatementList(testElement, new MessageCollector());
	}
	
	@Test
	public void testProcessStatementListEmptyTryRecoverCleanup() throws Exception
	{
		Element testElement = getTestStepsElement(EMPTY_TRY_RECOVER_CLEANUP_SCRIPT_NAME);
		InvocationList invocationList = statementParser.processStatementList(testElement, new MessageCollector());
		Assert.assertEquals(1, invocationList.size());
		Assert.assertEquals(TRY_RECOVER_CLEANUP_NAME, invocationList.get(0).getTargetName());
	}
	
	// this also covers the case of a map parameter having a parsing exception
	@Test
	public void testProcessStatementListInvocationBlockParameterHasException() throws Exception
	{
		Element testElement = getTestStepsElement(BLOCK_PARAM_EXCEPTION_SCRIPT_NAME);
		try
		{
			statementParser.processStatementList(testElement, new MessageCollector());
		}
		catch(ParsingException parsingException)
		{
			ExceptionAccumulator asExceptionAccumulator = (ExceptionAccumulator) parsingException;
			ExceptionAccumulator innerExceptionAccumulator = (ExceptionAccumulator) asExceptionAccumulator.getExceptions().get(0);
			String message = "The element 'string' is missing the required attribute 'name'";
			Assert.assertEquals(message, innerExceptionAccumulator.getExceptions().get(0).getMessage());
		}
	}
	
	@Test
	public void testProcessStatementListInvocationListParameterHasNoException() throws Exception
	{
		Element testElement = getTestStepsElement(LIST_PARAM_NO_EXCEPTION_SCRIPT_NAME);
		InvocationList invocationList = statementParser.processStatementList(testElement, new MessageCollector());
		Object listParameter = invocationList.get(0).getParameters().get("listParameter");
		Assert.assertNotNull(listParameter);
		Assert.assertTrue(listParameter instanceof List);
	}
	
	@Test
	public void testProcessStatementListInvocationListParameterHasException() throws Exception
	{
		Element testElement = getTestStepsElement(LIST_PARAM_EXCEPTION_SCRIPT_NAME);
		try
		{
			statementParser.processStatementList(testElement, new MessageCollector());
		}
		catch(ParsingException parsingException)
		{
			ExceptionAccumulator asExceptionAccumulator = (ExceptionAccumulator) parsingException;
			ExceptionAccumulator innerExceptionAccumulator = (ExceptionAccumulator) asExceptionAccumulator.getExceptions().get(0);
			String message = "The element 'string' is missing the required attribute 'name'";
			Assert.assertEquals(message, innerExceptionAccumulator.getExceptions().get(0).getMessage());
		}
	}
	
	protected Element getTestStepsElement(String testName) throws Exception
	{
		Element documentElement = documentBuilder.parse(FILE_NAME).getDocumentElement();
		NodeList childNodes = documentElement.getChildNodes();
		for (int childNodeIndex = 0; childNodeIndex < childNodes.getLength(); childNodeIndex++)
		{
			Node testNode = childNodes.item(childNodeIndex);
			if (!testNode.getNodeName().equalsIgnoreCase(TEST_ELEMENT))
				continue;
			if (!testNode.getAttributes().getNamedItem(NAME_ATTRIBUTE).getTextContent().equals(testName))
				continue;
			return ParserHelper.getFirstChildElementCaseInsensitive((Element) testNode, TEST_STEPS_ELEMENT);
		}
		return null;
	}
}
