package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.stringFormat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.test.JunitTests;

/**
 * @author ZhaoQingJiang
 */
public class ThreadCompletableExecutorTest extends JunitTests {

	public static void main(String[] args) {
		new ThreadCompletableExecutorTest().testComplete();
	}
	
	@Test
	public void testComplete() {
		ThreadPoolConfiguration configuration = new ThreadPoolConfiguration();
        configuration.setCoreSize(2);                       // 核心线程数: 2
        configuration.setMaxSize(2);                        // 最大线程数: 2
        configuration.setKeepAlive(0);               // 闲置线程存活时间: 1分钟
        configuration.setQueueSize(10);                // 队列长度: 10
        configuration.setName("nasl-source");
        ThreadExecutor executor = Threads.newThreadExecutor(Threads.newThreadPool(configuration));
        for (int i = 0; i < 12; i++) {
        	String msg = stringFormat("Task %s OK", i);
        	Runnable runner = () -> {
        		try {
        			Thread.sleep(5*1000);
        		} catch (InterruptedException e) {
        			System.err.println("Thread interrupted : " + Thread.currentThread().getName());
        		}
        		System.out.println(msg);
        	};
        	executor.commit(runner);
		}
        try {
        	executor.complete(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("线程池关闭！");
//        executor.shutdown();
	}
	
}
