package com.kxindot.goblin.method;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kxindot.goblin.Objects;
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
 * @author ZhaoQingJiang
 */
public interface MethodReference<T, R> {
    
    public static <T> MethodReference<T, Void> noArgs(NoArgConsumer<T> consumer) {
        return new NoArgConsumerReferenceImpl<>(consumer);
    }
    
    public static <T, R> MethodReference<T, R> noArgs(NoArgFunction<T, R> function) {
        return new NoArgFunctionReferenceImpl<>(function);
    }
    
    public static <T, P> OneArgConsumerReference<T, P> oneArgs(OneArgConsumer<T, P> consumer) {
        return new OneArgConsumerReferenceImpl<>(consumer);
    }
    
    public static <T, P, R> OneArgFunctionReference<T, P, R> oneArgs(OneArgFunction<T, P, R> function) {
        return new OneArgFunctionReferenceImpl<>(function);
    }
    
    public static <T, P1, P2> TwoArgConsumerReference<T, P1, P2> twoArgs(TwoArgConsumer<T, P1, P2> consumer) {
        return new TwoArgConsumerReferenceImpl<>(consumer);
    }
    
    public static <T, P1, P2, R> TwoArgFunctionReference<T, P1, P2, R> twoArgs(TwoArgFunction<T, P1, P2, R> function) {
        return new TwoArgFunctionReferenceImpl<>(function);
    }
    
    public static <T, P1, P2, P3> ThreeArgConsumerReference<T, P1, P2, P3> threeArgs(ThreeArgConsumer<T, P1, P2, P3> consumer) {
        return new ThreeArgConsumerReferenceImpl<>(consumer);
    }
    
    public static <T, P1, P2, P3, R> ThreeArgFunctionReference<T, P1, P2, P3, R> threeArgs(ThreeArgFunction<T, P1, P2, P3, R> function) {
        return new ThreeArgMethodReferenceImpl<>(function);
    }
    
    public static ReflectMethodReference<Object, Object> method(Method method) {
        return new JavaReflectMethodReference(method);
    }
    
    public static ReflectMethodReference<Object, Object> method(MethodProxy method) {
        return new CglibProxyMethodReference(method);
    }
    
    R invoke(T obj);
    
    Future<R> invokeAsync(T obj);
    
    
    /**
     * 抽象方法引用包装类
     * @author ZhaoQingJiang
     */
    abstract class AbstractMethodReference<T, M, R> implements MethodReference<T, R> {
        
        static final Logger log = LoggerFactory.getLogger("MethodReferenceRunnerLogger");
        private T obj;
        private final M method;
        private Object[] params;
        private int index = 0;
        private final int max;
        
        
        public AbstractMethodReference(M method) {
            this.method = Objects.requireNotNull(method);
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
              throw new MethodInvocationException(e);
          }
        }
        
        @Override
        public Future<R> invokeAsync(T obj) {
            this.obj = Objects.requireNotNull(obj);
            return Threads.submit(this.new Runner());
        }
        
        /**
         * @author ZhaoQingJiang
         */
        class Runner implements Callable<R> {
            @Override
            public R call() throws Exception {
                try {
                    return invoke(obj);
                } catch (Exception e) {
                    log.error("Async MethodReference Invoke Exception", e);
                    throw e;
                }
            }
        }
        
    }
    
    
    /**
     * @author ZhaoQingJiang
     */
    public class MethodInvocationException extends RuntimeException {

        private static final long serialVersionUID = -2675028474403209435L;

        public MethodInvocationException(String message, Throwable cause) {
            super(message, cause);
        }

        public MethodInvocationException(Throwable cause) {
            super(cause);
        }
    }
    
}