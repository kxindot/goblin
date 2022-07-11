package com.kxindot.goblin.exception;

import static java.lang.String.format;

/**
 * @author zhaoqingjiang
 */
public class Exception extends java.lang.Exception {

    private static final long serialVersionUID = 1443592013480430383L;

    public Exception() {
        super();
    }
    
    public Exception(String message) {
        super(message);
    }
    
    public Exception(Throwable cause) {
        super(cause);
    }
    
    public Exception(String message, Object... args) {
        super(format(message, args));
    }
    
    public Exception(Throwable cause, String message) {
        super(message, cause);
    }

    public Exception(Throwable cause, String message, Object... args) {
        super(format(message, args), cause);
    }
    
}
