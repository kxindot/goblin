package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.ThreeArgFunction;

public interface ThreeArgFunctionReference<T, P1, P2, P3, R> extends MethodReference<T, R> {
    
    ThreeArgFunctionReference<T, P1, P2, P3, R> first(P1 first);
    
    ThreeArgFunctionReference<T, P1, P2, P3, R> second(P2 second);
    
    ThreeArgFunctionReference<T, P1, P2, P3, R> third(P3 third);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class ThreeArgMethodReferenceImpl<T, P1, P2, P3, R>
    extends AbstractMethodReference<T, ThreeArgFunction<T, P1, P2, P3, R>, R>
    implements ThreeArgFunctionReference<T, P1, P2, P3, R> {

        public ThreeArgMethodReferenceImpl(ThreeArgFunction<T, P1, P2, P3, R> method) {
            super(method);
        }

        @Override
        public ThreeArgFunctionReference<T, P1, P2, P3, R> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public ThreeArgFunctionReference<T, P1, P2, P3, R> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public ThreeArgFunctionReference<T, P1, P2, P3, R> third(P3 third) {
            add(third, 2);
            return this;
        }

        @Override
        protected int len() {
            return 3;
        }

        @Override
        protected int max() {
            return 3;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected R invoke(T obj, ThreeArgFunction<T, P1, P2, P3, R> method, Object[] args) throws Throwable {
            return method.apply(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]));
        }

    }
    
}