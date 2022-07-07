package com.kxindot.goblin.thread.task;

/**
 * @author zhaoqingjiang
 */
@FunctionalInterface
public interface TaskResultNotifier<T> {

    /**
     * 
     * @param thread
     * @param task
     * @param result
     */
    void notify(Thread thread, Task<?, T> task, T result);
    
    /**
     * 
     * @param thread
     * @param task
     * @param throwable
     */
    default void exception(Thread thread, Task<?, T> task, Throwable throwable) {
        System.err.printf("Caught exception on task %s, threadName: %s\n", task.getClass().getSimpleName(), thread.getName());
        throwable.printStackTrace(System.err);
    }
}
