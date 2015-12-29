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
package org.finra.jtaf.core.model.execution;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.finra.jtaf.core.CommandRegistry;
import org.finra.jtaf.core.DefaultAutomationClassLoader;
import org.finra.jtaf.core.IAutomationClassLoader;
import org.finra.jtaf.core.asserts.ErrorAccumulator;
import org.finra.jtaf.core.model.exceptions.MissingInvocationTargetException;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.invocationtarget.Function;
import org.finra.jtaf.core.model.invocationtarget.InvocationTarget;
import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.statement.InvocationList;
import org.finra.jtaf.core.model.test.TestResult;
import org.finra.jtaf.core.model.test.TestScript;
import org.finra.jtaf.core.model.test.TestStatus;
import org.finra.jtaf.core.model.test.TestStepsDetails;
import org.finra.jtaf.core.plugins.execution.CommandRunnerPluginContext;
import org.finra.jtaf.core.plugins.execution.ICommandRunnerPlugin;
import org.finra.jtaf.core.plugins.execution.ITearDownPlugin;
import org.finra.jtaf.core.plugins.execution.ITestRunnerPlugin;
import org.finra.jtaf.core.plugins.execution.TearDownPluginContext;
import org.finra.jtaf.core.plugins.execution.TestRunnerPluginContext;

/**
 * This class is invoked by the Automation Engine to begin execution
 */
public class Interpreter {
    private static final Logger logger = Logger.getLogger(Interpreter.class);
    private CommandRegistry commandRegistry;
    private static IInterpreterContext context;
    private TestStatus testStatus;
    public ErrorAccumulator ea;

    private List<ITestRunnerPlugin> testRunnerPlugins;
    private List<ICommandRunnerPlugin> commandRunnerPlugins;
    private List<ITearDownPlugin> tearDownPlugins;
    private IAutomationClassLoader automationClassLoader;
    private List<TestStepsDetails> testStepDetails;

    /**
     * Add plugins that should run before or after a test
     *
     * @param testRunnerPlugins => the list of test level plugins
     */
    public void setTestRunnerPlugins(List<ITestRunnerPlugin> testRunnerPlugins) {
        this.testRunnerPlugins = testRunnerPlugins;
    }

    /**
     * Add plugins that should run before or after a command
     *
     * @param commandRunnerPlugins => the list of command level plugins
     */
    public void setCommandRunnerPlugins(
            List<ICommandRunnerPlugin> commandRunnerPlugins) {
        this.commandRunnerPlugins = commandRunnerPlugins;
    }

    public void setTearDownPlugins(List<ITearDownPlugin> tearDownPlugins) {
        this.tearDownPlugins = tearDownPlugins;
    }

    /**
     * Set the command registry
     *
     * @param commandRegistry
     */
    public void setCommandRegistry(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    /**
     * Execute the commands in the testscript passed to the Interpreter. Execute
     * custom plugins that are supposed to run before and after each test or
     * each command. The test passes if this method executes without throwing an
     * exception.
     *
     * @param test =>testscript object passed by the Automation Engine
     * @throws Throwable
     */
    public final TestResult interpret(TestScript test) throws Throwable {
        logger.info(test.getFullName());
        Throwable failure = null;
        context = new CorrectiveContext();
        context.setTestScript(test);

        ea = new ErrorAccumulator("Accumulator");
        // Multithreaded use
        automationClassLoader = new DefaultAutomationClassLoader();
        testStatus = TestStatus.Running;
        testStepDetails = new ArrayList<TestStepsDetails>();
        executeTestStartPlugins(test);
        try {
            visitInvocationList(test.getBody());
            failure = ea.getWrappedErrors();
        } catch (Throwable t) {
            failure = t;
            this.testStatus = TestStatus.Failed;
        } finally {
            if (failure == null)
                this.testStatus = TestStatus.Passed;
            else {
                this.testStatus = TestStatus.Failed;
            }

            executeTestFinishPlugins();

        }
        if (failure != null
                && failure.getClass().getSimpleName()
                .equalsIgnoreCase(test.getException())) {
            failure = null;
            this.testStatus = TestStatus.Passed;
        }
        return new TestResult(this.getTestStepDetails(), testStatus, failure);
    }

    /**
     * Return the list of test steps in the current test
     *
     * @return
     */
    public List<TestStepsDetails> getTestStepDetails() {
        return testStepDetails;
    }

    /**
     * Execute the list of commands in the test
     *
     * @param invocationtList
     * @throws Throwable
     */
    public void visitInvocationList(InvocationList invocationtList)
            throws Throwable {

        try {

            for (Invocation invocation : invocationtList) {
                try {

                    visitInvocation((Invocation) invocation);
                } catch (Throwable th) {
                    if (!ea.isEmpty() && !ea.hasThrown()) {
                        ea.addError((Throwable) th);

                        ea.throwErrors();
                    } else {
                        throw th;
                    }
                }
            }
        } catch (Throwable th) {
            this.testStatus = TestStatus.Failed;
            throw th;
        }
    }

    /**
     * Invoke the command's launch API. Responsible of actual execution of the
     * command
     *
     * @param command => current command object to be executed
     * @throws Throwable
     */
    public void visitCommand(Command command) throws Throwable {
        executeCommandStartPlugins(command);
        try {
            command.launch(context, this);
        } catch (Throwable t) {
            throw t;
        } finally {
            executeCommandFinishPlugins(command);
        }

    }

    /**
     * Execute the function, if any, present in the test
     *
     * @param function
     * @throws Throwable
     */
    public void visitFunction(Function function) throws Throwable {
        this.visitInvocationList(function.getBody());

    }

    /**
     * Execute the commands in the testscript passed to the Interpreter.
     *
     * @param invocation =>testscript object passed by the Automation Engine
     * @throws Throwable
     */
    public final void visitInvocation(Invocation invocation) throws Throwable {
        if (invocation.getTargetName().equals("TryRecoverCleanup")) {
            // Ignore logging TryRecoverCleanup statement
        } else {
            logger.info(invocation.toString());
        }
        if (this.commandRegistry.containsInvocationTarget(invocation
                .getTargetName())) {
            final InvocationTarget target = this.commandRegistry
                    .getInvocationTarget(invocation.getTargetName());
            context.pushInvocation(invocation, target);
        } else {
            throw new MissingInvocationTargetException(invocation);
        }

        try {
            InvocationTarget target = getInvocationTarget(invocation);

            target.acceptInvocationTargetVisitor(this);

        } finally {

            context.popFrame();
        }
    }

    /**
     * Get the invocation target instance if it exists in the Command Registry
     * else throw exception 'Invocation not found'
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    public InvocationTarget getInvocationTarget(Invocation invocation)
            throws Throwable {
        // Check if it exists in the local map already
        InvocationTarget target = null;
        // Get the InvocationTarget from the previously mapped ones.
        InvocationTarget mappedInvocationTarget = this.commandRegistry
                .getInvocationTarget(invocation.getTargetName());

        if (mappedInvocationTarget == null) {
            throw new IllegalArgumentException("Invocation "
                    + invocation.getTargetName() + " not found");
        }

        if (mappedInvocationTarget instanceof Function) {
            target = mappedInvocationTarget;
        } else {
            // Instantiate the InvocationTarget.
            Class<?> targetClass = automationClassLoader
                    .loadClass(mappedInvocationTarget.getClass().getName());
            if (InvocationTarget.class.isAssignableFrom(targetClass)) {
                Constructor<?> constructor = targetClass
                        .getConstructor(String.class);
                target = (InvocationTarget) constructor.newInstance(invocation
                        .getTargetName());

                // This is to reload the parameters from preparsed Map of
                // InvocationTargets (ScriptParser)
                for (String s : mappedInvocationTarget.getRequiredParameters()) {
                    target.addRequiredParameter(s);
                }

                for (String s : mappedInvocationTarget.getOptionalParameters()) {
                    target.addOptionalParameter(s);
                }

                for (String s : mappedInvocationTarget.getProductions()) {
                    target.addProduction(s);
                }
            } else {
                throw new IllegalArgumentException("Invocation not found");
            }
        }
        return target;
    }

    /**
     * Add details of the invocation to the list of test steps
     */
    public void addDetails(TestStepsDetails details) {
        testStepDetails.add(details);
    }

    /**
     * Add exception to the Error Accumulator and set the Test Status to 'Failed'
     *
     * @param th
     */
    public void addError(Throwable th) {
        ea.addError(th);
        this.testStatus = TestStatus.Failed;

    }

    /**
     * Run TestRunner plugins after a test finishes execution
     */
    private void executeTestFinishPlugins() {

        if (testRunnerPlugins != null) {
            for (ITestRunnerPlugin testPlugin : testRunnerPlugins) {
                try {
                    testPlugin
                            .handleTestFinish(new TestRunnerPluginContext(
                                    context.getTestScript(), new TestResult(
                                    this.getTestStepDetails(),
                                    testStatus), context));
                } catch (Throwable e) {
                    logger.error(e);

                }

            }
        }
    }

    /**
     * Run TestRunner plugins before a test starts execution
     *
     * @param test => the current testscript in context
     */
    private void executeTestStartPlugins(TestScript test) {
        // handleTestStart
        try {
            if (testRunnerPlugins != null) {
                for (ITestRunnerPlugin testPlugin : testRunnerPlugins) {
                    testPlugin.handleTestStart(new TestRunnerPluginContext(
                            test, new TestResult(this.getTestStepDetails(),
                            testStatus), context));
                }
            }
        } catch (Throwable th) {
            logger.error(th);
        }

    }

    /**
     * Run CommandRunner plugins after a command is executed
     *
     * @param command
     */
    private void executeCommandFinishPlugins(Command command) {

        try {
            if (commandRunnerPlugins != null) {
                for (ICommandRunnerPlugin commandPlugin : commandRunnerPlugins) {
                    commandPlugin
                            .handleCommandAfter(new CommandRunnerPluginContext(
                                    new TestStepsDetails(command.getName(),
                                            command.getUsage()), context));
                }
            }
        } catch (Throwable th) {
            logger.error(th);
        }

    }

    /**
     * Run CommandRunner plugins before a command is executed
     *
     * @param command
     */
    private void executeCommandStartPlugins(Command command) {
        try {
            // handle command start
            if (commandRunnerPlugins != null) {
                for (ICommandRunnerPlugin commandPlugin : commandRunnerPlugins) {
                    commandPlugin
                            .handleCommandBefore(new CommandRunnerPluginContext(
                                    new TestStepsDetails(command.getName(),
                                            command.getUsage()), context));
                }
            }
        } catch (Throwable th) {
            logger.error(th);
        }

    }

    public void executeTearDownPlugins(Throwable failureReason, IInvocationContext invocationContext) {
        try {
            if (tearDownPlugins != null) {
                for (ITearDownPlugin tearDownPlugin : tearDownPlugins) {
                    TestScript testScript = context.getTestScript();
                    TearDownPluginContext tearDownPluginContext = new TearDownPluginContext(testScript, failureReason, invocationContext);
                    tearDownPlugin.handleBeforeTearDown(tearDownPluginContext);
                }
            }
        } catch (Throwable throwable) {
            logger.error("Error while executing teardown plugins", throwable);
        }
    }

}
