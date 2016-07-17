package org.finra.jtaf.core.commands;

import java.io.File;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.test.TestAgenda;
import org.finra.jtaf.core.model.test.TestResult;
import org.finra.jtaf.core.model.test.TestScript;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class IterateOverListTest {

    private static AutomationEngine engine=null;
    private static TestAgenda testAgenda =null;
    private static boolean setup = false;
    private static TestScript SingleItemTest = null;
    private static TestScript MultipleItemIterationTest = null;

    @Before
    public void setup() {

        engine = AutomationEngine.getInstance();

        if (engine.getTestAgenda() == null) {
            engine.buildModel(new File("testlibrary"), new File("testscripts"));
            testAgenda = engine.getTestAgenda();

          //  TraceabilityMatrix.produceTraceabilityMatrix(testAgenda);
        }


        if (!setup) {
            testAgenda = engine.getTestAgenda();

            for (TestScript testScript : testAgenda.getTestScripts()) {
                String temp = testScript.getName();
                temp = temp.replace(testScript.getFileName(), "");
                if (temp.equals("SingleItemTest")) {
                    SingleItemTest = testScript;
                }
                if (temp.equals("MultipleItemIterationTest")) {
                    MultipleItemIterationTest = testScript;
                }
            }

        }
        setup = true;
    }

    @Test
    public void testMultipleIteration() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(MultipleItemIterationTest);
        int size = tr.getTestStepsDetails().size();
        System.out.println(size);
        Assert.assertTrue(size == 4);
    }

    @Test
    public void testSingleIteration() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(SingleItemTest);
        Assert.assertTrue(tr.isTestPassed());

    }

}
