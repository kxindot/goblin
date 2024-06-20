package com.kxindot.goblin.method;

import static com.kxindot.goblin.Objects.requireNotNull;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.kxindot.goblin.Objects;
import com.kxindot.goblin.concurrent.ThreadCompletableExecutor;
import com.kxindot.goblin.method.function.NoArgConsumer;
import com.kxindot.goblin.method.function.NoArgFunction;
import com.kxindot.goblin.method.function.OneArgConsumer;
import com.kxindot.goblin.method.function.OneArgFunction;
import com.kxindot.goblin.method.function.ReflectMethodReference;
import com.kxindot.goblin.method.function.ReflectMethodReference.CglibProxyMethodReference;
import com.kxindot.goblin.method.function.ReflectMethodReference.JavaReflectMethodReference;
import com.kxindot.goblin.method.function.ThreeArgConsumer;
import com.kxindot.goblin.method.function.ThreeArgFunction;
import com.kxindot.goblin.method.function.TwoArgConsumer;
import com.kxindot.goblin.method.function.TwoArgFunction;
import com.kxindot.goblin.method.reference.NoArgConsumerReference.NoArgConsumerReferenceImpl;
import com.kxindot.goblin.method.reference.NoArgFunctionReference.NoArgFunctionReferenceImpl;
import com.kxindot.goblin.method.reference.OneArgConsumerReference;
import com.kxindot.goblin.method.reference.OneArgConsumerReference.OneArgConsumerReferenceImpl;
import com.kxindot.goblin.method.reference.OneArgFunctionReference;
import com.kxindot.goblin.method.reference.OneArgFunctionReference.OneArgFunctionReferenceImpl;
import com.kxindot.goblin.method.reference.ThreeArgConsumerReference;
import com.kxindot.goblin.method.reference.ThreeArgConsumerReference.ThreeArgConsumerReferenceImpl;
import com.kxindot.goblin.method.reference.ThreeArgFunctionReference;
import com.kxindot.goblin.method.reference.ThreeArgFunctionReference.ThreeArgMethodReferenceImpl;
import com.kxindot.goblin.method.reference.TwoArgConsumerReference;
import com.kxindot.goblin.method.reference.TwoArgConsumerReference.TwoArgConsumerReferenceImpl;
import com.kxindot.goblin.method.reference.TwoArgFunctionReference;
import com.kxindot.goblin.method.reference.TwoArgFunctionReference.TwoArgFunctionReferenceImpl;
import com.kxindot.goblin.thread.Threads;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 方法引用定义.
 * 
 * @author ZhaoQingJiang
 */
public interface MethodReference<T, R> {
    
	/**
	 * 创建无参数无返回值的方法引用.
	 * 
	 * @param <T> 方法所属类类型
	 * @param consumer {@link NoArgConsumer}
	 * @return {@link MethodReference}
	 */
    public static <T> MethodReference<T, Void> noArgs(NoArgConsumer<T> consumer) {
        return new NoArgConsumerReferenceImpl<>(consumer);
    }
    
    /**
     * 创建无参数有返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <R> 方法返回值类型
     * @param function {@link NoArgFunction}
     * @return {@link MethodReference}
     */
    public static <T, R> MethodReference<T, R> noArgs(NoArgFunction<T, R> function) {
        return new NoArgFunctionReferenceImpl<>(function);
    }
    
    /**
     * 创建单参数无返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <P> 方法参数类型
     * @param consumer {@link OneArgConsumer}
     * @return {@link MethodReference}
     */
    public static <T, P> OneArgConsumerReference<T, P> oneArgs(OneArgConsumer<T, P> consumer) {
        return new OneArgConsumerReferenceImpl<>(consumer);
    }
    
    /**
     * 创建单参数有返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <P> 方法参数类型
     * @param <R> 方法返回值类型
     * @param function {@link OneArgFunction}
     * @return {@link OneArgFunctionReference}
     */
    public static <T, P, R> OneArgFunctionReference<T, P, R> oneArgs(OneArgFunction<T, P, R> function) {
        return new OneArgFunctionReferenceImpl<>(function);
    }
    
    /**
     * 创建双参数无返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <P1> 方法第一参数类型
     * @param <P2> 方法第二参数类型
     * @param consumer {@link TwoArgConsumer}
     * @return {@link TwoArgConsumerReference}
     */
    public static <T, P1, P2> TwoArgConsumerReference<T, P1, P2> twoArgs(TwoArgConsumer<T, P1, P2> consumer) {
        return new TwoArgConsumerReferenceImpl<>(consumer);
    }
    
    /**
     * 创建双参数有返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <P1> 方法第一参数类型
     * @param <P2> 方法第二参数类型
     * @param <R> 方法返回值类型
     * @param function {@link TwoArgFunction}
     * @return {@link TwoArgFunctionReference}
     */
    public static <T, P1, P2, R> TwoArgFunctionReference<T, P1, P2, R> twoArgs(TwoArgFunction<T, P1, P2, R> function) {
        return new TwoArgFunctionReferenceImpl<>(function);
    }
    
    /**
     * 创建三参数无返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <P1> 方法第一参数类型
     * @param <P2> 方法第二参数类型
     * @param <P3> 方法第三参数类型
     * @param consumer {@link ThreeArgConsumer}
     * @return {@link ThreeArgConsumerReference}
     */
    public static <T, P1, P2, P3> ThreeArgConsumerReference<T, P1, P2, P3> threeArgs(ThreeArgConsumer<T, P1, P2, P3> consumer) {
        return new ThreeArgConsumerReferenceImpl<>(consumer);
    }
    
    /**
     * 创建三参数有返回值的方法引用.
     * 
     * @param <T> 方法所属类类型
     * @param <P1> 方法第一参数类型
     * @param <P2> 方法第二参数类型
     * @param <P3> 方法第三参数类型
     * @param <R> 方法返回值类型
     * @param function {@link ThreeArgFunction}
     * @return {@link ThreeArgFunctionReference}
     */
    public static <T, P1, P2, P3, R> ThreeArgFunctionReference<T, P1, P2, P3, R> threeArgs(ThreeArgFunction<T, P1, P2, P3, R> function) {
        return new ThreeArgMethodReferenceImpl<>(function);
    }
    
    /**
     * 创建反射方法对象{@link Method}的方法引用.
     * 
     * @param method Method
     * @return {@link ReflectMethodReference}
     */
    public static ReflectMethodReference<Object, Object> method(Method method) {
        return new JavaReflectMethodReference(method);
    }
    
    /**
     * 创建cglib代理方法对象{@link MethodProxy}的方法引用.
     * 
     * @param method MethodProxy
     * @return {@link ReflectMethodReference}
     */
    public static ReflectMethodReference<Object, Object> method(MethodProxy method) {
        return new CglibProxyMethodReference(method);
    }
    
    /**
     * 同步调用方法.若方法运行出现错误,则会抛出{@link MethodInvocationException}异常.
     * 
     * @param obj 对象实例
     * @return 方法返回值
     * @throws IllegalArgumentException 若obj==null,则抛出此异常.
     * @throws MethodInvocationException 若方法运行出现错误,则抛出此异常.
     */
    R invoke(T obj);
    
    /**
     * 异步调用方法,返回被{@link Future}包装的方法返回值.
     * 
     * @param obj 对象实例
     * @return 被{@link Future}包装的方法返回值
     * @throws IllegalArgumentException 若obj==null,则抛出此异常.
     */
    Future<R> invokeAsync(T obj);
    
    /**
     * 使用指定线程池服务异步调用方法,返回被{@link Future}包装的方法返回值.
     * 
     * @param obj 对象实例
     * @param executor 线程池服务 
     * @return 被{@link Future}包装的方法返回值
     * @throws IllegalArgumentException 若obj==null,则抛出此异常.
     * @throws NullPointerException 若executor==null,则抛出此异常.
     */
    Future<R> invokeAsync(T obj, ExecutorService executor);
    
    /**
     * 将方法调用提交至多任务同步完成线程池.
     * 提交完成后,可调用{@link ThreadCompletableExecutor#complete()}
     * 或{@link ThreadCompletableExecutor#complete(int, java.util.concurrent.TimeUnit)}
     * 方法异步运行已提交的任务.
     * 
     * @param obj 对象实例
     * @param executor 多任务同步完成线程池
     * @throws IllegalArgumentException 若obj==null,则抛出此异常.
     * @throws NullPointerException 若executor==null,则抛出此异常.
     */
    void commit(T obj, ThreadCompletableExecutor executor);
    
    
    /**
     * 抽象方法引用.
     * 
     * @author ZhaoQingJiang
     */
    abstract class AbstractMethodReference<T, M, R> implements MethodReference<T, R> {
        
        private T obj;
        private final M method;
        private Object[] params;
        private int index = 0;
        private final int max;
        
        
        public AbstractMethodReference(M method) {
            this.method = requireNotNull(method);
            this.max = max();
            Objects.requireTrue(max >= 0, "method parameter max length must be positive");
            int len = len();
            Objects.requireTrue(len >= 0 && len <= max, "method parameter length must be positive and less than or equal to max length");
            this.params = new Object[len];
        }
        
        protected abstract int len();
        
        protected abstract int max();
        
        protected abstract R invoke(T obj, M method, Object[] args) throws Throwable;
        
        protected void add(Object param) {
            check(++index);
            resize();
            params[index] = param;
        }
        
        protected void add(Object param, int index) {
            check(index);
            resize();
            params[index] = param;
        }
        
        protected void reset() {
            params = new Object[len()];
        }
        
        private void resize() {
            int len = params.length;
            if (index < len) return;
            do {
                len += (len << 1);
                if (len >= max) len = max;
            } while (index >= len);
            Object[] objs = new Object[len];
            System.arraycopy(params, 0, objs, 0, index + 1);
            params = objs;
        }
        
        private void check(int index) {
            if (index >= max) {
                throw new IndexOutOfBoundsException("method parameter length: " + max + ", index: " + index);
            } 
        }
        
		@Override
		public R invoke(T obj) {
			try {
				return invoke(obj, method, params);
			} catch (Throwable e) {
				throw new MethodInvocationException("", e);
			}
		}
        
        @Override
        public Future<R> invokeAsync(T obj) {
            this.obj = requireNotNull(obj);
            return Threads.submit(this.new Caller());
        }
        
        @Override
        public Future<R> invokeAsync(T obj, ExecutorService executor) {
        	this.obj = requireNotNull(obj);
        	return executor.submit(this.new Caller());
        }
        
        @Override
        public void commit(T obj, ThreadCompletableExecutor executor) {
        	this.obj = requireNotNull(obj);
        	executor.commit(this.new Runner());
        }
        
        /**
         * @author ZhaoQingJiang
         */
        class Caller implements Callable<R> {
            
        	@Override
            public R call() throws Exception {
            	return invoke(obj);
            }
        	
        }
        
        /**
         * @author ZhaoQingJiang
         */
        class Runner implements Runnable {

			@Override
			public void run() {
				invoke(obj);
			}
        	
        }
        
    }
    
    
    /**
     * 调用方法错误异常.
     * 
     * @author ZhaoQingJiang
     */
    public class MethodInvocationException extends RuntimeException {

        private static final long serialVersionUID = -2675028474403209435L;

        public MethodInvocationException(Throwable cause) {
        	super(cause);
        }
        
        public MethodInvocationException(String message, Throwable cause) {
            super(message, cause);
        }

    }
    
}