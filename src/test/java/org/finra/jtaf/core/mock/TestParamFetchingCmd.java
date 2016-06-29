package org.finra.jtaf.core.mock;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.junit.Assert;

public class TestParamFetchingCmd extends Command {

	public TestParamFetchingCmd(String name) throws NameFormatException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
		// TODO Auto-generated method stub
		if (getName().equalsIgnoreCase("testoptionalParameters"))
			testOptionalParameters();
		else if (getName().equalsIgnoreCase("testRequiredParameters"))
			testRequiredParameters();
		else if (getName().equalsIgnoreCase("testParameters"))
			testParameters();
	}

	private void testParameters() {
		try {
			getRequiredString("testStringRequired");
		}catch(Exception e){
			Assert.assertEquals("testParameters: missing required String attribute 'testStringRequired'", e.getMessage());
		
		}
		try {
			getRequiredInteger("testIntegerRequired");
		}catch(Exception e){
			Assert.assertEquals("testParameters: missing required integer attribute 'testIntegerRequired'", e.getMessage());
		
		}
		try {
			getRequiredBoolean("testBooleanRequired");
		}catch(Exception e){
			Assert.assertEquals("testParameters: missing required boolean attribute 'testBooleanRequired'", e.getMessage());
		
		}
		try {
			getRequiredFloat("testFloatRequired");
		}catch(Exception e){
			Assert.assertEquals("testParameters: missing required float attribute 'testFloatRequired'", e.getMessage());
		
		}
		try {
			getRequiredObject("testObjectRequired");
		}catch(Exception e){
			Assert.assertEquals("testParameters : The parameter with key [testObjectRequired] is not set.", e.getMessage());
		
		}
		getOptionalObject("testObjectOptional");
		Assert.assertTrue(getGlobalContext().get("OptString") != null);
		
		
	}

	private void testOptionalParameters(){
		Assert.assertEquals("test string", getRequiredString("testStringRequired"));
		Assert.assertEquals(1, getRequiredInteger("testIntegerRequired"));
		Assert.assertEquals(1.1f, getRequiredFloat("testFloatRequired"), 0.2);
		Assert.assertEquals(false, getRequiredBoolean("testBooleanRequired"));
		Assert.assertEquals("{1,2}", getRequiredObject("testObjectRequired"));
		Assert.assertEquals("test string optional", getOptionalString("testStringOptional"));
		Assert.assertEquals("{cat, dog}", getOptionalObject("testObjectOptional"));
		Assert.assertEquals(false, getBooleanOrDefault("testBooleanDefault", true));		
		Assert.assertEquals("not the default string", getStringOrDefault("testStringDefault", "testString"));
		Assert.assertEquals(2, getIntegerOrDefault("testIntegerDefault", 1));
		Assert.assertEquals(2.2f, getFloatOrDefault("testFloatDefault", 1.1f), 0.2);
	}

	private void testRequiredParameters() {
		Assert.assertEquals("test string required", getRequiredString("testStringRequired"));
		Assert.assertEquals(1, getRequiredInteger("testIntegerRequired"));
		Assert.assertEquals(1.1f, getRequiredFloat("testFloatRequired"), 0.2);
		Assert.assertEquals(true, getRequiredBoolean("testBooleanRequired"));
		Assert.assertEquals("{1,2}", getRequiredObject("testObjectRequired"));
		Assert.assertTrue(getOptionalString("testStringOptional") == null);
		getOptionalObject("testObjectOptional");
		Assert.assertTrue(getGlobalContext().get("OptObj") != null);
		Assert.assertEquals(true, getBooleanOrDefault("testBooleanDefault", true));		
		Assert.assertEquals("default string", getStringOrDefault("testStringDefault", "default string"));
		Assert.assertEquals(1, getIntegerOrDefault("testIntegerDefault", 1));
		Assert.assertEquals(1.1f, getFloatOrDefault("testFloatDefault", 1.1f), 0.2);
	}
}
