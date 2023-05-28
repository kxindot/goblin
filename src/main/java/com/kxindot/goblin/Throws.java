package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.EMPTY_OBJ_ARRAY;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.stringFormat;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 异常相关便捷工具方法
 * @author ZhaoQingJiang
 */
public final class Throws {
    
    /** 私有构造 */
    private Throws() {}
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <E>
     * @param supplier 异常构造器引用或方法引用
     * @throws E 抛出此异常
     * @see Throwable#Throwable()
     */
    public static <E extends Throwable> void threx(Supplier<E> supplier) throws E {
        throw supplier.get();
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param message 异常信息
     * @throws E 抛出此异常
     * @see Throwable#Throwable(String)
     */
    public static <E extends Throwable> void threx(Function<String, E> function, String message) throws E {
        threx(function, message, EMPTY_OBJ_ARRAY);
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws E 抛出此异常
     * @see Throwable#Throwable(String)
     * @see #stringFormat(String, Object...)
     */
    public static <E extends Throwable> void threx(Function<String, E> function, String format, Object... args) throws E {
        throw function.apply(stringFormat(format, args));
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @throws E 抛出此异常
     * @see Throwable#Throwable(Throwable)
     */
    public static <E extends Throwable> void threx(Function<Throwable, E> function, Throwable cause) throws E {
        throw function.apply(cause);
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param message 异常信息
     * @throws E 抛出此异常
     * @see Throwable#Throwable(String, Throwable)
     */
    public static <E extends Throwable> void threx(BiFunction<String, Throwable, E> function, Throwable cause, String message) throws E {
        threx(function, cause, message, EMPTY_OBJ_ARRAY);
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws E 抛出此异常
     * @see Throwable#Throwable(String, Throwable)
     * @see #stringFormat(String, Object...)
     */
    public static <E extends Throwable> void threx(BiFunction<String, Throwable, E> function, Throwable cause, String format, Object... args) throws E {
        throw function.apply(stringFormat(format, args), cause);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param supplier 异常构造器引用或方法引用
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Throwable#Throwable()
     */
    public static <E extends Throwable> void silentThrex(Supplier<E> supplier) throws WrapperException {
        silentThrex(supplier.get());
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param message 异常信息
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static <E extends Throwable> void silentThrex(Function<String, E> function, String message) throws WrapperException {
        silentThrex(function, message, EMPTY_OBJ_ARRAY);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Objects#stringFormat(String, Object...)
     */
    public static <E extends Throwable> void silentThrex(Function<String, E> function, String format, Object... args) throws WrapperException {
        silentThrex(function.apply(stringFormat(format, args)));
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static <E extends Throwable> void silentThrex(Function<Throwable, E> function, Throwable cause) throws WrapperException {
        silentThrex(function.apply(cause));
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param message 异常信息
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static <E extends Throwable> void silentThrex(BiFunction<String, Throwable, E> function, Throwable cause, String message) throws WrapperException {
        silentThrex(function, cause, message, EMPTY_OBJ_ARRAY);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Objects#stringFormat(String, Object...)
     */
    public static <E extends Throwable> void silentThrex(BiFunction<String, Throwable, E> function, Throwable cause, String format, Object... args) throws WrapperException {
        silentThrex(function.apply(stringFormat(format, args), cause));
    }
    
    /**
     * 异常安静抛出.
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将入参异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 
     * @param exception Throwable
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static void silentThrex(Throwable exception) throws WrapperException {
        silentThrex(exception, null);
    }
    
    /**
     * 异常安静抛出.
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将入参异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 
     * @param exception Throwable
     * @param message 异常信息
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static void silentThrex(Throwable exception, String message) throws WrapperException {
        silentThrex(exception, message, EMPTY_OBJ_ARRAY);
    }

    /**
     * 异常安静抛出.
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将入参异常作为起因异常(cause),构造{@link WrapperException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link WrapperException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param exception Throwable
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws WrapperException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Objects#stringFormat(String, Object...)
     */
    public static void silentThrex(Throwable exception, String format, Object... args) throws WrapperException {
        if (isNotNull(exception)) {
            if (RuntimeException.class.isInstance(exception)) 
                throw RuntimeException.class.cast(exception);
            format = stringFormat(format, args);
            if (isBlank(format)) 
                format = exception.getMessage();
            format = stringFormat("Cause: %s, Message: %s", exception.getClass().getSimpleName(), format);
            throw new WrapperException(format, exception);
        }
    }
    
    /**
     * 判断入参异常是否是{@link WrapperException}或其子类.若入参为null,则默认返回false.
     * 
     * @param throwable Throwable
     * @return boolean
     * @see WrapperException
     */
    public static boolean isWrapperException(Throwable throwable) {
        return isNull(throwable) ? false : WrapperException.class.isInstance(throwable);
    }
    
    /**
     * 获取由{@link WrapperException}包装的起因(cause)异常.
     * 若入参异常不是{@link WrapperException}或其子类,则返回入参本身;
     * 否则,返回被包装的起因(cause)异常.
     * 
     * @param throwable Throwable
     * @return Throwable
     * @see WrapperException
     */
    public static Throwable unwrapperException(Throwable throwable) {
        return !isWrapperException(throwable) ?  throwable : throwable.getCause();
    }
    
    
    /**
     * 包装类异常,用于包装其他异常.
     * @author ZhaoQingJiang
     */
    static class WrapperException extends RuntimeException {

        private static final long serialVersionUID = -7695946876494794480L;

        public WrapperException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
