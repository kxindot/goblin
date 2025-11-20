package com.kxindot.goblin.concurrent;

/**
 * 任务执行监听接口。
 * 任务执行后根据任务状态执行对应回调方法。
 * 
 * @author ZhaoQingJiang
 */
public interface CompletableListener {
	
	/**
	 * 任务开始执行回调方法。
	 * 
	 * @param completable Completable
	 */
	default void onRunning(Completable completable) {
		
	}

	/**
	 * 任务执行完成回调方法。
	 * 
	 * @param completable Completable
	 */
	default void onCompleted(Completable completable) {
		
	}
	
	/**
	 * 任务执行超时回调方法。
	 * 
	 * @param completable Completable
	 * @param timeout long
	 */
	default void onTimeout(Completable completable, long timeout) {
		
	}
	
	/**
	 * 任务取消执行回调方法。
	 * 
	 * @param completable Completable
	 */
	default void onCanceled(Completable completable) {
		
	}
	
	/**
	 * 任务执行异常回调方法。
	 * 
	 * @param completable Completable
	 * @param error Throwable
	 */
	default void onError(Completable completable, Throwable error) {
		
	}
	
}
