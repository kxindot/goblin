package com.kxindot.goblin.compile;

import com.kxindot.goblin.exception.RuntimeException;

/**
 * @author zhaoqingjiang
 */
public class JavaDynamicCompileException extends RuntimeException {

    private static final long serialVersionUID = -8106787057402508785L;

    public JavaDynamicCompileException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public JavaDynamicCompileException(Throwable cause, String message) {
        super(cause, message);
    }

    public JavaDynamicCompileException(Throwable cause) {
        super(cause);
    }

    public JavaDynamicCompileException(String message, Object... args) {
        super(message, args);
    }

    public JavaDynamicCompileException(String message) {
        super(message);
    }

}
