package com.kxindot.goblin.test;

import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.defaultIfBlank;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Objects.stringRepeat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * Junit测试抽象类.
 * 
 * @author ZhaoQingJiang
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class JunitTests {
	
	private Logger logger = LoggerFactory.getLogger(getClass(), false);
    private static final String begin = stringRepeat(">", 30);
    private static final String end = stringRepeat("<", 30);
    private static final String caseLine = stringRepeat("#", 18);
    private ThreadLocal<Case> local = new ThreadLocal<>();

    @BeforeAll
    public void beforeAll() {
        logger.info(begin + getClass().getSimpleName() + begin);
    }
    
    @AfterAll
    public void afterAll() {
        logger.info(end + getClass().getSimpleName() + end);
    }
    
    @BeforeEach
    public void beforeEach() {
        
    }
    
    @AfterEach
    public void afterEach() {
        local.remove();
    }
    
    /**
     * 用例开始.
     * 
     * @param desc 用例描述
     */
    protected void caseBegin(String desc) {
    	desc = defaultIfBlank(desc, EMP);
    	logger.info("{}=> 用例开始: {}", caseLine, desc);
    	local.set(new Case(desc));
    }
    
    /**
     * 用例结束.
     */
    protected void caseEnd() {
    	Case c = local.get();
    	if (c != null) {
			logger.info("{}<= 用例结束: {}, 总耗时: {}", caseLine, c.desc, System.currentTimeMillis() - c.timestamp);
			local.remove();
		}
    }
    
    /**
     * 打印空行
     */
    protected void println() {
    	logger.info(EMP);
    }
    
    /**
     * 打印空行
     * 
     * @param count int
     */
    protected void println(int count) {
    	requireTrue(count > 0, "打印空行行数必须大于0");
    	for (int i = 0; i < count; i++) {
    		logger.info(EMP);
		}
    }
    
    /**
     * 打印信息.
     * 
     * @param message String
     */
    protected void println(String message) {
    	logger.info(message);
    }
    
    /**
     * 打印信息.
     * 
     * @param format String
     * @param args Object[]
     */
    protected void println(String format, Object... args) {
    	logger.info(format, args);
    }
    
    /**
     * 打印信息.
     * 
     * @param throwable Throwable
     */
    protected void println(Throwable throwable) {
    	println(EMP, throwable);
    }
    
    /**
     * 打印信息.
     * 
     * @param message String
     * @param throwable Throwable
     */
    protected void println(String message, Throwable throwable) {
    	logger.info(message, throwable);
    }
    
    /**
     * @author ZhaoQingJiang
     */
    private class Case {
    	String desc;
    	long timestamp;
    	
		Case(String desc) {
			this.desc = desc;
			this.timestamp = System.currentTimeMillis();
		}
    }
    
}
