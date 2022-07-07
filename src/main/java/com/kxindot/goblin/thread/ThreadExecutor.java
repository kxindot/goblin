package com.kxindot.goblin.thread;

import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.newUnmodifiedEmptyList;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Thread Executor
 * @author zhaoqingjiang
 */
public class ThreadExecutor extends AbstractExecutorService {

    private static final Logger log = LoggerFactory.getLogger(ThreadExecutor.class);
    private static final String Default_Prefix = "goblin";
    private static final UncaughtExceptionHandler Default_Handler = (t, e) -> {
        if (log.isWarnEnabled())
            log.warn("Uncaught thread exception : {}", t.getName(), e);
    };
    private ExecutorService wrapper;
    private boolean wrapped = false;
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
        this.wrapper = requireNotNull(threadPool);
        this.wrapped = true;
    }

    @Override
    public void execute(Runnable command) {
        if (wrapped)
            wrapper.execute(command);
        else
            factory.newThread(command).start();
    }

    @Override
    public void shutdown() {
        if (wrapped)
            wrapper.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return wrapped ? wrapper.shutdownNow() : newUnmodifiedEmptyList();
    }

    @Override
    public boolean isShutdown() {
        return wrapped ? wrapper.isShutdown() : true;
    }

    @Override
    public boolean isTerminated() {
        return wrapped ? wrapper.isTerminated() : true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return wrapped ? wrapper.awaitTermination(timeout, unit) : true;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return wrapped ? wrapper.submit(task) : super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return wrapped ? wrapper.submit(task, result) : super.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return wrapped ? wrapper.submit(task) : super.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return wrapped ? wrapper.invokeAll(tasks) : super.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        return wrapped ? wrapper.invokeAll(tasks, timeout, unit) : super.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return wrapped ? wrapper.invokeAny(tasks) : super.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return wrapped ? wrapper.invokeAny(tasks, timeout, unit) : super.invokeAny(tasks, timeout, unit);
    }

}
