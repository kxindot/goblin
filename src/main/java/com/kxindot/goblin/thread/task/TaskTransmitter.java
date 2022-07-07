package com.kxindot.goblin.thread.task;

/**
 * @author zhaoqingjiang
 */
@FunctionalInterface
public interface TaskTransmitter<T> {

    /**
     * 在任务执行前此方法会被调用
     * @param thread 当前线程
     * @param task 当前待执行任务
     * @return T 返回值会做为当前待执行任务入参传入
     */
    T transmit(Thread thread, Task<T, ?> task);
    
}
