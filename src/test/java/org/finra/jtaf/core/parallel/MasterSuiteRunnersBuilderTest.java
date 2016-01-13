package org.finra.jtaf.core.parallel;

import org.junit.Assert;

import org.junit.Test;

public class MasterSuiteRunnersBuilderTest {

    @Test
    public void testRunnerForClass() throws Throwable {
        MasterSuiteRunnersBuilder builder = new MasterSuiteRunnersBuilder();
        Assert.assertEquals(null, builder.runnerForClass(null));
    }

}
