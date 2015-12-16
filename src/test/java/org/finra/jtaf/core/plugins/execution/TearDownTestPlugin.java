package org.finra.jtaf.core.plugins.execution;

public class TearDownTestPlugin implements ITearDownPlugin {
    @Override
    public void handleBeforeTearDown(TearDownPluginContext tearDownPluginContext) throws RunnerPluginException {
        tearDownPluginContext.getInvocationContext().getAllObjects().put("tearDownTestActualValue".toLowerCase(), "Test successful");
    }
}
