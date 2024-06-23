package com.kxindot.goblin.logger;

import static com.kxindot.goblin.logger.Level.TRACE;

/**
 * 日志对象工厂类,其静态方法可获取日志对象实例
 * @author ZhaoQingJiang
 */
public class LoggerFactory {

    /**
     * 获取日志对象.
     * 
     * @param cls {@code Class<?>}
     * @return Logger
     */
    public static Logger getLogger(Class<?> cls) {
        return getLogger(cls, TRACE);
    }
    
    /**
     * 获取日志对象.
     * 
     * @param cls {@code Class<?>}
     * @return Logger
     */
    public static Logger getLogger(Class<?> cls, boolean enablePattern) {
    	return getLogger(cls, TRACE, enablePattern);
    }

    /**
     * 获取日志对象.
     * 
     * @param cls {@code Class<?>}
     * @param level Level
     * @return Logger
     */
    public static Logger getLogger(Class<?> cls, Level level) {
        org.slf4j.Logger logger = null;
        try {
            logger = org.slf4j.LoggerFactory.getLogger(cls);
        } catch (Throwable e) {
            // ignore
        }
        return getLogger(logger, cls.getName(), level, true);
    }
    
    /**
     * 获取日志对象.
     * 
     * @param cls {@code Class<?>}
     * @param level Level
     * @return Logger
     */
    private static Logger getLogger(Class<?> cls, Level level, boolean enablePattern) {
    	org.slf4j.Logger logger = null;
    	try {
    		logger = org.slf4j.LoggerFactory.getLogger(cls);
    	} catch (Throwable e) {
    		// ignore
    	}
    	return getLogger(logger, cls.getName(), level, enablePattern);
    }

    /**
     * 获取日志对象.
     * 
     * @param className String
     * @return Logger
     */
    public static Logger getLogger(String className) {
        return getLogger(className, TRACE);
    }

    /**
     * 获取日志对象.
     * 
     * @param className String
     * @param level Level
     * @return Logger
     */
    public static Logger getLogger(String className, Level level) {
        org.slf4j.Logger logger = null;
        try {
            logger = org.slf4j.LoggerFactory.getLogger(className);
        } catch (Throwable e) {
            // ignore
        }
        return getLogger(logger, className, level, true);
    }

    /**
     * 获取日志对象.
     * 
     * @param logger org.slf4j.Logger
     * @param className String
     * @param level Level
     * @return Logger
     */
    private static Logger getLogger(org.slf4j.Logger logger, String className, Level level, boolean enablePattern) {
        LoggerImpl ins = new LoggerImpl(logger, className, enablePattern);
        if (level != null) {
            ins.setLocalLevel(level);
        }
        return ins;
    }

}
