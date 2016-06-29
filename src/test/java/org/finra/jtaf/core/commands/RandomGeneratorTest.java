package org.finra.jtaf.core.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestResult;
import org.finra.jtaf.core.model.test.TestScript;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RandomGeneratorTest {

    private static AutomationEngine engine = null;
    private static TestAgenda testAgenda = null;
    private static boolean setup = false;
    private static TestScript generateRegexp = null;
    private static TestScript generateRegexp2 = null;
    private static TestScript generatePhone = null;
    private static TestScript generateAbbrevUSState = null;
    private static TestScript generateUSState = null;
    private static TestScript generateNumber = null;
    private static TestScript generateNumber2 = null;
    private static TestScript generateNumber3 = null;
    private static TestScript generateNumber4 = null;
    private static TestScript generateString = null;
    private static TestScript generateString2 = null;
    private static TestScript generateString3 = null;
    private static TestScript generateString4 = null;
    private static TestScript generateString5 = null;
    
    final static public List<String> abbrevUSState = new ArrayList<String>(Arrays.asList("AL", "AK", "AS", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FM", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MH", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
			"NM", "NY", "NC", "ND", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VI", "VA", "WA", "WV", "WI", "WY"));
    
    final static public List<String> USState = new ArrayList<String>(Arrays.asList("Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Federated States of Micronesia", "Florida", "Georgia", "Guam", "Hawaii",
			"Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Marshall Islands", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
			"North Carolina", "North Dakota", "Northern Mariana Islands", "Ohio", "Oklahoma", "Oregon", "Palau", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virgin Island", "Virginia", "Washington", "West Virginia",
			"Wisconsin", "Wyoming"));
	public static final String charsForRandomString = "qwertyuioplkjhgfdsazxcvbnm";

    @Before
    public void setup() {
        
        engine = AutomationEngine.getInstance();

        if (engine.getTestAgenda() == null) {
            engine.buildModel(new File("testlibrary"), new File("testscripts"));
//            engine.setDependenciesOn(false);
            testAgenda = engine.getTestAgenda();

          //  TraceabilityMatrix.produceTraceabilityMatrix(testAgenda);
        }
    
        
        if (!setup) {
            testAgenda = engine.getTestAgenda();

            for (TestScript testScript : testAgenda.getTestScripts()) {
                String temp = testScript.getName();
                temp = temp.replace(testScript.getFileName(), "");
                if (temp.equals("GenerateRegexp"))
                	generateRegexp = testScript;
                if (temp.equals("GenerateRegexp2"))
                	generateRegexp2 = testScript;
                if (temp.equals("GeneratePhone"))
                	generatePhone = testScript;
                if (temp.equals("GenerateAbbrevUSState"))
                	generateAbbrevUSState = testScript;
                if (temp.equals("GenerateUSState"))
                	generateUSState = testScript;
                if (temp.equals("GenerateNumber"))
                	generateNumber = testScript;
                if (temp.equals("GenerateNumber2"))
                	generateNumber2 = testScript;
                if (temp.equals("GenerateNumber3"))
                	generateNumber3 = testScript;
                if (temp.equals("GenerateNumber4"))
                	generateNumber4 = testScript;
                if (temp.equals("GenerateString"))
                	generateString = testScript;
                if (temp.equals("GenerateString2"))
                	generateString2 = testScript;
                if (temp.equals("GenerateString3"))
                	generateString3 = testScript;
                if (temp.equals("GenerateString4"))
                	generateString4 = testScript;
                if (temp.equals("GenerateString5"))
                	generateString5 = testScript;
            }
        }
        setup = true;
    }

    @Test
    public void testGenerateRegexp() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateRegexp);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("Regexp failed", str.matches("[abc][def]"));
    }

    @Test
    public void testGenerateRegexp2() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateRegexp2);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("Regexp failed", str.matches("[abc][def]"));
    }

    @Test
    public void testGeneratePhone() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generatePhone);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("Phone regex failed", str.matches("[0-9]{10}"));
    }

    @Test
    public void testGenerateAbbrevUSState() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateAbbrevUSState);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generated was not an abbreviated US state", abbrevUSState.contains(str));
    }
    
    @Test
    public void testGenerateUSState() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateUSState);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generated was not an abbreviated US state", USState.contains(str));
    }

    /***
     * Tests Number Generated with default length (5)
     * @throws Throwable
     */
    @Test
    public void testGenerateNumber() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateNumber);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("Number generation failed", str.matches("[0-9]{5}"));
    }
    
    /***
     * Tests Number Generated with length = 7
     * @throws Throwable
     */
    @Test
    public void testGenerateNumber2() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateNumber2);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("Number generation failed", str.matches("[0-9]{7}"));
    }
    
    /***
     * Tests Number Generated between 10 and 100
     * @throws Throwable
     */
    @Test
    public void testGenerateNumber3() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateNumber3);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        int num = Integer.parseInt(str);
        Assert.assertTrue("Number generation failed", 10 <= num && num <= 100);
    }
    
    /***
     * Test number generated with min specified but no max, so use default length (5) 
     * @throws Throwable
     */
    @Test
    public void testGenerateNumber4() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateNumber4);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("Number generation failed", str.matches("[0-9]{5}"));
    }

    /***
     * Tests String generated with default length (5)
     * @throws Throwable
     */
    @Test
    public void testGenerateString() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateString);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generation failed", str.matches("[" + charsForRandomString + "]{5}"));
    }

    /***
     * Tests String generated with length = 7
     * @throws Throwable
     */
    @Test
    public void testGenerateString2() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateString2);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generation failed", str.matches("[" + charsForRandomString + "]{7}"));
    }
    
    /**
     * Tests String with length in range from 5-10 characters
     * @throws Throwable
     */
    @Test
    public void testGenerateString3() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateString3);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generation failed", str.matches("[" + charsForRandomString + "]{5,10}"));
    }

    /**
     * Tests String with length = 7 when min length is specified without a max
     * @throws Throwable
     */
    @Test
    public void testGenerateString4() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateString4);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generation failed", str.matches("[" + charsForRandomString + "]{7}"));
    }
    
    /**
     * Tests String with default length (5) when min length is specified without a max
     * @throws Throwable
     */
    @Test
    public void testGenerateString5() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(generateString5);
        if (!tr.isTestPassed())
        	Assert.fail("JTAF Test failed: " + tr.getFailureReason());
        String str = "" + Command.getFromGlobalContext("it");
        Assert.assertTrue("String generation failed", str.matches("[" + charsForRandomString + "]{5}"));
    }

}
