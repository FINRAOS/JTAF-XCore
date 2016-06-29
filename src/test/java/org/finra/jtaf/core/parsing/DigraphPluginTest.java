package org.finra.jtaf.core.parsing;

import java.io.File;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestScript;
import org.junit.Assert;
import org.junit.Test;

public class DigraphPluginTest
{
	private static final String DEPENDENT_TEST = "Dependent Test with AutomationValue not Specified in Strategy";
	private static final String AUTOMATION_VALUE = "AutomationValueTest";
	
	private TestAgenda testAgenda;
	
	@Test
	public void testAutomationValueChangedWhenDependent()
	{
		AutomationEngine automationEngine = AutomationEngine.getInstance();
		if (automationEngine.getTestAgenda() == null)
			automationEngine.buildModel(new File("testlibrary"), new File("testscripts"));
		testAgenda = automationEngine.getTestAgenda();
		
		Assert.assertTrue(testAgendaIncludesTest(DEPENDENT_TEST));
		Assert.assertEquals(AUTOMATION_VALUE, getScriptByName(DEPENDENT_TEST).getAutomationValue());
	}
	
	private boolean testAgendaIncludesTest(String dependentTest)
	{
		return getScriptByName(DEPENDENT_TEST) != null;
	}
	
	private TestScript getScriptByName(String dependentTest)
	{
		for (TestScript testScript : testAgenda.getTestScripts())
			if (testScript.getName().equals(dependentTest))
				return testScript;
		return null;
	}
}
