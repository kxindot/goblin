package com.kxindot.goblin.concurrent;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 异步执行同步完成线程池.
 * 
 * @author ZhaoQingJiang
 */
public interface ThreadCompletableExecutor {
	
	/**
	 * 新增待执行任务.
	 * 
	 * @param task Runnable
	 */
	void commit(Runnable task);

	/**
	 * 新增待执行任务.
	 * 
	 * @param tasks Runnable[]
	 */
	void commit(Runnable... tasks);
	
	/**
	 * 新增待执行任务.
	 * 
	 * @param tasks {@code Collection<? extends Runnable>}
	 */
	void commit(Collection<? extends Runnable> tasks);
	
	/**
	 * 执行当前已提交的所有待执行任务并阻塞至所有任务执行完成.
	 */
	void complete();
	
	/**
	 * 执行当前已提交的所有待执行任务并阻塞至所有任务执行完成或直到超时退出.
	 * 
	 * @param timeout int
	 * @param unit TimeUnit
	 */
	void complete(int timeout, TimeUnit unit);
	
}
