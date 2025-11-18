package com.kxindot.goblin.concurrent;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 异步执行同步完成线程池.
 * 
 * @author ZhaoQingJiang
 */
public interface CompletableExecutorService {
	
	/**
	 * 新增待执行任务。
	 * 
	 * @param id String
	 * @param task Runnable
	 */
	void commit(String id, Runnable task);

	/**
	 * 新增待执行任务。
	 * 
	 * @param task Completable
	 */
	void commit(Completable task);
	
	/**
	 * 批量新增待执行任务。
	 * 
	 * @param tasks Completable[]
	 */
	void commit(Completable... tasks);

	/**
	 * 批量新增待执行任务。
	 * 
	 * @param tasks {@code Collection<Completable>}
	 */
	void commit(Collection<Completable> tasks);
	
	/**
	 * 将当前线程已提交的所有任务提交至线程池执行并返回任务执行结果。调用此方法会阻塞当前线程。<br>
	 * 当cancelWhenException=true时，若任意任务执行异常，方法会立即返回结果，其中还未进入线程池运行的任务将被取消执行，正在运行的任务将尝试中断执行。<br>
	 * 当cancelWhenException=false时，不论任务执行异常还是执行成功，都将等待所有任务执行完成后方法返回。
	 * 
	 * @param cancelWhenException boolean
	 * @return Completables
	 */
	Completables complete(boolean cancelWhenException);
	
	/**
	 * 将当前线程已提交的所有任务提交至线程池执行并返回任务执行结果。调用此方法会阻塞当前线程。<br>
	 * 当cancelWhenException=true时，若任意任务执行异常且未超过超时时间，方法会立即返回结果，
	 * 其中还未进入线程池运行的任务将被取消执行，正在运行的任务将尝试中断执行。<br>
	 * 当cancelWhenException=false且任务执行时间超过设置的超时时间时，方法也会立即返回结果，
	 * 其中还未进入线程池运行的任务将被取消执行，正在运行的任务也将尝试中断执行。
	 * 
	 * @param cancelWhenException boolean
	 * @param timeout long
	 * @param unit TimeUnit
	 */
	Completables complete(boolean cancelWhenException, long timeout, TimeUnit unit);
	
}
