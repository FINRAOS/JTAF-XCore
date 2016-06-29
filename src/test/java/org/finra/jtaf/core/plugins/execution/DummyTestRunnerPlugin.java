package org.finra.jtaf.core.plugins.execution;

import org.finra.jtaf.core.plugins.execution.RunnerPluginException;
import org.finra.jtaf.core.plugins.execution.TestRunnerPluginContext;
import org.finra.jtaf.core.plugins.execution.ITestRunnerPlugin;


/**
 * Dummy implementation for testing purposes
 */
public class DummyTestRunnerPlugin implements ITestRunnerPlugin {

	TestRunnerPluginContext argsBefore;
	TestRunnerPluginContext argsAfter;

	@Override
	public void handleTestStart(TestRunnerPluginContext args)
			throws RunnerPluginException {
		argsBefore = new TestRunnerPluginContext(null, null, null);
		argsBefore = args;

	}

	@Override
	public void handleTestFinish(TestRunnerPluginContext args)
			throws RunnerPluginException {
		argsAfter = new TestRunnerPluginContext(null, null, null);
		argsAfter = args;
		try{
			if (argsAfter.getTestResult().getTestStepsDetails().size() > 0){
				if (argsAfter.getTestResult().getTestStepsDetails().get(0).getName().equalsIgnoreCase("mockstep2"))
					throw new RunnerPluginException("Testing Plugin Exceptions");
			}		
			
		}catch(RunnerPluginException e){
			throw new RunnerPluginException("Plugin Exception", e);
		}
		

	}

}
