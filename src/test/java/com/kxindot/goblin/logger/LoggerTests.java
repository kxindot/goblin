package com.kxindot.goblin.logger;

/**
 * @author ZhaoQingJiang
 */
public class LoggerTests {

    private static Logger logger = LoggerFactory.getLogger(LoggerTests.class);
    
    public static void main(String[] args) {
        logger.info("日志Logger占位符测试: {}", "看到我就是成功");
    }
    
}
