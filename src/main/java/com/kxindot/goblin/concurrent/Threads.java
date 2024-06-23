package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.defaultIfNull;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Throws.silentThrex;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 线程/线程池类工具.
 * 
 * @author ZhaoQingJiang
 */
public class Threads {
	
	private static ThreadExecutor executor = new ThreadPoolExecutor();
	
	/**
	 * 使用默认异步线程池异步运行任务: 
	 * <pre>
	 * 获取默认异步线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
	 * 
	 * @see ExecutorService#execute(Runnable)
	 * @param runnable Runnable
	 */
	public static void runAsync(Runnable runnable) {
        executor.execute(runnable);
    }
    
	/**
	 * 使用默认异步线程池异步运行任务: 
	 * <pre>
	 * 获取默认异步线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
	 * 
	 * @see ExecutorService#execute(Runnable)
	 * @param runnables Runnable[]
	 */
    public static void runAsync(Runnable... runnables) {
        Stream.of(runnables).forEach(r -> executor.execute(r));
    }
    
    /**
     * 使用默认异步线程池异步运行任务: 
	 * <pre>
	 * 获取默认异步线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ExecutorService#execute(Runnable)
     * @param runnables {@code Collection<Runnable>}
     */
    public static void runAsync(Collection<Runnable> runnables) {
        runnables.forEach(r -> executor.execute(r));
    }
    
    /**
     * 使用默认异步线程池异步运行任务: 
	 * <pre>
	 * 获取默认异步线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ExecutorService#submit(Callable)
     * @param <T>
     * @param callable Callable
     * @return Future
     */
    public static <T> Future<T> runAsync(Callable<T> callable) {
        return executor.submit(callable);
    }
    
    /**
     * 提交任务至默认异步执行同步完成线程池,
     * 可调用{@link ThreadCompletableExecutor#complete()}或{@link ThreadCompletableExecutor#complete(int, TimeUnit)}
     * 方法运行提交的所有任务: 
	 * <pre>
	 * 获取默认异步执行同步完成线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ThreadCompletableExecutor#commit(Runnable)
     * @see ThreadCompletableExecutor#complete()
     * @see ThreadCompletableExecutor#complete(int, TimeUnit)
     * @param runnable Runnable
     * @return {@link ThreadCompletableExecutor}
     */
    public static ThreadCompletableExecutor commit(Runnable runnable) {
    	executor.commit(runnable);
    	return executor;
    }
    
    /**
     * 提交任务至默认异步执行同步完成线程池,
     * 可调用{@link ThreadCompletableExecutor#complete()}或{@link ThreadCompletableExecutor#complete(int, TimeUnit)}
     * 方法运行提交的所有任务:  
	 * <pre>
	 * 获取默认异步执行同步完成线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ThreadCompletableExecutor#commit(Runnable...)
     * @see ThreadCompletableExecutor#complete()
     * @see ThreadCompletableExecutor#complete(int, TimeUnit)
     * @param runnables Runnable[]
     * @return {@link ThreadCompletableExecutor}
     */
    public static ThreadCompletableExecutor commit(Runnable... runnables) {
    	executor.commit(runnables);
    	return executor;
    }
    
    /**
     * 提交任务至默认异步执行同步完成线程池,
     * 可调用{@link ThreadCompletableExecutor#complete()}或{@link ThreadCompletableExecutor#complete(int, TimeUnit)}
     * 方法运行提交的所有任务:  
	 * <pre>
	 * 获取默认异步执行同步完成线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步执行同步完成线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ThreadCompletableExecutor#commit(Collection)
     * @see ThreadCompletableExecutor#complete()
     * @see ThreadCompletableExecutor#complete(int, TimeUnit)
     * @param runnables {@code Collection<? extends Runnable>}
     * @return {@link ThreadCompletableExecutor}
     */
    public static ThreadCompletableExecutor commit(Collection<? extends Runnable> runnables) {
    	executor.commit(runnables);
    	return executor;
    }
    
    /**
     * 使用默认异步线程池执行直至所有任务执行完成,并返回执行结果:
     * <pre>
	 * 获取默认异步线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ExecutorService#invokeAll(Collection)
     * @param <T> 执行结果类型
     * @param callables Callable[]
     * @return {@code List<Future>}
     */
    @SuppressWarnings("unchecked")
	public static <T> List<Future<T>> invokeAll(Callable<T>... callables) {
    	List<Future<T>> futures = null;
    	try {
    		futures = executor.invokeAll(newArrayList(callables));
    	} catch (InterruptedException e) {
    		silentThrex(e);
    	}
    	return futures;
    }
    
    /**
     * 使用默认异步线程池执行直至所有任务执行完成,并返回执行结果:
     * <pre>
	 * 获取默认异步线程池 : {@link Threads#getDefaulThreadExecutor()}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadExecutor)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ExecutorService)}
	 * 设置默认异步线程池 : {@link Threads#setDefaultThreadExecutor(ThreadPoolConfiguration)}
	 * </pre>
     * 
     * @see ExecutorService#invokeAll(Collection)
     * @param <T> 执行结果类型
     * @param callables {@code Collection<Callable>}
     * @return {@code List<Future>}
     */
    public static <T> List<Future<T>> invokeAll(Collection<Callable<T>> callables) {
    	List<Future<T>> futures = null;
        try {
			futures = executor.invokeAll(callables);
		} catch (InterruptedException e) {
			silentThrex(e);
		}
        return futures;
    }
    
	/**
	 * 获取{@link Threads}工具内置默认异步线程池.
	 * 
	 * @return {@link ThreadExecutor}
	 */
	public static ThreadExecutor getDefaulThreadExecutor() {
		return executor;
	}
	
	/**
	 * 创建异步线程池.
	 * 
	 * @param executor {@link ExecutorService}
	 * @return {@link ThreadExecutor}
	 */
	public static ThreadExecutor newThreadExecutor(ExecutorService executor) {
		return new ThreadPoolExecutor(requireNotNull(executor, "executor == null"));
	}
	
	/**
	 * 创建异步线程池.
	 * 
	 * @param configuration {@link ThreadPoolConfiguration}
	 * @return {@link ThreadExecutor}
	 */
	public static ThreadExecutor newThreadExecutor(ThreadPoolConfiguration configuration) {
		return new ThreadPoolExecutor(configuration);
	}
	
	/**
	 * 创建线程池{@link java.util.concurrent.ThreadPoolExecutor}.
	 * 
	 * @param configuration {@link ThreadPoolConfiguration}
	 * @return {@link java.util.concurrent.ThreadPoolExecutor}
	 */
	public static java.util.concurrent.ThreadPoolExecutor newThreadPool(ThreadPoolConfiguration configuration) {
		String name = configuration.getName();
		UncaughtExceptionHandler ueHandler = configuration.getUncaughtExceptionHandler();
		ThreadFactory factory = isBlank(name) ? new ThreadFactory(ueHandler) : new ThreadFactory(name, ueHandler);
		RejectedExecutionHandler reHandler = defaultIfNull(configuration.getRejectedExecutionHandler(),
		        new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
		return new java.util.concurrent.ThreadPoolExecutor(
				configuration.getCoreSize(), 
				configuration.getMaxSize(),
		        configuration.getKeepAlive(), 
		        TimeUnit.MILLISECONDS,
		        new LinkedBlockingQueue<>(configuration.getQueueSize()), 
		        factory, 
		        reHandler);
	}
	
	/**
	 * 设置{@link Threads}工具内置默认异步线程池.
	 * 
	 * @param configuration {@link ThreadPoolConfiguration}
	 */
	public static synchronized void setDefaultThreadExecutor(ThreadPoolConfiguration configuration) {
		setDefaultThreadExecutor(newThreadExecutor(configuration));
	}
	
	/**
	 * 设置{@link Threads}工具内置默认异步线程池.
	 * 
	 * @param executor {@link ExecutorService}
	 */
	public static synchronized void setDefaultThreadExecutor(ExecutorService executor) {
		setDefaultThreadExecutor(newThreadExecutor(executor));
	}
	
	/**
	 * 设置{@link Threads}工具内置默认异步线程池.
	 * 
	 * @param executor {@link ThreadExecutor}
	 */
	public static synchronized void setDefaultThreadExecutor(ThreadExecutor executor) {
		requireNotNull(executor, "executor == null");
		Threads.executor.shutdown();
		Threads.executor = executor;
	}

}
