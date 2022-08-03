package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.TwoArgFunction;

/**
 * 
 * @author ZhaoQingJiang
 * @param <T>
 * @param <P1>
 * @param <P2>
 * @param <R>
 */
public interface TwoArgFunctionReference<T, P1, P2, R> extends MethodReference<T, R> {
    
    TwoArgFunctionReference<T, P1, P2, R> first(P1 first);
    
    TwoArgFunctionReference<T, P1, P2, R> second(P2 second);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class TwoArgFunctionReferenceImpl<T, P1, P2, R>
    extends AbstractMethodReference<T, TwoArgFunction<T, P1, P2, R>, R> 
    implements TwoArgFunctionReference<T, P1, P2, R> {

        public TwoArgFunctionReferenceImpl(TwoArgFunction<T, P1, P2, R> method) {
            super(method);
        }

        @Override
        public TwoArgFunctionReference<T, P1, P2, R> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public TwoArgFunctionReference<T, P1, P2, R> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        protected int len() {
            return 2;
        }

        @Override
        protected int max() {
            return 2;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected R invoke(T obj, TwoArgFunction<T, P1, P2, R> method, Object[] args) throws Throwable {
            return method.apply(obj, ((P1) args[0]), ((P2) args[1]));
        }

    }
}