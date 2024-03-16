package com.kxindot.goblin.logger;

import static com.kxindot.goblin.Objects.LF;
import static com.kxindot.goblin.Objects.countMatch;
import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.stringFormat;
import static com.kxindot.goblin.Objects.stringJoinWith;
import static com.kxindot.goblin.Objects.stringReplaceFirst;
import static com.kxindot.goblin.logger.Level.DEBUG;
import static com.kxindot.goblin.logger.Level.ERROR;
import static com.kxindot.goblin.logger.Level.INFO;
import static com.kxindot.goblin.logger.Level.TRACE;
import static com.kxindot.goblin.logger.Level.WARN;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.helpers.NOPLogger;

import com.kxindot.goblin.Objects;

/**
 * 日志对象实现
 * @author ZhaoQingJiang
 */
class LoggerImpl implements Logger {

    
    private static final String Braces = "{}";
    private static final String Specifier = "%s";
    private static final String Pattern = "%s [%s] %s [%s] => %s";
    private static final DateTimeFormatter Timer = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private final String name;
    private final org.slf4j.Logger logger;
    private boolean loggerEnable = true;
    private Level level;
    private boolean enablePattern;
    
    protected LoggerImpl(String name) {
        this(null, name, true);
    }
    
    protected LoggerImpl(String name, boolean enablePattern) {
    	this(null, name, enablePattern);
    }
    
    protected LoggerImpl(org.slf4j.Logger logger, String name) {
    	this(logger, name, true);
    }
    
    protected LoggerImpl(org.slf4j.Logger logger, String name, boolean enablePattern) {
    	if (logger == null || logger instanceof NOPLogger) {
    		this.loggerEnable = false;
    	}
    	this.logger = logger;
    	this.name = requireNotBlank(name, "name不能为空!");
    	this.enablePattern = enablePattern;
    }
    
    protected void setLocalLevel(Level level) {
        this.level = level;
    }

    @Override
    public String getName() {
        return loggerEnable ? logger.getName() : name;
    }

    @Override
    public boolean isTraceEnabled() {
        return loggerEnable ? logger.isTraceEnabled() : (int) level.value() <= TRACE.value();
    }
    
    @Override
    public void trace(String msg) {
        if (loggerEnable) {
            logger.trace(msg);
        } else {
            log(TRACE, msg);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (loggerEnable) {
            logger.trace(format, arg);
        } else {
            log(TRACE, format, arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (loggerEnable) {
            logger.trace(format, arg1, arg2);
        } else {
            log(TRACE, format, arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (loggerEnable) {
            logger.trace(format, arguments);
        } else {
            log(TRACE, format, arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (loggerEnable) {
            logger.trace(msg, t);
        } else {
            log(TRACE, msg, t);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return loggerEnable ? logger.isDebugEnabled() : (int) level.value() <= DEBUG.value();
    }

    @Override
    public void debug(String msg) {
        if (loggerEnable) {
            logger.debug(msg);
        } else {
            log(DEBUG, msg);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (loggerEnable) {
            logger.debug(format, arg);
        } else {
            log(DEBUG, format, arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (loggerEnable) {
            logger.debug(format, arg1, arg2);
        } else {
            log(DEBUG, format, arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (loggerEnable) {
            logger.debug(format, arguments);
        } else {
            log(DEBUG, format, arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (loggerEnable) {
            logger.debug(msg, t);
        } else {
            log(DEBUG, msg, t);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return loggerEnable ? logger.isInfoEnabled() : (int) level.value() <= INFO.value();
    }

    @Override
    public void info(String msg) {
        if (loggerEnable) {
            logger.info(msg);
        } else {
            log(INFO, msg);
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (loggerEnable) {
            logger.info(format, arg);
        } else {
            log(INFO, format, arg);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (loggerEnable) {
            logger.info(format, arg1, arg2);
        } else {
            log(INFO, format, arg1, arg2);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (loggerEnable) {
            logger.info(format, arguments);
        } else {
            log(INFO, format, arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (loggerEnable) {
            logger.info(msg, t);
        } else {
            log(INFO, msg, t);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return loggerEnable ? logger.isWarnEnabled() : (int) level.value() <= WARN.value();
    }

    @Override
    public void warn(String msg) {
        if (loggerEnable) {
            logger.warn(msg);
        } else {
            log(WARN, msg);
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (loggerEnable) {
            logger.warn(format, arg);
        } else {
            log(WARN, format, arg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (loggerEnable) {
            logger.warn(format, arguments);
        } else {
            log(WARN, format, arguments);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (loggerEnable) {
            logger.warn(format, arg1, arg2);
        } else {
            log(WARN, format, arg1, arg2);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (loggerEnable) {
            logger.warn(msg, t);
        } else {
            log(WARN, msg, t);
        }
    }


    @Override
    public boolean isErrorEnabled() {
        return loggerEnable ? logger.isErrorEnabled() : (int) level.value() <= ERROR.value();
    }

    @Override
    public void error(String msg) {
        if (loggerEnable) {
            logger.error(msg);
        } else {
            log(ERROR, msg);
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (loggerEnable) {
            logger.error(format, arg);
        } else {
            log(ERROR, format, arg);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (loggerEnable) {
            logger.error(format, arg1, arg2);
        } else {
            log(ERROR, format, arg1, arg2);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (loggerEnable) {
            logger.error(format, arguments);
        } else {
            log(ERROR, format, arguments);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (loggerEnable) {
            logger.error(msg, t);
        } else {
            log(ERROR, msg, t);
        }
    }

    void log(Level level, String message, Object... args) {
        if ((int) level.value() >= this.level.value()) {
            message = enablePattern ? stringFormat(Pattern, 
                    level.keyword(),
                    Thread.currentThread().getName(), 
                    Timer.format(LocalDateTime.now()), 
                    name, format(message, args)) : format(message, args);
            if (level == Level.ERROR) {
                System.err.println(message);
            } else {
                System.out.println(message);
            }
        }
    }
    
    static String format(String format, Object... args) {
        requireNotNull(format);
        if (isEmpty(args)) {
            return format;
        }
        Throwable ex = null;
        Object[] arguments = null;
        if (args.length == 1 && args[0] instanceof Throwable) {
            ex = (Throwable) args[0];
        } else if (args[args.length - 1] instanceof Throwable) {
            ex = (Throwable) args[args.length - 1];
            arguments = new Object[args.length - 1];
            System.arraycopy(args, 0, arguments, 0, args.length - 1);
        } else {
			arguments = args;
		}
        if (arguments != null) {
        	int count = countMatch(format, Braces);
            if (count > arguments.length ) {
                count = arguments.length;
            }
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof Throwable) {
                    arguments[i] = ((Throwable) arguments[i]).getMessage();
                }
            }
            format = stringReplaceFirst(format, Braces, Specifier, count);
            format = stringFormat(format, arguments);
        }
        if (ex != null) {
            format = stringJoinWith(LF, format, Objects.toString((ex)));
        }
        return format;
    }

}
