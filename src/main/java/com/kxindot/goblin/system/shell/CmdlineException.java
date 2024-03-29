package com.kxindot.goblin.system.shell;

/**
 * @author ZhaoQingJiang
 */
public class CmdlineException extends RuntimeException {

	private static final long serialVersionUID = 3661057816971504901L;

	CmdlineException(String message) {
		super(message);
	}
	
	CmdlineException(String message, Throwable cause) {
		super(message, cause);
	}
}