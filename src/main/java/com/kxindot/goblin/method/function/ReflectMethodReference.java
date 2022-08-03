package com.kxindot.goblin.method.function;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.kxindot.goblin.method.MethodReference;

import net.sf.cglib.proxy.MethodProxy;

public interface ReflectMethodReference<T, R> extends MethodReference<T, R> {
    
    ReflectMethodReference<T, R> param(Object param);
    
    ReflectMethodReference<T, R> param(Object param, int index);
    
    ReflectMethodReference<T, R> params(Object[] params);
    
    
    /**
     * Java反射方法{@link java.lang.reflect.Method}引用包装
     * @author ZhaoQingJiang
     */
    class JavaReflectMethodReference extends AbstractReflectMethodReference<Object, Method, Object> {

        public JavaReflectMethodReference(Method m) {
            super(m);
        }

        @Override
        protected Object invoke(Object obj, Method m, Object[] args) throws Exception {
            return m.invoke(obj, args);
        }
    }
    
    /**
     * Cglib代理方法{@link net.sf.cglib.proxy.MethodProxy}引用包装
     * @author ZhaoQingJiang
     */
    class CglibProxyMethodReference 
    extends AbstractReflectMethodReference<Object, MethodProxy, Object>  {

        public CglibProxyMethodReference(MethodProxy m) {
            super(m);
        }

        @Override
        protected Object invoke(Object obj, MethodProxy m, Object[] args) throws Throwable {
            return m.invokeSuper(obj, args);
        }
    }
    
    /**
     * @author ZhaoQingJiang
     */
    abstract class AbstractReflectMethodReference<T, M, R> 
    extends AbstractMethodReference<T, M, R>
    implements ReflectMethodReference<T, R> {

        public AbstractReflectMethodReference(M method) {
            super(method);
        }

        @Override
        public ReflectMethodReference<T, R> param(Object param) {
            add(param);
            return this;
        }

        @Override
        public ReflectMethodReference<T, R> param(Object param, int index) {
            add(param, index);
            return this;
        }

        @Override
        public ReflectMethodReference<T, R> params(Object[] params) {
            reset();
            Arrays.stream(params).forEach(p -> add(p));
            return this;
        }

        @Override
        protected int len() {
            return 2 << 1;
        }

        @Override
        protected int max() {
            return 255;
        }

    }
}