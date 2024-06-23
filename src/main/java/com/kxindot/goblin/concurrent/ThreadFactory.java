package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.newHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static java.lang.Thread.NORM_PRIORITY;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂.
 * 
 * @author ZhaoQingJiang
 */
public class ThreadFactory implements java.util.concurrent.ThreadFactory {

	private static Map<String, Integer> counters = newHashMap();
    private final ThreadGroup group;
    private final AtomicInteger number = new AtomicInteger(1);
    private final String name;
    private UncaughtExceptionHandler handler;
    
    public ThreadFactory() {
    	this("goblin");
    }
    
    public ThreadFactory(String name) {
        this(name, null);
    }
    
    public ThreadFactory(UncaughtExceptionHandler handler) {
    	this("goblin", handler);
    }
    
    public ThreadFactory(String name, UncaughtExceptionHandler handler) {
    	requireNotBlank(name, "name can't be null or blank");
    	SecurityManager sm = System.getSecurityManager();
        this.group = sm != null ? sm.getThreadGroup() 
                : Thread.currentThread().getThreadGroup();
        int i = count(name);
        if (i != 0) {
			name += "-" + i;
		}
        this.name = name + "-thread-";
        this.handler = handler;
    }
    
    /**
     * 获取线程工厂名称.
     * 
     * @return String
     */
    public String getName() {
		return name;
	}
    
    @Override
    public Thread newThread(Runnable r) {
    	return newThread(r, false, NORM_PRIORITY);
    }
    
    /**
     * 创建线程.
     * 
     * @param r Runnable
     * @param daemon boolean
     * @param priority int
     * @return Thread
     */
    public Thread newThread(Runnable r, boolean daemon, int priority) {
    	if (r == null) throw new NullPointerException();
    	Thread t = new Thread(group, r, name + number.getAndIncrement(), 0);
    	if (daemon) {
			t.setDaemon(true);
		} else if (t.isDaemon()) {
			t.setDaemon(false);
		}
    	if (priority != t.getPriority()) {t.setPriority(priority);}
    	if (handler != null) t.setUncaughtExceptionHandler(handler);
    	return t;
    }
    
    
    /**
     * 线程工厂名称计数.
     */
    private synchronized int count(String name) {
    	int counter = counters.getOrDefault(name, 0);
    	try {
    		return counter;
		} finally {
			counters.put(name, ++counter);
		}
    }
    
}