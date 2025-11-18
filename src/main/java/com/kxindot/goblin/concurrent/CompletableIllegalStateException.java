package com.kxindot.goblin.concurrent;

/**
 * 任务状态不合法异常。
 * 
 * @author ZhaoQingJiang
 */
public class CompletableIllegalStateException extends CompletableExecuteException {

	private static final long serialVersionUID = -1998162734013674345L;

	CompletableIllegalStateException(String message) {
		super(message);
	}
	
}
