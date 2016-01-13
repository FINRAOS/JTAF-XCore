package org.finra.jtaf.core.exceptions;

/**
 * Created by mibrahim on 1/13/16.
 */
public class GetInvocationTargetException extends Exception {
    public GetInvocationTargetException(String message) {
        super(message);
    }

    public GetInvocationTargetException(String message, Exception e) {
        super(message, e);
    }
}
