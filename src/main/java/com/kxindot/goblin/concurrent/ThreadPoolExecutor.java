package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.unmodifiableEmptyList;
import static com.kxindot.goblin.concurrent.Completables.State.EXCEPTION;
import static com.kxindot.goblin.concurrent.Completables.State.TIMEOUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.kxindot.goblin.Objects;
import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * 异步线程池。
 * 
 * @author ZhaoQingJiang
 */
class ThreadPoolExecutor extends AbstractExecutorService implements ThreadExecutor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private boolean wrapped = true;
	private ThreadFactory factory;
	private ExecutorService executor;
	private ThreadPoolConfiguration configuration;
	private ThreadLocal<List<Completable>> runnables = ThreadLocal.withInitial(ArrayList::new);

	ThreadPoolExecutor() {
		this.wrapped = false;
		this.factory = new ThreadFactory();
	}

	ThreadPoolExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	ThreadPoolExecutor(ThreadPoolConfiguration configuration) {
		java.util.concurrent.ThreadPoolExecutor pool = Threads.newThreadPool(configuration);
		this.executor = pool;
		this.configuration = configuration;
		this.factory = (ThreadFactory) pool.getThreadFactory();
	}

	@Override
	public void shutdown() {
		if (wrapped)
			executor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return wrapped ? executor.shutdownNow() : unmodifiableEmptyList();
	}

	@Override
	public boolean isShutdown() {
		return wrapped ? executor.isShutdown() : true;
	}

	@Override
	public boolean isTerminated() {
		return wrapped ? executor.isTerminated() : true;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return wrapped ? executor.awaitTermination(timeout, unit) : true;
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return wrapped ? executor.submit(task) : super.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return wrapped ? executor.submit(task, result) : super.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return wrapped ? executor.submit(task) : super.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return wrapped ? executor.invokeAll(tasks) : super.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
	        throws InterruptedException {
		return wrapped ? executor.invokeAll(tasks, timeout, unit) : super.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return wrapped ? executor.invokeAny(tasks) : super.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
	        throws InterruptedException, ExecutionException, TimeoutException {
		return wrapped ? executor.invokeAny(tasks, timeout, unit) : super.invokeAny(tasks, timeout, unit);
	}

	@Override
	public void execute(Runnable command) {
		if (wrapped)
			executor.execute(command);
		else
			factory.newThread(command).start();
	}

	@Override
	public void commit(String taskId, Runnable task) {
		commit(new Completable(taskId, task));
	}
	
	@Override
	public void commit(Completable task) {
		task.getId();
		runnables.get().add(task);
	}
	
	@Override
	public void commit(Completable... tasks) {
		Arrays.stream(tasks).filter(Objects::isNotNull).forEach(this::commit);
	}
	
	@Override
	public void commit(Collection<Completable> tasks) {
		tasks.stream().filter(Objects::isNotNull).forEach(this::commit);
	}

	@Override
	public Completables complete(boolean cancelWhenException) {
		return complete(cancelWhenException, -1, null);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Completables complete(boolean cancelWhenException, long timeout, TimeUnit unit) {
		Completables completables = new Completables(runnables.get());
		int size = completables.size();
		if (size < 1) {
			logger.info("无任务提交，执行退出！");
			return completables;
		}
		CompletableErrorInterruptListener listener = null;
		if (cancelWhenException) {
			listener = new CompletableErrorInterruptListener();
		}
		CompletableFuture[] futures = new CompletableFuture[size];
		for (int i = 0; i < size; i++) {
			Completable completable = completables.get(i);
			completable.addListener(listener);
			futures[i] = CompletableFuture.runAsync(completable, this);
		}
		CompletableFuture<Void> future = CompletableFuture.allOf(futures);
		try {
			if (timeout <= 0) {
				future.get();
			} else {
				future.get(timeout, unit);
			}
			return completables;
		} catch (ExecutionException e) {
			cancel(completables, futures, -1);
			if (e.getCause() instanceof CompletableIllegalStateException) {
				throw CompletableIllegalStateException.class.cast(e);
			}
			completables.setState(EXCEPTION);
			return completables;
		} catch (TimeoutException e) {
			completables.setState(TIMEOUT);
			cancel(completables, futures, unit.toNanos(timeout));
			return completables;
		}  catch (InterruptedException e) {
			cancel(completables, futures, -1);
			if (cancelWhenException && listener.intrrupted) {
				Thread.interrupted();
				completables.setState(EXCEPTION);
				return completables;
			}
			throw new CompletableExecuteException("任务集执行被异常关闭", e);
		} finally {
			runnables.remove();
			remove(completables, listener);
		}
	}
	
	/**
	 * 取消未执行或执行中的{@link Completable}任务。
	 * 
	 * @param completables Completables
	 * @param futures CompletableFuture[]
	 * @param timeout long
	 */
	@SuppressWarnings("rawtypes")
	private void cancel(Completables completables, CompletableFuture[] futures, long timeout) {
		for (int i = 0; i < completables.size(); i++) {
			CompletableFuture taskFuture = futures[i];
			if (!taskFuture.isDone()) {
				completables.get(i).cancel(timeout);
			}
		}
	}
	
	/**
	 * 删除异常中断监听器。
	 * 
	 * @param completables Completables
	 * @param listener CompletableErrorInterruptListener
	 */
	private void remove(Completables completables, CompletableErrorInterruptListener listener) {
		if (isNull(listener)) {
			return;
		}
		for (int i = 0; i < completables.size(); i++) {
			completables.get(i).removeListener(listener);
		}
		listener.thread = null;
	}
	
	@Override
	public String toString() {
		if (isNull(factory)) {
			return executor.toString();
		}
		StringBuilder builder = new StringBuilder(factory.getName()).append("executor").append("[");
		if (isNull(configuration) && isNotNull(executor)) {
			builder.append(executor.toString());
		} else {
			builder.append("coreSize=").append(configuration.getCoreSize()).append(", maxSize=").append(configuration.getMaxSize())
	        .append(", keepAlive=").append(configuration.getKeepAlive()).append(", queueSize=").append(configuration.getQueueSize());
		}
		return builder.append("]").toString();
	}
	
	
	/**
	 * 任务异常中断监听器。
	 * 
	 * @author ZhaoQingJiang
	 */
	class CompletableErrorInterruptListener implements CompletableListener {
		
		boolean intrrupted;
		Thread thread =  Thread.currentThread();
		
		@Override
		public void onError(Completable completable, Throwable error) {
			if (thread != null 
					&& !thread.isInterrupted()) {
				intrrupted = true;
				thread.interrupt();
			}
		}
		
	}

}
