package com.kxindot.goblin.concurrent;

/**
 * 任务执行异常。
 * 
 * @author ZhaoQingJiang
 */
public class CompletableExecuteException extends RuntimeException {

	private static final long serialVersionUID = 312059681097999880L;

	CompletableExecuteException(String message, Throwable cause) {
		super(message, cause);
	}

	CompletableExecuteException(String message) {
		super(message);
	}

	CompletableExecuteException(Throwable cause) {
		super(cause);
	}
	
}
