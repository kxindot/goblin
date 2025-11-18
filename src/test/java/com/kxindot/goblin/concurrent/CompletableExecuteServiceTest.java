package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.Objects.stringFormat;
import static com.kxindot.goblin.Throws.silentThrex;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;
import com.kxindot.goblin.random.AverageRandomChooser;
import com.kxindot.goblin.random.RandomChooser;
import com.kxindot.goblin.test.JunitTests;

/**
 * @author ZhaoQingJiang
 */
public class CompletableExecuteServiceTest extends JunitTests {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		new CompletableExecuteServiceTest().testComplete();
	}
	
	@Test
	public void testComplete() {
		logger.info("测试开始：{}", "abc");
		ThreadPoolConfiguration configuration = new ThreadPoolConfiguration();
        configuration.setCoreSize(5);                       // 核心线程数: 2
        configuration.setMaxSize(5);                        // 最大线程数: 2
        configuration.setKeepAlive(0);               // 闲置线程存活时间: 1分钟
        configuration.setQueueSize(10);                // 队列长度: 10
        configuration.setName("nasl-source");
        ThreadExecutor executor = Threads.newThreadExecutor(Threads.newThreadPool(configuration));
        RandomChooser<Long> chooser = new AverageRandomChooser<>();
        chooser.add(1 * 1000L);
        chooser.add(2 * 1000L);
        chooser.add(3 * 1000L);
        chooser.add(4 * 1000L);
        chooser.add(5 * 1000L);
        chooser.add(6 * 1000L);
        chooser.add(7 * 1000L);
        chooser.add(8 * 1000L);
        chooser.add(9 * 1000L);
        
        for (int i = 0; i < 15; i++) {
        	Runnable runner;
        	long timeout = chooser.choose();
        	if (i < 5) {
        		String msg = stringFormat("re %d", timeout);
        		runner = () -> {
        			try {
        				Thread.sleep(2*1000);
        			} catch (InterruptedException e) {
        				silentThrex(e);
        			}
        			throw new RuntimeException(msg);
        		};
			} else {
	        	String msg = stringFormat("Task %d OK, timeout %d", i, timeout);
	        	runner = () -> {
	        		try {
	        			Thread.sleep(timeout);
	        		} catch (InterruptedException e) {
	        			silentThrex(e);
	        		}
	        		System.out.println(msg);
	        	};
			}
        	executor.commit("任务" + i, runner);
		}
        
        try {
        	Completables completables = executor.complete(true, 4, TimeUnit.SECONDS);
        	for (int i = 0; i < completables.size(); i++) {
        		Completable completable = completables.get(i);
        		logger.info("任务详细：{}", completable);
			}
        	logger.info("任务集执行成功：{}", completables.isCompleted());
        	logger.info("任务集执行超时：{}", completables.isTimeout());
			completables.listTimeouts().forEach(e -> {
				logger.info("执行超时任务：{}", e.toString());
			});
			logger.info("任务集执行异常：{}", completables.isException());
        	completables.listExceptions().forEach(e -> {
        		logger.info("执行异常任务：{}", e.toString());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
        try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        logger.info("线程池关闭！");
        executor.shutdown();
	}
	
}
