package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.NoArgFunction;

/**
 * @author ZhaoQingJiang
 */
public interface NoArgFunctionReference<T, R> extends MethodReference<T, R> {

    
    /**
     * @author ZhaoQingJiang
     */
    class NoArgFunctionReferenceImpl<T, R> 
    extends AbstractMethodReference<T, NoArgFunction<T, R>, R> {

        public NoArgFunctionReferenceImpl(NoArgFunction<T, R> method) {
            super(method);
        }

        @Override
        protected int len() {
            return 0;
        }

        @Override
        protected int max() {
            return 0;
        }

        @Override
        protected R invoke(T obj, NoArgFunction<T, R> method, Object[] args) throws Throwable {
            return method.apply(obj);
        }

    }
    
}
