package org.finra.jtaf.core.asserts;

import org.junit.Assert;
import org.junit.Test;

public class IgnoreErrorsAssertTest {

	@Test
	public void testAssertTrueWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertTrue("This was a failure", false);
		Assert.assertEquals("\n This was a failure", iea.checkErrors());
	}
	
	@Test
	public void testAssertTrueWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertTrue("Hopefully there's no failure here", true);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertTrueWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertTrue(false);
		Assert.assertEquals("\n null", iea.checkErrors());
	}
	
	@Test
	public void testAssertTrueWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertTrue(true);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertFalseWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertFalse("Hopefully there's no failure here", false);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertFalseWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertFalse("This was a failure", true);
		Assert.assertEquals("\n This was a failure", iea.checkErrors());
	}
	
	@Test
	public void testAssertFalseWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertFalse(false);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertFalseWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertFalse(true);
		Assert.assertEquals("\n null", iea.checkErrors());
	}
	
	@Test
	public void testFailWithMessage() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.fail("Failure");
		Assert.assertEquals("\n Failure", iea.checkErrors());
	}
	
	@Test
	public void testFail() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.fail();
		Assert.assertEquals("\n null", iea.checkErrors());
	}
	
	@Test
	public void testAssertObjectEqualsWhenNull() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((Object) null, (Object) null);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertObjectEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", (Object) "", (Object) "");
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertObjectEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", (Object) "", (Object) " ");
		Assert.assertEquals("\n This was a failure expected:<> but was:< >", iea.checkErrors());
	}
	
	@Test
	public void testAssertObjectEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((Object) "", (Object) "");
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertObjectEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((Object) "", (Object) " ");
		Assert.assertEquals("\n expected:<> but was:< >", iea.checkErrors());
	}
	
	@Test
	public void testAssertStringEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", "", " ");
		Assert.assertEquals("\n This was a failure expected:<[]> but was:<[ ]>", iea.checkErrors());
	}
	
	@Test
	public void testAssertStringEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", "", "");
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertStringEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("", " ");
		Assert.assertEquals("\n expected:<[]> but was:<[ ]>", iea.checkErrors());
	}
	
	@Test
	public void testAssertStringEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("", "");
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertDoubleEqualsWhenEqual() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(.05, .05, .02);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertDoubleEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", .05, .08, .02);
		Assert.assertEquals("\n This was a failure expected:<0.05> but was:<0.08>", iea.checkErrors());
	}
	
	@Test
	public void testAssertDoubleEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", .05, .06, .02);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertDoubleEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(.05, .08, .02);
		Assert.assertEquals("\n expected:<0.05> but was:<0.08>", iea.checkErrors());
	}
	
	@Test
	public void testAssertDoubleEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(.05, .06, .02);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertFloatEqualsWhenEqual() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(.05f, .05f, .02f);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertFloatEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", .05f, .08f, .02f);
		Assert.assertEquals("\n This was a failure expected:<0.05> but was:<0.08>", iea.checkErrors());
	}
	
	@Test
	public void testAssertFloatEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", .05f, .06f, .02f);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertFloatEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(.05f, .08f, .02f);
		Assert.assertEquals("\n expected:<0.05> but was:<0.08>", iea.checkErrors());
	}
	
	@Test
	public void testAssertFloatEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(.05f, .06f, .02f);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertLongEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", (long) 100, (long) 101);
		Assert.assertEquals("\n This was a failure expected:<100> but was:<101>", iea.checkErrors());
	}
	
	@Test
	public void testAssertLongEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", (long) 100, (long) 100);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertLongEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((long) 100, (long) 101);
		Assert.assertEquals("\n expected:<100> but was:<101>", iea.checkErrors());
	}
	
	@Test
	public void testAssertLongEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((long) 100, (long) 100);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWithMessageWhenFalse1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", true, false);
		Assert.assertEquals("\n This was a failure expected:<true> but was:<false>", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWithMessageWhenFalse2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", false, true);
		Assert.assertEquals("\n This was a failure expected:<false> but was:<true>", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWithMessageWhenTrue1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", true, true);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWithMessageWhenTrue2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", false, false);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWhenFalse1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(true, false);
		Assert.assertEquals("\n expected:<true> but was:<false>", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWhenFalse2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(false, true);
		Assert.assertEquals("\n expected:<false> but was:<true>", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWhenTrue1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(true, true);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertBooleanEqualsWhenTrue2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(false, false);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertByteEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", (byte) 14, (byte) 15);
		Assert.assertEquals("\n This was a failure expected:<14> but was:<15>", iea.checkErrors());
	}
	
	@Test
	public void testAssertByteEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", (byte) 4, (byte) 4);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertByteEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((byte) 14, (byte) 15);
		Assert.assertEquals("\n expected:<14> but was:<15>", iea.checkErrors());
	}
	
	@Test
	public void testAssertByteEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((byte) 4, (byte) 4);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertCharEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", 'a', 'b');
		Assert.assertEquals("\n This was a failure expected:<a> but was:<b>", iea.checkErrors());
	}
	
	@Test
	public void testAssertCharEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", 'a', 'a');
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertCharEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals('a', 'b');
		Assert.assertEquals("\n expected:<a> but was:<b>", iea.checkErrors());
	}
	
	@Test
	public void testAssertCharEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals('a', 'a');
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertShortEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", (short) 1, (short) 2);
		Assert.assertEquals("\n This was a failure expected:<1> but was:<2>", iea.checkErrors());
	}
	
	@Test
	public void testAssertShortEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", (short) 1, (short) 1);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertShortEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((short) 1, (short) 2);
		Assert.assertEquals("\n expected:<1> but was:<2>", iea.checkErrors());
	}
	
	@Test
	public void testAssertShortEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals((short) 1, (short) 1);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertIntegerEqualsWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("This was a failure", 5, 8);
		Assert.assertEquals("\n This was a failure expected:<5> but was:<8>", iea.checkErrors());
	}
	
	@Test
	public void testAssertIntegerEqualsWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals("Hopefully there's no failure here", 5, 5);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertIntegerEqualsWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(5, 8);
		Assert.assertEquals("\n expected:<5> but was:<8>", iea.checkErrors());
	}
	
	@Test
	public void testAssertIntegerEqualsWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertEquals(5, 5);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotNullWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNotNull("This was a failure", null);
		Assert.assertEquals("\n This was a failure", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotNullWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNotNull("Hopefully there's no failure here", new Object());
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotNullWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNotNull(null);
		Assert.assertEquals("\n null", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotNullWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNotNull(new Object());
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertNullWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNull("This was a failure", new Object());
		Assert.assertEquals("\n This was a failure", iea.checkErrors());
	}
	
	@Test
	public void testAssertNullWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNull("Hopefully there's no failure here", null);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertNullWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String superman = "superman";
		iea.assertNull(superman);
		Assert.assertEquals("\n Expected: <null> but was: superman", iea.checkErrors());
	}
	
	@Test
	public void testAssertNullWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.assertNull(null);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertSameWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		String b = "tomatoh";
		iea.assertSame("This was a failure", a, b);
		Assert.assertEquals("\n This was a failure expected same:<tomato> was not:<tomatoh>", iea.checkErrors());
	}
	
	@Test
	public void testAssertSameWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Object a = new Object();
		iea.assertSame("Hopefully there's no failure here", a, a);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertSameWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		String b = "tomatoh";
		iea.assertSame(a, b);
		Assert.assertEquals("\n expected same:<tomato> was not:<tomatoh>", iea.checkErrors());
	}
	
	@Test
	public void testAssertSameWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Object a = new Object();
		iea.assertSame(a, a);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotSameWithMessageWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Object a = new Object();
		iea.assertNotSame("This was a failure", a, a);
		Assert.assertEquals("\n This was a failure expected not same", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotSameWithMessageWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Object a = new Object();
		Object b = new Object();
		iea.assertNotSame("Hopefully there's no failure here", a, b);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotSameWhenFalse() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		iea.assertNotSame(a, a);
		Assert.assertEquals("\n expected not same", iea.checkErrors());
	}
	
	@Test
	public void testAssertNotSameWhenTrue() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Object a = new Object();
		Object b = new Object();
		iea.assertNotSame(a, b);
		Assert.assertEquals("", iea.checkErrors());
	}
	
	@Test
	public void testFailSame1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.failSame("This is an error message");
		Assert.assertEquals("\n This is an error message expected not same", iea.checkErrors());
	}
	
	@Test
	public void testFailSame2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		iea.failSame(null);
		Assert.assertEquals("\n expected not same", iea.checkErrors());
	}
	
	@Test
	public void testFailNotSame1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		iea.failNotSame("This is an error message", a, a);
		Assert.assertEquals("\n This is an error message expected same:<tomato> was not:<tomato>", iea.checkErrors());
	}
	
	@Test
	public void testFailNotSame2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		iea.failNotSame(null, a, a);
		Assert.assertEquals("\n expected same:<tomato> was not:<tomato>", iea.checkErrors());
	}
	
	@Test
	public void testFailNotEquals1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		String b = "tomatoh";
		iea.failNotEquals("This is an error message", a, b);
		Assert.assertEquals("\n This is an error message expected:<tomato> but was:<tomatoh>", iea.checkErrors());
	}
	
	@Test
	public void testFailNotEquals2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		String b = "tomatoh";
		iea.failNotEquals(null, a, b);
		Assert.assertEquals("\n expected:<tomato> but was:<tomatoh>", iea.checkErrors());
	}
	
	@Test
	public void testFormat1() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Assert.assertEquals("This is an error message expected:<Superman> but was:<Batman>", iea.format("This is an error message", "Superman", "Batman"));
	}
	
	@Test
	public void testFormat2() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		Assert.assertEquals("expected:<Superman> but was:<Batman>", iea.format(null, "Superman", "Batman"));
	}
	
	@Test(expected=AssertionFailedError.class)
	public void testEndOfCommand() {
		IgnoreErrorsAssert iea = new IgnoreErrorsAssert(new ErrorAccumulator("test"));
		String a = "tomato";
		String b = "tomatoh";
		iea.failNotEquals(null, a, b);
		iea.endOfCommand();
	}
}
