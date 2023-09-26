package com.kxindot.goblin.resource.property.resolver;

/**
 * @author ZhaoQingJiang
 */
public class PropertyResolveException extends RuntimeException {

	private static final long serialVersionUID = -7392781394792281963L;

	public PropertyResolveException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyResolveException(String message) {
		super(message);
	}

	public PropertyResolveException(Throwable cause) {
		super(cause);
	}
}
