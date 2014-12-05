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
package org.finra.jtaf.core.asserts;

import junit.framework.Assert;

/**
 * This class is mainly a wrapper for Junits Assert class. Instead of
 * immediately throwing an error, the error is stored in an ErrorAccumulator to
 * be thrown when needed.
 * 
 */
public class IgnoreErrorsAssert {

    public ErrorAccumulator ea;

    /**
     * Called to instantiate this class
     * 
     * @param ea
     *            the ErrorAccumulator to be used to collect all exceptions
     */
    public IgnoreErrorsAssert(ErrorAccumulator ea) {
        this.ea = ea;
    }

    /**
     * Asserts that a condition is true. If it isn't, it stores a Throwable in
     * the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if condition is false
     * @param condition
     *            the condition to be evaluated
     */
    @SuppressWarnings("deprecation")
    public void assertTrue(String message, boolean condition) {
        try {
            Assert.assertTrue(message, condition);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that a condition is true. If it isn't, it stores a Throwable in
     * the accumulator.
     * 
     * @param condition
     *            the condition to be evaluated
     */
    @SuppressWarnings("deprecation")
    public void assertTrue(boolean condition) {
        try {
            Assert.assertTrue(condition);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that a condition is false. If it isn't, it stores a Throwable in
     * the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if condition is true
     * 
     * @param condition
     *            the condition to be evaluated
     */
    @SuppressWarnings("deprecation")
    public void assertFalse(String message, boolean condition) {
        try {
            Assert.assertFalse(message, condition);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that a condition is false. If it isn't, it stores a Throwable in
     * the accumulator.
     * 
     * @param condition
     *            the condition to be evaluated
     */
    @SuppressWarnings("deprecation")
    public void assertFalse(boolean condition) {
        try {
            Assert.assertFalse(condition);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Fails a test with a Throwable being stored in the accumulator with the
     * given message.
     * 
     * @param message
     *            message to be thrown
     */
    @SuppressWarnings("deprecation")
    public void fail(String message) {
        try {
            Assert.fail(message);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Fails a test with a Throwable being stored in the accumulator with no
     * message.
     */
    @SuppressWarnings("deprecation")
    public void fail() {
        try {
            Assert.fail();
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two objects are equal. If they are not, a Throwable is
     * stored in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */

    @SuppressWarnings("deprecation")
    public void assertEquals(String message, Object expected, Object actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two objects are equal. If they are not an Throwable is
     * stored in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(Object expected, Object actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two Strings are equal. If they are not, a Throwable is
     * stored in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, String expected, String actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two Strings are equal. If they are not, a Throwable is
     * stored in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String expected, String actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two doubles are equal concerning a delta. If they are not, a
     * Throwable is stored in the accumulator with the given message. If the
     * expected value is infinity then the delta value is ignored.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     * @param delta
     *            the difference allowed between the two values
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, double expected, double actual, double delta) {
        try {
            Assert.assertEquals(message, expected, actual, delta);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two doubles are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored. If they are not, a
     * Throwable is stored in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     * @param delta
     *            the difference allowed between the two values
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(double expected, double actual, double delta) {
        try {
            Assert.assertEquals(expected, actual, delta);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two floats are equal concerning a positive delta. If they
     * are not, a Throwable is stored in the accumulator with the given message.
     * If the expected value is infinity then the delta value is ignored.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     * @param delta
     *            the difference allowed between the two values
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, float expected, float actual, float delta) {
        try {
            Assert.assertEquals(message, expected, actual, delta);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two floats are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored. If they are not, a
     * Throwable is stored in the accumulator
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     * @param delta
     *            the difference allowed between the two values
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(float expected, float actual, float delta) {
        try {
            Assert.assertEquals(expected, actual, delta);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two longs are equal. If they are not, a Throwable is stored
     * in the accumulator with the given message. If they are not, a Throwable
     * is stored in the accumulator
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, long expected, long actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two longs are equal. If they are not, a Throwable is stored
     * in the accumulator. If they are not, a Throwable is stored in the
     * accumulator
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(long expected, long actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two booleans are equal. If they are not, a Throwable is
     * stored in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, boolean expected, boolean actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two booleans are equal. If they are not, a Throwable is
     * stored in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(boolean expected, boolean actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two bytes are equal. If they are not an Throwable is thrown
     * with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, byte expected, byte actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two bytes are equal. If they are not, a Throwable is stored
     * in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(byte expected, byte actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two chars are equal. If they are not, a Throwable is stored
     * in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, char expected, char actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two chars are equal. If they are not, a Throwable is stored
     * in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(char expected, char actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two shorts are equal. If they are not, a Throwable is stored
     * in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     **/
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, short expected, short actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two shorts are equal. If they are not, a Throwable is stored
     * in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(short expected, short actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two ints are equal. If they are not, a Throwable is stored
     * in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(String message, int expected, int actual) {
        try {
            Assert.assertEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two ints are equal. If they are not, a Throwable is stored
     * in the accumulator.
     * 
     * @param expected
     *            expected value
     * @param actual
     *            actual value that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertEquals(int expected, int actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that an object isn't null. If they are not, a Throwable is stored
     * in the accumulator.
     * 
     * @param object
     *            object to be checked to see if it is null or not
     */
    @SuppressWarnings("deprecation")
    public void assertNotNull(Object object) {
        try {
            Assert.assertNotNull(object);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that an object isn't null. If they are not, a Throwable is stored
     * in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param object
     *            object to be checked to see if it is null or not
     */
    @SuppressWarnings("deprecation")
    public void assertNotNull(String message, Object object) {
        try {
            Assert.assertNotNull(message, object);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that an object is null. If they are not, a Throwable is stored in
     * the accumulator.
     * 
     * @param object
     *            Object to check to see if it is null
     */
    @SuppressWarnings("deprecation")
    public void assertNull(Object object) {
        try {
            Assert.assertNull(object);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that an object is null. If they are not, a Throwable is stored in
     * the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not equal
     * @param object
     *            object to be checked to see if it is null or not
     */
    @SuppressWarnings("deprecation")
    public void assertNull(String message, Object object) {
        try {
            Assert.assertNull(message, object);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two objects refer to the same object. If they are not, a
     * Throwable is stored in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if not the same
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertSame(String message, Object expected, Object actual) {
        try {
            Assert.assertSame(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two objects refer to the same object. If they are not, a
     * Throwable is stored in the accumulator.
     * 
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertSame(Object expected, Object actual) {
        try {
            Assert.assertSame(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two objects do not refer to the same object. If they are
     * not, a Throwable is stored in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if they are the same
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertNotSame(String message, Object expected, Object actual) {
        try {
            Assert.assertNotSame(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }

    }

    /**
     * Asserts that two objects do not refer to the same object. If they are
     * not, a Throwable is stored in the accumulator.
     * 
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public void assertNotSame(Object expected, Object actual) {
        try {
            Assert.assertNotSame(expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Asserts that two objects do refer to the same object. If they are not, a
     * Throwable is stored in the accumulator with the given message.
     * 
     * @param message
     *            message to be thrown if they are the same
     */
    @SuppressWarnings("deprecation")
    public void failSame(String message) {
        try {
            Assert.failSame(message);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Calls the Assert classes failNotSame method which creates an exception
     * using the given message and object
     * 
     * @param message
     *            message to be thrown if they are the same
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public void failNotSame(String message, Object expected, Object actual) {

        try {
            Assert.failNotSame(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Calls the Assert classes failNotEquals method which creates an exception
     * using the given message and objects
     * 
     * @param message
     *            message to be thrown if they are the same
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public void failNotEquals(String message, Object expected, Object actual) {
        try {
            Assert.failNotEquals(message, expected, actual);
        } catch (Throwable e) {
            ea.addError(e);
        }
    }

    /**
     * Calls the Assert classes format method which creates a formatted string
     * using the given message and objects to be used for an exception
     * 
     * @param message
     *            message to be thrown if they are the same
     * @param expected
     *            expected object
     * @param actual
     *            actual object that is being checked
     */
    @SuppressWarnings("deprecation")
    public String format(String message, Object expected, Object actual) {
        return Assert.format(message, expected, actual);
    }

    /**
     * Throws all of the accumulated errors.
     */
    public void endOfCommand() {
        ea.throwErrors();

    }

    /**
     * Used to check if there are any recorded errors.
     * 
     * @return returns the errors that have been recorded.
     */
    public String checkErrors() {
        return ea.getErrorMessages();
    }
}
