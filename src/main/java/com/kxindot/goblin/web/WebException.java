package com.kxindot.goblin.web;

import static com.kxindot.goblin.Objects.isNull;

/**
 * Web应用公共异常基类.
 * 
 * @author ZhaoQingJiang
 */
public abstract class WebException extends RuntimeException {

	private static final long serialVersionUID = -2772193914719536107L;
	
	private Code code;

	public WebException() {
		super();
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(Throwable cause) {
		super(cause);
	}
	
	public WebException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WebException(Code code, String message) {
		super(message);
		this.code = code;
	}
	
	public WebException(Code code, Throwable cause) {
		super(cause);
		this.code = code;
	}
	
	public WebException(Code code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public Code getCode() {
		return isNull(code) ? defaultCode() : code;
	}
	
	protected abstract Code defaultCode();
}
