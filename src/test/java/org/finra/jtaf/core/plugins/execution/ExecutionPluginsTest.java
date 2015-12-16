package org.finra.jtaf.core.plugins.execution;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.core.CommandRegistry;
import org.finra.jtaf.core.DefaultAutomationClassLoader;
import org.finra.jtaf.core.IAutomationClassLoader;
import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.Interpreter;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.statement.InvocationList;
import org.finra.jtaf.core.model.test.TestResult;
import org.finra.jtaf.core.model.test.TestScript;
import org.junit.Assert;
import org.junit.Test;

public class ExecutionPluginsTest {

    private Command c1;
    private Command c2;
    private CommandRegistry cmdReg;
    private Interpreter iv;

    private DummyTestRunnerPlugin dummyTestPlugin;
    private DummyCommandRunnerPlugin dummyCommandPlugin;
    private Invocation stmt;
    private InvocationList statements;
    private TestResult tr;
    private IAutomationClassLoader automationClassLoader = new DefaultAutomationClassLoader();

    private void setUpCommands() throws Throwable {
        Class<?> targetClass1 = automationClassLoader
                .loadClass("org.finra.jtaf.core.mock.MockCommand1");
        Constructor<?> constructor = targetClass1.getConstructor(String.class);
        c1 = (Command) constructor.newInstance("mockstep1");

        Class<?> targetClass2 = automationClassLoader
                .loadClass("org.finra.jtaf.core.mock.MockCommand2");
        Constructor<?> constructor2 = targetClass2.getConstructor(String.class);
        c2 = (Command) constructor2.newInstance("mockstep2");

    }

    public void setUpRegistry() throws Throwable {
        cmdReg = new CommandRegistry();
        cmdReg.registerInvocationTarget("mockstep1", c1);
        cmdReg.registerInvocationTarget("mockstep2", c2);
    }

    private void setupInterpreter(CommandRegistry cr)
            throws NameFormatException {
        dummyTestPlugin = new DummyTestRunnerPlugin();
        dummyCommandPlugin = new DummyCommandRunnerPlugin();
        List<ITestRunnerPlugin> testRunnerPlugins = new ArrayList<ITestRunnerPlugin>();
        testRunnerPlugins.add(dummyTestPlugin);

        List<ICommandRunnerPlugin> commandRunnerPlugins = new ArrayList<ICommandRunnerPlugin>();
        commandRunnerPlugins.add(dummyCommandPlugin);

        iv = new Interpreter();
        iv.setCommandRegistry(cr);
        iv.setTestRunnerPlugins(testRunnerPlugins);
        iv.setCommandRunnerPlugins(commandRunnerPlugins);

    }

    @Test
    /* Test Success and Command Success */
    public void pluginCallbacksForTestSuccess() throws Throwable {
        setUpCommands();
        setUpRegistry();
        TestScript ts1 = new TestScript("DummyScript1", false);
        stmt = new Invocation(c2.getName());
        statements = new InvocationList();
        statements.add(stmt);

        ts1.setBody(statements);
        try {
            setupInterpreter(cmdReg);
            tr = iv.interpret(ts1);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Assert.assertEquals("DummyScript1", dummyTestPlugin.argsBefore
                .getTestScript().getName());
        Assert.assertEquals("Running", dummyTestPlugin.argsBefore
                .getTestResult().getTestStatus().toString());
        Assert.assertTrue(dummyTestPlugin.argsBefore.getIInvocationContext()
                .getTestScript().getBody().contains(stmt));
        Assert.assertEquals("mockstep2", dummyCommandPlugin.before
                .getTestStepsDetails().getName());
        Assert.assertEquals(true, dummyCommandPlugin.before
                .getTestStepsDetails().getActualResult());

        Assert.assertEquals("Passed", dummyTestPlugin.argsAfter.getTestResult()
                .getTestStatus().toString());
        Assert.assertTrue(dummyTestPlugin.argsAfter.getIInvocationContext()
                .getTestScript().getBody().contains(stmt));
        Assert.assertEquals("mockstep2", dummyCommandPlugin.after
                .getTestStepsDetails().getName());
        Assert.assertEquals(true, dummyCommandPlugin.after
                .getTestStepsDetails().getActualResult());

        Assert.assertEquals("Passed", tr.getTestStatus().toString());
    }

    @Test
	/* Test Failure */
    public void pluginCallbacksForTestFailure1() throws Throwable {
        setUpCommands();
        setUpRegistry();
        TestScript ts2 = new TestScript("DummyScript2", false);
        stmt = new Invocation("CommandDoesNotExist");
        statements = new InvocationList();
        statements.add(stmt);
        ts2.setBody(statements);
        try {
            setupInterpreter(cmdReg);
            tr = iv.interpret(ts2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Assert.assertEquals("Running", dummyTestPlugin.argsBefore
                .getTestResult().getTestStatus().toString());
        Assert.assertTrue(dummyTestPlugin.argsBefore.getIInvocationContext()
                .getTestScript().getBody().contains(stmt));

        Assert.assertEquals("Failed", dummyTestPlugin.argsAfter.getTestResult()
                .getTestStatus().toString());
        Assert.assertTrue(dummyTestPlugin.argsBefore.getIInvocationContext()
                .getTestScript().getBody().contains(stmt));

        Assert.assertEquals("Failed", tr.getTestStatus().toString());
    }

    @Test
	/* Test failure and Command failure */
    public void pluginCallbacksForTestFailure2() throws Throwable {
        setUpCommands();
        setUpRegistry();
        TestScript ts3 = new TestScript("DummyScript3", false);
        stmt = new Invocation(c1.getName());
        statements = new InvocationList();
        statements.add(stmt);
        ts3.setBody(statements);
        try {
            setupInterpreter(cmdReg);
            tr = iv.interpret(ts3);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        dummyTestPlugin.argsBefore.getIInvocationContext();
        Assert.assertEquals("Running", dummyTestPlugin.argsBefore
                .getTestResult().getTestStatus().toString());
        Assert.assertTrue(dummyTestPlugin.argsBefore.getIInvocationContext()
                .getTestScript().getBody().contains(stmt));
        Assert.assertEquals("mockstep1", dummyCommandPlugin.before
                .getTestStepsDetails().getName());
        Assert.assertEquals(true, dummyCommandPlugin.before
                .getTestStepsDetails().getActualResult());

        Assert.assertEquals("Failed", dummyTestPlugin.argsAfter.getTestResult()
                .getTestStatus().toString());
        Assert.assertTrue(dummyTestPlugin.argsBefore.getIInvocationContext()
                .getTestScript().getBody().contains(stmt));
        Assert.assertEquals("mockstep1", dummyCommandPlugin.after
                .getTestStepsDetails().getName());
        Assert.assertEquals(true, dummyCommandPlugin.after
                .getTestStepsDetails().getActualResult());

        Assert.assertEquals("Failed", tr.getTestStatus().toString());
    }

}