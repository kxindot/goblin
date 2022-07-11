package com.kxindot.goblin.thread.task;

/**
 * @author zhaoqingjiang
 */
public abstract class TaskContext {

    private static ThreadLocal<Object> cache = new ThreadLocal<>();
    
    public static Object get() {
        return cache.get();
    }
    
    public static void set(Object value) {
        cache.set(value);
    }
    
    public static void remove() {
        cache.remove();
    }
}
