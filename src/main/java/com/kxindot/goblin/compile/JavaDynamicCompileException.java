package com.kxindot.goblin.compile;

/**
 * @author zhaoqingjiang
 */
public class JavaDynamicCompileException extends RuntimeException {

    private static final long serialVersionUID = -8106787057402508785L;

	protected JavaDynamicCompileException() {
		super();
	}

	protected JavaDynamicCompileException(String message, Throwable cause) {
		super(message, cause);
	}

	protected JavaDynamicCompileException(String message) {
		super(message);
	}

	protected JavaDynamicCompileException(Throwable cause) {
		super(cause);
	}
}
