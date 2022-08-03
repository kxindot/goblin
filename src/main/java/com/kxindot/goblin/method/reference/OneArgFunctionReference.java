package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.OneArgFunction;

/**
 * 无参方法引用
 * @author ZhaoQingJiang
 * @param <T>
 * @param <P>
 * @param <R>
 */
public interface OneArgFunctionReference<T, P, R> extends MethodReference<T, R> {
    
    MethodReference<T, R> param(P param);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class OneArgFunctionReferenceImpl<T, P, R> 
    extends AbstractMethodReference<T, OneArgFunction<T, P, R>, R> 
    implements OneArgFunctionReference<T, P, R>{

        public OneArgFunctionReferenceImpl(OneArgFunction<T, P, R> method) {
            super(method);
        }

        @Override
        public MethodReference<T, R> param(P param) {
            add(param, 0);
            return this;
        }

        @Override
        protected int len() {
            return 1;
        }

        @Override
        protected int max() {
            return 1;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected R invoke(T obj, OneArgFunction<T, P, R> method, Object[] args) throws Throwable {
            return method.apply(obj, (P) args[0]);
        }
        
    }
}