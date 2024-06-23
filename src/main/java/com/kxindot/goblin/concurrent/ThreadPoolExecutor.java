package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.unmodifiableEmptyList;
import static com.kxindot.goblin.Throws.silentThrex;

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

import com.kxindot.goblin.Throws;
import com.kxindot.goblin.Throws.WrapperException;
import com.kxindot.goblin.method.MethodReference.MethodInvocationException;

/**
 * 异步线程池.
 * 
 * @author ZhaoQingJiang
 */
class ThreadPoolExecutor extends AbstractExecutorService implements ThreadExecutor {

	private boolean wrapped = true;
	private ThreadFactory factory;
	private ExecutorService executor;
	private ThreadPoolConfiguration configuration;
	private ThreadLocal<List<Runnable>> runnables = ThreadLocal.withInitial(ArrayList::new);

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
	public void commit(Runnable task) {
		requireNotNull(task, "task == null");
		runnables.get().add(task);
	}

	@Override
	public void commit(Runnable... tasks) {
		Arrays.stream(tasks).forEach(this::commit);
	}

	@Override
	public void commit(Collection<? extends Runnable> tasks) {
		tasks.forEach(this::commit);
	}

	@Override
	public void complete() {
		complete(-1, null);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void complete(int timeout, TimeUnit unit) {
		List<Runnable> list = runnables.get();
		if (isEmpty(list)) {
			return;
		}
		CompletableFuture[] futures = new CompletableFuture[list.size()];
		for (int i = 0; i < list.size(); i++) {
			futures[i] = CompletableFuture.runAsync(list.get(i), wrapped ? executor : this);
		}
		CompletableFuture<Void> future = CompletableFuture.allOf(futures);
		try {
			if (timeout <= 0) {
				future.get();
			} else {
				future.get(timeout, unit);
			}
		} catch (TimeoutException e) {
			if (!future.isDone()) {
				future.cancel(true);
				silentThrex(e, "running tasks timeout");
			}
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (Throws.isWrapperException(e)) {
				throw WrapperException.class.cast(cause);
			} else if (MethodInvocationException.class.isInstance(cause)) {
				cause = MethodInvocationException.class.cast(cause).getCause();
			}
			silentThrex(cause);
		} catch (InterruptedException e) {
			silentThrex(e, "running tasks has been interrupted");
		} finally {
			runnables.remove();
		}
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

}
