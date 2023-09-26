package com.kxindot.goblin.resource.property;

/**
 * @author ZhaoQingJiang
 */
public class InvalidPropertyValueException extends RuntimeException {

	private static final long serialVersionUID = 973984617784869189L;

	public InvalidPropertyValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPropertyValueException(String message) {
		super(message);
	}

	public InvalidPropertyValueException(Throwable cause) {
		super(cause);
	}
}
