package com.kxindot.goblin.thread;

import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kxindot.goblin.thread.task.Task;
import com.kxindot.goblin.thread.task.TaskCallback;
import com.kxindot.goblin.thread.task.TaskContext;
import com.kxindot.goblin.thread.task.TaskResultNotifier;
import com.kxindot.goblin.thread.task.TaskTransmitter;

/**
 * @author zhaoqingjiang
 */
public final class Threads {
    
    private static ThreadExecutor executor = new ThreadExecutor();

    
    public static void run(Runnable runnable) {
        executor.execute(runnable);
    }
    
    public static void run(Runnable... runnables) {
        Stream.of(runnables).forEach(r -> executor.execute(r));
    }
    
    public static void run(Collection<Runnable> runnables) {
        runnables.forEach(r -> executor.execute(r));
    }
    
    public static <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Future<T>[] submit(Callable<T>... callables) throws InterruptedException {
        List<Future<T>> list = executor.invokeAll(newArrayList(callables));
        return list.toArray(new Future[list.size()]);
    }
    
    public static <T> Collection<Future<T>> submit(Collection<Callable<T>> callables) throws InterruptedException {
        return executor.invokeAll(callables);
    }
    
    public static Future<?> submitRunnable(Runnable runnable) {
        return executor.submit(runnable);
    }
    
    public static Future<?>[] submitRunnable(Runnable... runnables) {
        Future<?>[] futures = new Future[runnables.length];
        for (int i = 0; i < futures.length; i++) {
            futures[i] = executor.submit(runnables[i]);
        }
        return futures;
    }
    
    public static Collection<Future<?>> submitRunnable(Collection<Runnable> runnables) {
        return runnables.stream().map(r -> executor.submit(r)).collect(Collectors.toList());
    }

    public static void execute(Task<?, ?> task) {
        execute(task, null, null);
    }
    
    public static <P, R> void execute(Task<P, R> task, TaskCallback<P, R> callback) {
        execute(task, callback, callback);
    }

    public static <P, R> void execute(Task<P, R> task, TaskTransmitter<P> transmitter) {
        execute(task, transmitter, null);
    }

    public static <P, R> void execute(Task<P, R> task, TaskResultNotifier<R> notifier) {
        execute(task, null, notifier);
    }
    
    public static <P, R> void execute(Task<P, R> task, TaskTransmitter<P> transmitter, TaskResultNotifier<R> notifier) {
        
    }

    
    /**
     * @author zhaoqingjiang
     */
    @SuppressWarnings("unchecked")
    static class Runner<P, R> implements Runnable {

        Task<P, R> task;
        
        Runner(Task<P, R> task) {
            this.task = requireNotNull(task);
        }

        @Override
        public void run() {
            task.run((P) TaskContext.get());
        }
        
    }
    
    /**
     * @author zhaoqingjiang
     */
    @SuppressWarnings("unchecked")
    static class Caller<P, R> implements Callable<R> {

        Task<P, R> task;
        
        Caller(Task<P, R> task) {
            this.task = requireNotNull(task);
        }

        @Override
        public R call() throws Exception {
            return task.run((P) TaskContext.get());
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
