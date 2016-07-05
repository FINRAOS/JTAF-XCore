package org.finra.jtaf.core.asserts;

import org.junit.Assert;

import org.junit.Test;


public class AssertionFailedErrorTest {
	
	public void testAssertionFailedErrorMessage1() {
		AssertionFailedError e = new AssertionFailedError(null);
		Assert.assertEquals("", e.getMessage());
	}
	
	public void testAssertionFailedErrorMessage2() {
		AssertionFailedError e = new AssertionFailedError("This is an assertion failed");
		Assert.assertEquals("This is an assertion failed", e.getMessage());
	}
	
	@Test(expected = AssertionFailedError.class)
	public void testAssertionFailedError1() {
		AssertionFailedError e = new AssertionFailedError(null);
		Assert.assertEquals("", e.getMessage());
		throw e;
	}
	
	@Test(expected = AssertionFailedError.class)
	public void testAssertionFailedError2() {
		AssertionFailedError e = new AssertionFailedError("This is an assertion failed");
		Assert.assertEquals("This is an assertion failed", e.getMessage());
		throw e;
	}

}
