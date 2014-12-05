package org.finra.jtaf.core.asserts;

import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.core.asserts.ErrorAccumulator;
import org.junit.Assert;
import org.junit.Test;

public class ErrorAccumulatorTest {
	
	@Test
	public void testErrorAccumulatorConstructAndGetName() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		Assert.assertEquals("test", ea.getName());
	}
	
	@Test
	public void testErrorAccumulatorSetAndGetName() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		Assert.assertEquals("test", ea.getName());
	}

	@Test
	public void testErrorAccumulatorAddErrorsAndGetNumErrors() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		Assert.assertTrue(ea.isEmpty());
		ea.addError(new Throwable());
		Assert.assertEquals(ea.getNumErrors(), 1);
		ea.addError(new Exception());
		Assert.assertEquals(ea.getNumErrors(), 2);
		ea.addError(new RuntimeException());
		Assert.assertEquals(ea.getNumErrors(), 3);
	}
	
	@Test
	public void testErrorAccumulatorGetErrorMessages() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		ea.addError(new Exception("This is a test message"));
		ea.addError(new RuntimeException("This is another test message"));
		ea.addError(new Exception());
		String errorMessagesExpected = "\n This is a test message\n This is another test message\n null";
		Assert.assertEquals(errorMessagesExpected, ea.getErrorMessages());
	}
	
	@Test
	public void testErrorAccumulatorGetErrorStackTraces() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		
		StackTraceElement ste1 = new StackTraceElement("Banana", "unpeel", "helloworld.txt", 47);
		StackTraceElement ste2 = new StackTraceElement("Orange", "unpeel", "helloworld.txt", 49);
		StackTraceElement[] elements = {ste1, ste2};
		Throwable t = new Throwable();
		t.setStackTrace(elements);
		ea.addError(t);
		
		StackTraceElement ste3 = new StackTraceElement("Apple", "unpeel", "helloworld.txt", 47);
		StackTraceElement ste4 = new StackTraceElement("Pear", "unpeel", "helloworld.txt", 49);
		StackTraceElement[] elements2 = {ste3, ste4};
		Throwable t2 = new Throwable();
		t2.setStackTrace(elements2);
		ea.addError(t2);
		
		
		
		String errorStackTracesExpected = "\n java.lang.Throwable\n\tat Banana.unpeel(helloworld.txt:47)\n\t" +
				"at Orange.unpeel(helloworld.txt:49)\n\n java.lang.Throwable\n\tat Apple.unpeel(helloworld.txt:47)" +
				"\n\tat Pear.unpeel(helloworld.txt:49)";
		Assert.assertEquals(errorStackTracesExpected.replaceAll("[\t\n\r]", ""), 
				ea.getErrorStackTraces().replaceAll("[\t\n\r]", ""));
	}
	
	@Test
	public void testErrorAccumulatorGetErrorStackTraceElements() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		
		StackTraceElement ste1 = new StackTraceElement("Banana", "unpeel", "helloworld.txt", 47);
		StackTraceElement ste2 = new StackTraceElement("Orange", "unpeel", "helloworld.txt", 49);
		StackTraceElement[] elements = {ste1, ste2};
		Throwable t = new Throwable();
		t.setStackTrace(elements);
		ea.addError(t);
		
		StackTraceElement ste3 = new StackTraceElement("Apple", "unpeel", "helloworld.txt", 47);
		StackTraceElement ste4 = new StackTraceElement("Pear", "unpeel", "helloworld.txt", 49);
		StackTraceElement[] elements2 = {ste3, ste4};
		Throwable t2 = new Throwable();
		t2.setStackTrace(elements2);
		ea.addError(t2);
		
		
		List<StackTraceElement[]> expected = new ArrayList<StackTraceElement[]>();
		expected.add(elements);
		expected.add(elements2);
		
		Assert.assertArrayEquals(expected.get(0), ea.getErrorStackTraceElementsList().get(0));
		Assert.assertArrayEquals(expected.get(1), ea.getErrorStackTraceElementsList().get(1));
	}
	
	@Test
	public void testThrowErrorsWhenNone() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		ea.throwErrors();
		// shouldn't throw any exceptions
	}
	
	@Test
	public void testWrappedErrorsWhenNone() {
		ErrorAccumulator ea = new ErrorAccumulator("test");
		Assert.assertEquals("Get wrapped errors wasn't null", null, ea.getWrappedErrors());
	}
	
}
