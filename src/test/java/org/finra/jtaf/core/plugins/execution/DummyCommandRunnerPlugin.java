package org.finra.jtaf.core.plugins.execution;

import org.finra.jtaf.core.mock.MockCommand2;
import org.junit.Assert;


public class DummyCommandRunnerPlugin implements ICommandRunnerPlugin {

	CommandRunnerPluginContext before;
	CommandRunnerPluginContext after;
	
	@Override
	public void handleCommandBefore(CommandRunnerPluginContext args)
			throws RunnerPluginException {
	
		before = args;
		if (before.getTestStepsDetails().getName().equals("mockstep2"))
			Assert.assertEquals(false, MockCommand2.testEnd);
		
	}

	@Override
	public void handleCommandAfter(CommandRunnerPluginContext args)
			throws RunnerPluginException {
		after = args;
		if (before.getTestStepsDetails().getName().equals("mockstep2")) {
			Assert.assertEquals(true, MockCommand2.testEnd);
		
		}
			
		if (after.getTestStepsDetails().getName().equals("mockstep1"))
			Assert.assertEquals(true, after.getTestStepsDetails().getActualResult());
	}

}
