package com.kxindot.goblin.deprecated;

import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.newUnmodifiedEmptyList;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kxindot.goblin.Objects;
import com.kxindot.goblin.thread.ThreadFactory;

/**
 * @author ZhaoQingJiang
 */
@Deprecated
public class ThreadExecutor implements ExecutorService {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadExecutor.class);
    private static final String Default_Prefix = "goblin";
    private static final UncaughtExceptionHandler Default_Handler = (t, e) -> {
        if (log.isWarnEnabled()) log.warn("Uncaught thread exception : {}", t.getName(), e);
    };
    private ExecutorService pool;
    private boolean poolEnable = false;
    private ThreadFactory factory;
    
    public ThreadExecutor() {
        this(Default_Prefix);
    }
    
    public ThreadExecutor(String prefix) {
        this(prefix, Default_Handler);
    }
    
    public ThreadExecutor(String prefix, UncaughtExceptionHandler handler) {
        if (isBlank(prefix)) 
            prefix = Default_Prefix;
        this.factory = new ThreadFactory(prefix, handler);
    }
    
    public ThreadExecutor(ExecutorService threadPool) {
        this.pool = requireNotNull(threadPool);
        this.poolEnable = true;
    }
    
    @Override
    public void execute(Runnable command) {
        requireNotNull(command);
        if (poolEnable)
            pool.execute(command);
        else 
            factory.newThread(command).start();
    }

    @Override
    public void shutdown() {
        if (poolEnable) pool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return poolEnable ? pool.shutdownNow() : newUnmodifiedEmptyList();
    }

    @Override
    public boolean isShutdown() {
        return poolEnable ? pool.isShutdown() : true;
    }

    @Override
    public boolean isTerminated() {
        return poolEnable ?  pool.isTerminated() : true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return poolEnable ? pool.awaitTermination(timeout, unit) : true;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        requireNotNull(task);
        if (poolEnable) return pool.submit(task);
        RunnableFuture<T> command = new FutureTask<>(task);
        execute(command);
        return command;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (poolEnable) {
            return pool.submit(task, result);
        }
        Objects.requireNotNull(task);
        RunnableFuture<T> fTask = new FutureTask<>(task, result);
        execute(fTask);
        return fTask;
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (poolEnable) {
            return pool.submit(task);
        }
        Objects.requireNotNull(task);
        RunnableFuture<?> fTask = new FutureTask<>(task, null);
        execute(fTask);
        return fTask;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (poolEnable) {
            return pool.invokeAll(tasks);
        }
        Objects.requireNotNull(tasks);
        if (tasks.isEmpty()) return Objects.newUnmodifiedEmptyList();
        List<Future<T>> list = Objects.newArrayList(tasks.size());
        tasks.forEach(t -> list.add(submit(t)));
        return Objects.newUnmodifiedList(list);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        if (poolEnable) {
            return pool.invokeAll(tasks, timeout, unit);
        }
        throw new UnsupportedOperationException("service == null");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        if (poolEnable) {
            return pool.invokeAny(tasks);
        }
        throw new UnsupportedOperationException("service == null");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (poolEnable) {
            return pool.invokeAny(tasks, timeout, unit);
        }
        throw new UnsupportedOperationException("service == null");
    }

}
