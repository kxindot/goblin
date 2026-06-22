package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isNotBlank;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.stringFormat;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 异常工具。
 * 
 * @author ZhaoQingJiang
 */
@SuppressWarnings("unchecked")
public final class Throws {
	
	private static final Field DETAIL_MESSAGE = Reflections.findField(Throwable.class, "detailMessage");
	
	/**
	 * 获取异常堆栈格式化字符串。
	 * 
	 * @param throwable Throwable
	 * @return String
	 */
	public static String getStackTrace(Throwable throwable) {
		String stackTrace = null;
        try (StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw)) {
        	throwable.printStackTrace(pw);
            stackTrace = sw.toString();
        } catch (IOException e) {
            silentThrex(e);
        }
        return stackTrace;
	}
    
    /**
     * 生成异常实例。提供格式化异常信息能力。
     * 
     * @see Objects#stringFormat
     * @param <T>
     * @param function {@code Function<String, T>}
     * @param format String
     * @param args Object[]
     * @return T
     */
    public static <T extends Throwable> T newException(Function<String, T> function, String format, Object... args) {
    	return function.apply(stringFormat(format, args));
    }
    
    /**
     * 生成异常实例。提供格式化异常信息能力。
     * 
     * @see Objects#stringFormat
     * @param <T>
     * @param function {@code Function<String, T>}
     * @param cause Throwable
     * @param format String
     * @param args Object[]
     * @return T
     */
    public static <T extends Throwable> T newException(BiFunction<String, Throwable, T> function, Throwable cause, String format, Object... args) {
    	return function.apply(stringFormat(format, args), cause);
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <T>
     * @param supplier 异常构造器引用或方法引用
     * @throws T 抛出此异常
     * @see Throwable#Throwable()
     */
    public static <T extends Throwable> void threx(Supplier<T> supplier) throws T {
    	threx(supplier.get());
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <T>
     * @param function 异常构造器引用或方法引用
     * @param message 异常信息
     * @throws T 抛出此异常
     * @see Throwable#Throwable(String)
     */
    public static <T extends Throwable> void threx(Function<String, T> function, String message) throws T {
    	threx(function.apply(message));
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <T>
     * @param function 异常构造器引用或方法引用
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws T 抛出此异常
     * @see Throwable#Throwable(String)
     * @see #stringFormat(String, Object...)
     */
    public static <T extends Throwable> void threx(Function<String, T> function, String format, Object... args) throws T {
    	threx(newException(function, format, args));
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <T>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @throws T 抛出此异常
     * @see Throwable#Throwable(Throwable)
     */
    public static <T extends Throwable> void threx(Function<Throwable, T> function, Throwable cause) throws T {
        threx(function.apply(cause));
    }
    
    /**
     * 根据提供的引用生成异常实例并抛出.
     * 
     * @param <T>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param message 异常信息
     * @throws T 抛出此异常
     * @see Throwable#Throwable(String, Throwable)
     */
    public static <T extends Throwable> void threx(BiFunction<String, Throwable, T> function, Throwable cause, String message) throws T {
        threx(function.apply(message, cause));
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
        threx(newException(function, cause, format, args));
    }
    
    
    /**
     * 剔除异常中Throws类相关栈帧并抛出异常。
     * 
     * @param <T>
     * @param throwable T
     * @throws T
     */
    private static <T extends Throwable> void threx(T throwable) throws T {
    	excludeStackFramesAtTop(throwable, Throws.class);
    	throw throwable;
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param supplier 异常构造器引用或方法引用
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Throwable#Throwable()
     */
	public static <T extends Throwable> void silentThrex(Supplier<Throwable> supplier) throws T {
    	Throwable throwable = supplier.get();
		throw (T) excludeStackFrames(throwable, Throws.class);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param message 异常信息
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
	public static <T extends Throwable> void silentThrex(Function<String, Throwable> function, String message) throws T {
    	Throwable throwable = function.apply(message);
    	throw (T) excludeStackFrames(throwable, Throws.class);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Objects#stringFormat(String, Object...)
     */
    public static <T extends Throwable> void silentThrex(Function<String, Throwable> function, String format, Object... args) throws T {
    	Throwable throwable = function.apply(stringFormat(format, args));
    	throw (T) excludeStackFrames(throwable, Throws.class);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static <T extends Throwable> void silentThrex(Function<Throwable, Throwable> function, Throwable cause) throws T {
    	Throwable throwable = function.apply(cause);
    	throw (T) excludeStackFrames(throwable, Throws.class);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param message 异常信息
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static <T extends Throwable> void silentThrex(BiFunction<String, Throwable, Throwable> function, Throwable cause, String message) throws T {
    	Throwable throwable = function.apply(message, cause);
    	throw (T) excludeStackFrames(throwable, Throws.class);
    }
    
    /**
     * 异常安静抛出.根据提供的引用生成异常实例并抛出.</br>
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将生成的异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param <E>
     * @param function 异常构造器引用或方法引用
     * @param cause 起因异常
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Objects#stringFormat(String, Object...)
     */
    public static <T extends Throwable> void silentThrex(BiFunction<String, Throwable, Throwable> function, Throwable cause, String format, Object... args) throws T {
    	Throwable throwable = function.apply(stringFormat(format, args), cause);
    	throw (T) excludeStackFrames(throwable, Throws.class);
    }
    
    /**
     * 异常安静抛出.
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将入参异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 
     * @param exception Throwable
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
	public static <T extends Throwable> void silentThrex(Throwable exception) throws T {
    	throw (T) exception;
    }
    
    /**
     * 异常安静抛出.
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将入参异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 
     * @param exception Throwable
     * @param message 异常信息
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     */
    public static <T extends Throwable> void silentThrex(Throwable exception, String message) throws T {
    	String detailMessage = exception.getMessage();
    	detailMessage = isBlank(detailMessage) ? message : stringFormat("%s; %s", message, detailMessage);
    	Reflections.setValue(exception, DETAIL_MESSAGE, detailMessage);
    	throw (T) exception;
    }
    

    /**
     * 异常安静抛出.
     * 此方法旨在抛出{@link Exception}类型的异常时,不引起编译器强制检查.</br>
     * 实现方法为: 将入参异常作为起因异常(cause),构造{@link SilentException}实例,并抛出此{@link RuntimeException}子类.</br>
     * 在上层捕获到{@link SilentException}异常后,可用{@link #isWrapperException(Throwable)}和{@link #unwrapperException(Throwable)}方法判断及获取起因异常(cause).</br>
     * 异常信息由{@link #stringFormat(String, Object...)}方法合成,
     * 即format及args参数需遵循此方法规范.
     * 
     * @param exception Throwable
     * @param format 格式化异常信息
     * @param args 格式化异常信息参数
     * @throws SilentException
     * @see #isWrapperException(Throwable)
     * @see #unwrapperException(Throwable)
     * @see Objects#stringFormat(String, Object...)
     */
    public static <T extends Throwable> void silentThrex(Throwable exception, String format, Object... args) throws T {
        silentThrex(exception, stringFormat(format, args));
    }
    
    
    /**
     * 剔除异常堆栈中，从栈顶开始指定类的所有栈帧。若栈顶不匹配类，则不做任何操作。
     * 
     * @param throwable Throwable
     * @param excludeClass {@code Class<?>}
     */
    public static Throwable excludeStackFramesAtTop(Throwable throwable, Class<?> excludeClass) {
    	return excludeStackFramesAtTop(throwable, excludeClass, EMP);
    }
    
    /**
     * 剔除异常堆栈中，从栈顶开始指定类方法的栈帧。若栈顶不匹配类及方法，则不做任何操作。
     * 
     * @param throwable Throwable
     * @param excludeClass {@code Class<?>}
     * @param excludeClassMethod String
     */
    public static Throwable excludeStackFramesAtTop(Throwable throwable, Class<?> excludeClass, String excludeClassMethod) {
    	if (isNull(excludeClass)) {
			return throwable;
		}
    	final String name = excludeClass.getName();
    	return excludeStackFramesAtTop(throwable, frame -> {return isExcludeFrame(frame, name, excludeClassMethod);});
    }
    
    /**
     * 剔除异常堆栈中，从栈顶开始指定类方法的栈帧。若栈顶不匹配类及方法，则不做任何操作。
     * 
     * @param throwable Throwable
     * @param excludeClass {@code Class<?>}
     * @param excludeClassMethod Pattern
     */
    public static Throwable excludeStackFramesAtTop(Throwable throwable, Class<?> excludeClass, Pattern excludeClassMethod) {
    	if (isNull(excludeClass)) {
			return throwable;
		}
    	final String name = excludeClass.getName();
    	return excludeStackFramesAtTop(throwable, frame -> {return isExcludeFrame(frame, name, excludeClassMethod);});
    }
    
    /**
     * 剔除异常堆栈中，从栈顶开始指定类方法的栈帧。若栈顶不匹配类及方法，则不做任何操作。
     * 
     * @param throwable Throwable
     * @param excludeClass {@code Class<?>}
     * @param excludeClassMethod Pattern
     */
    public static Throwable excludeStackFramesAtTop(Throwable throwable, Predicate<StackTraceElement> excludePredicate) {
    	if (isNull(throwable)) {
			return throwable;
		}
    	int index = 0;
    	StackTraceElement[] stack = throwable.getStackTrace();
    	while (index < stack.length && excludePredicate.test(stack[index])) {
			index++;
		}
    	if (index < stack.length) {
    		StackTraceElement[] newStack =new StackTraceElement[stack.length - index];
    		System.arraycopy(stack, index, newStack, 0, newStack.length);
    		throwable.setStackTrace(newStack);
		}
    	return throwable;
    }
    
    /**
     * 重新填充异常栈帧，剔除指定类的栈帧。
     * 
     * @param throwable Throwable
     * @param excludeClass Class<?>
     */
    public static Throwable excludeStackFrames(Throwable throwable, Class<?> excludeClass) {
    	return excludeStackFrames(throwable, excludeClass, EMP);
    }
    
    /**
     * 重新填充异常堆栈，剔除指定类的栈帧。支持剔除类方法，需指定方法名称正则。
     * 
     * @param throwable Throwable
     * @param excludeClass Class
     * @param excludeMthodPattern String
     */
    public static Throwable excludeStackFrames(Throwable throwable, Class<?> excludeClass, String excludeClassMethod) {
    	if (isNull(excludeClass)) {
    		return throwable;
    	}
    	final String name = excludeClass.getName();
    	return excludeStackFrames(throwable, frame -> {return isExcludeFrame(frame, name, excludeClassMethod);});
    }
    
    /**
     * 重新填充异常堆栈，剔除指定类的栈帧。支持剔除类方法，需指定方法名称正则。
     * 
     * @param throwable Throwable
     * @param excludeClass Class
     * @param excludeMthodPattern Pattern
     */
    public static Throwable excludeStackFrames(Throwable throwable, Class<?> excludeClass, Pattern excludeClassMethod) {
    	if (isNull(excludeClass)) {
    		return throwable;
    	}
    	final String name = excludeClass.getName();
    	return excludeStackFrames(throwable, frame -> {return isExcludeFrame(frame, name, excludeClassMethod);});
    }
    
    
    public static Throwable excludeStackFrames(Throwable throwable, Predicate<StackTraceElement> excludePredicate) {
    	if (isNull(throwable)) {
			return throwable;
		}
    	StackTraceElement[] stack = throwable.getStackTrace();
    	List<StackTraceElement> newStack = newArrayList(stack.length);
    	for (StackTraceElement frame : stack) {
			if (!excludePredicate.test(frame)) {
				newStack.add(frame);
			}
		}
    	if (newStack.size() < stack.length) {
    		throwable.setStackTrace(newStack.toArray(new StackTraceElement[0]));
		}
    	return throwable;
    }
    
    /**
     * 判断此栈帧是否需要跳过。
     * 
     * @param frame StackTraceElement
     * @param excludeClass String
     * @param excludeMthodPattern String
     * @return boolean
     */
    private static boolean isExcludeFrame(StackTraceElement frame, String excludeClass, String excludeClassMethod) {
    	boolean exclude = excludeClass.equals(frame.getClassName());
    	if (exclude && isNotBlank(excludeClassMethod)) {
			return excludeClassMethod.equals(frame.getMethodName());
		}
    	return exclude;
    }
    
    /**
     * 判断此栈帧是否需要跳过。
     * 
     * @param frame StackTraceElement
     * @param excludeClass String
     * @param excludeMthodPattern Pattern
     * @return boolean
     */
    private static boolean isExcludeFrame(StackTraceElement frame, String excludeClass, Pattern excludeMthodPattern) {
    	boolean exclude = excludeClass.equals(frame.getClassName());
    	if (exclude && isNotNull(excludeMthodPattern)) {
			return excludeMthodPattern.matcher(frame.getMethodName()).matches();
		}
    	return exclude;
    }
    
    
    /** 私有构造 */
    private Throws() {}
    
}
