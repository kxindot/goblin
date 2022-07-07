package com.kxindot.goblin.thread;

import static com.kxindot.goblin.Objects.isBlank;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhaoQingJiang
 */
public class ThreadFactory implements java.util.concurrent.ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger number = new AtomicInteger(1);
    private final String name;
    private UncaughtExceptionHandler handler;
    
    public ThreadFactory(String prefix) {
        this(prefix, null);
    }
    
    public ThreadFactory(String prefix, UncaughtExceptionHandler handler) {
        if (isBlank(prefix)) {
            throw new IllegalArgumentException("Invalid prefix") ;
        }
        SecurityManager sm = System.getSecurityManager();
        this.group = sm != null ? sm.getThreadGroup() 
                : Thread.currentThread().getThreadGroup();
        this.name = prefix + "-thread-";
        this.handler = handler;
    }
    
    @Override
    public Thread newThread(Runnable r) {
        if (r == null) throw new NullPointerException();
        Thread t = new Thread(group, r,
                name + number.getAndIncrement(), 0);
        if (t.isDaemon()) {t.setDaemon(false);}
        if (Thread.NORM_PRIORITY != t.getPriority()) {t.setPriority(Thread.NORM_PRIORITY);}
        if (handler != null) t.setUncaughtExceptionHandler(handler);
        return t;
    }
}