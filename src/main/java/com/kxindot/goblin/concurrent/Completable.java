package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.util.Collections;
import java.util.List;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * 继承{@link Runnable}接口，并对其进行了包装。
 * 内置state状态位，对任务运行进行了状态管理。
 * 
 * @author ZhaoQingJiang
 */
public class Completable implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(Completable.class);
	private static final byte WAITING = 0;
	private static final byte RUNNING = 1;
	private static final byte COMPLETED = 2;
	private static final byte TIMEOUT = -1;
	private static final byte CANCELED = -2;
	private static final byte ERROR = -3;
	
	private String id;
	private Runnable runnable;
	private boolean throwError;
	private Throwable error;
	private volatile byte state = WAITING;
	private Thread thread;
	private long start = 0L;
	private long cost = 0L;
	private long timeout = -1L;
	private CompletableHandler handler;
	
	public Completable(String id, Runnable runnable) {
		this(id, runnable, true);
	}
	
	public Completable(String id, Runnable runnable, boolean throwError) {
		this.id = requireNotBlank(id, "id不能为null或空字符串");
		this.runnable = requireNotNull(runnable, "runnable不能等于null");
		this.throwError = throwError;
		this.handler = new CompletableHandler();
	}
	
	/**
	 * 任务执行是否完成。
	 * 
	 * @return boolean
	 */
	public boolean isCompleted() {
		return state == COMPLETED;
	}
	
	/**
	 * 任务执行是否超时。
	 * 
	 * @return boolean
	 */
	public boolean isTimeout() {
		return state == TIMEOUT;
	}
	
	/**
	 * 任务执行是否取消。
	 * 
	 * @return boolean
	 */
	public boolean isCanceled() {
		return state == CANCELED;
	}
	
	/**
	 * 任务执行是否发生错误。
	 * 
	 * @return boolean
	 */
	public boolean isError() {
		return state == ERROR;
	}
	
	/**
	 * 获取任务ID。
	 * 
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 获取任务执行时发生的错误。
	 * 
	 * @return Throwable
	 */
	public Throwable getError() {
		return error;
	}
	
	/**
	 * 添加任务执行监听器。
	 * 
	 * @param listener CompletableListener
	 */
	public void addListener(CompletableListener listener) {
		this.handler.add(listener);
	}
	
	/**
	 * 删除任务执行监听器。
	 * 
	 * @param listener CompletableListener
	 */
	public void removeListener(CompletableListener listener) {
		this.handler.remove(listener);
	}
	
	/**
	 * 删除所有任务执行监听器。
	 */
	public void removeListeners() {
		this.handler.listeners.clear();
	}
	
	/**
	 * 重置任务状态。若当前任务正在执行中，则调用此方法无效。
	 */
	public synchronized void reset() {
		if (state == RUNNING) {
			logger.warn("【%s】任务正在执行，无法重置状态！", id);
			return;
		}
		state = WAITING;
		start = 0L;
		timeout = -1L;
		error = null;
	}
	
	/**
	 * 取消任务的执行。只有当任务状态时waiting时，任务可以取消，其他状态调用此方法无效。 
	 */
	synchronized void cancel(long timeout) {
		if (state == WAITING) {
			state = CANCELED;
			handler.onCanceled(this);
		} else if (state == RUNNING) {
			if (timeout > 0) {
				long end = System.nanoTime();
				if (end - start > timeout) {
					state = TIMEOUT;
					cost = end - start;
					handler.onTimeout(this, timeout);
				} else {
					this.timeout = timeout;
				}
			}
			if (thread != null) {
				thread.interrupt();
				thread = null;
			}
		}
	}
	
	@Override
	public void run() {
		if (state == WAITING) {
			state = RUNNING;
			start = System.nanoTime();
			thread = Thread.currentThread();
			handler.onRunning(this);
			try {
				runnable.run();
			} catch (Throwable e) {
				error = e;
			}
			thread = null;
			cost = System.nanoTime() - start;
			synchronized (this) {
				if (error != null && state != TIMEOUT) {
					if (error instanceof InterruptedException) {
						if (timeout > 0 && cost > timeout) {
							state = TIMEOUT;
							handler.onTimeout(this, timeout);
						} else {
							state = CANCELED;
							handler.onCanceled(this);
						}
						error = null;
					} else {
						state = ERROR;
						handler.onError(this, error);
						if (throwError) {
							throw new CompletableExecuteException("任务执行异常：" + error.getMessage(), error);
						}
						logger.error("任务执行异常：{}", error.getMessage(), error);
					}
				} else if (state != TIMEOUT) {
					state = COMPLETED;
					handler.onCompleted(this);
				}
			}
			return;
		}
		throw new CompletableIllegalStateException("【" + id + "】" + "任务已执行，当前状态为：" + state + "，若需重新执行，请使用reset方法重置任务状态！");
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Completable [id=").append(id).append(", runnable=").append(runnable).append(", state=")
		        .append(state).append("]");
		return builder.toString();
	}
	
	
	/**
	 * 内置任务执行回调包装。
	 * 
	 * @author ZhaoQingJiang
	 */
	class CompletableHandler implements CompletableListener {
		
		List<CompletableListener> listeners = Collections.synchronizedList(newArrayList());
		
		/**
		 * 新增监听器。
		 * 
		 * @param listener CompletableListener
		 */
		void add(CompletableListener listener) {
			if (isNotNull(listener)) {
				listeners.add(listener);
			}
		}
		
		/**
		 * 删除监听器。
		 * 
		 * @param listener CompletableListener
		 */
		void remove(CompletableListener listener) {
			if (isNotNull(listener)) {
				listeners.remove(listener);
			}
		}
		
		@Override
		public void onRunning(Completable completable) {
			if (isNotEmpty(listeners)) {
				for (CompletableListener listener : listeners) {
					try {
						listener.onRunning(completable);
					} catch (Exception e) {
						logger.error("Completable onRunning callback error", e);
					}
				}
			}
		}
		
		@Override
		public void onCompleted(Completable completable) {
			if (isNotEmpty(listeners)) {
				for (CompletableListener listener : listeners) {
					try {
						listener.onCompleted(completable);
					} catch (Exception e) {
						logger.error("Completable onCompleted callback error", e);
					}
				}
			}
		}
		
		@Override
		public void onTimeout(Completable completable, long timeout) {
			if (isNotEmpty(listeners)) {
				for (CompletableListener listener : listeners) {
					try {
						listener.onTimeout(completable, timeout);
					} catch (Exception e) {
						logger.error("Completable onTimeout callback error", e);
					}
				}
			}
		}
		
		@Override
		public void onCanceled(Completable completable) {
			if (isNotEmpty(listeners)) {
				for (CompletableListener listener : listeners) {
					try {
						listener.onCanceled(completable);
					} catch (Exception e) {
						logger.error("Completable onCanceled callback error", e);
					}
				}
			}
		}
		
		@Override
		public void onError(Completable completable, Throwable error) {
			if (isNotEmpty(listeners)) {
				for (CompletableListener listener : listeners) {
					try {
						listener.onError(completable, error);
					} catch (Exception e) {
						logger.error("Completable onError callback error", e);
					}
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("CompletableHandler [listeners=").append(listeners).append("]");
			return builder.toString();
		}
		
	}
	
}