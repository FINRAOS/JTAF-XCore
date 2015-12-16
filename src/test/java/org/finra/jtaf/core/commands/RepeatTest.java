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


public class RepeatTest {

    private static AutomationEngine engine = null;
    private static TestAgenda testAgenda = null;
    private static boolean setup = false;
    private static TestScript complex = null;
    private static TestScript repeat = null;
    private static TestScript nestedRepeat = null;

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
                if (temp.equals("ComplexRepeat")) {
                    complex = testScript;
                }
                if (temp.equals("NestedRepeat")) {
                    nestedRepeat = testScript;
                }
                if (temp.equals("Repeat")) {
                    repeat = testScript;
                }
            }

        }
        setup = true;

    }

    @Test
    public void testRepeat() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(repeat);
        int size = tr.getTestStepsDetails().size();
        Assert.assertTrue(size == 5);
    }

    @Test
    public void testNestedRepeat() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(nestedRepeat);
        int size = tr.getTestStepsDetails().size();
        Assert.assertTrue(size == 25);
    }

    @Test
    public void testComplexRepeat() throws Throwable {
        Interpreter iv = AutomationEngine.getInstance().getInterpreter();
        TestResult tr = iv.interpret(complex);
        int size = tr.getTestStepsDetails().size();
        Assert.assertTrue(size == 30);

    }

}
