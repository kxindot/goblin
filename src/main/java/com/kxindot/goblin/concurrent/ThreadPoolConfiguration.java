package com.kxindot.goblin.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * 线程池配置.
 * 
 * @author ZhaoQingJiang
 */
public class ThreadPoolConfiguration {
	
	/**
	 * 线程池名称
	 */
	private String name;
	
	/**
	 * 线程未知异常处理器
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	
	/**
	 * 核心线程数
	 */
	private int coreSize;
	
	/**
	 * 最大线程数
	 */
	private int maxSize;
	
	/**
	 * 闲置线程存活时间,单位: 毫秒
	 */
	private long keepAlive;
	
	/**
	 * 队列长度
	 */
	private int queueSize;
	
	/**
	 * 线程池拒绝执行处理器
	 */
	private RejectedExecutionHandler rejectedExecutionHandler;
	
	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return UncaughtExceptionHandler
	 */
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	/**
	 * @return int
	 */
	public int getCoreSize() {
		return coreSize;
	}

	/**
	 * @return int
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * @return long
	 */
	public long getKeepAlive() {
		return keepAlive;
	}

	/**
	 * @return int
	 */
	public int getQueueSize() {
		return queueSize;
	}

	/**
	 * @return RejectedExecutionHandler
	 */
	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return rejectedExecutionHandler;
	}

	/**
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param uncaughtExceptionHandler UncaughtExceptionHandler
	 */
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	/**
	 * @param coreSize int
	 */
	public void setCoreSize(int coreSize) {
		this.coreSize = coreSize;
	}

	/**
	 * @param maxSize int
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * @param keepAlive long
	 */
	public void setKeepAlive(long keepAlive) {
		this.keepAlive = keepAlive;
	}

	/**
	 * @param queueSize int
	 */
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	/**
	 * @param rejectedExecutionHandler RejectedExecutionHandler
	 */
	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
	}

	@Override
	public String toString() {
		return String.format("ThreadPoolConfiguration [name=%s, coreSize=%s, maxSize=%s, keepAlive=%s, queueSize=%s]",
		        name, coreSize, maxSize, keepAlive, queueSize);
	}

}
