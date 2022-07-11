package com.kxindot.goblin.thread.task;

/**
 * @author zhaoqingjiang
 */
@FunctionalInterface
public interface Task<P, R> {

    R run(P p);
}
