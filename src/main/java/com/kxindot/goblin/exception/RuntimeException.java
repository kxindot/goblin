package com.kxindot.goblin.exception;

import static java.lang.String.format;

/**
 * @author zhaoqingjiang
 */
public class RuntimeException extends java.lang.RuntimeException {

    private static final long serialVersionUID = -2246048961749513869L;

    public RuntimeException() {
        super();
    }
    
    public RuntimeException(String message) {
        super(message);
    }
    
    public RuntimeException(Throwable cause) {
        super(cause);
    }
    
    public RuntimeException(String message, Object... args) {
        super(format(message, args));
    }
    
    public RuntimeException(Throwable cause, String message) {
        super(message, cause);
    }

    public RuntimeException(Throwable cause, String message, Object... args) {
        super(format(message, args), cause);
    }
    
}
