package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.ThreeArgConsumer;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 * @param <P3> Method Third Parameter Type
 */
public interface ThreeArgConsumerReference<T, P1, P2, P3> extends MethodReference<T, Void> {

    ThreeArgConsumerReference<T, P1, P2, P3> first(P1 first);
    
    ThreeArgConsumerReference<T, P1, P2, P3> second(P2 second);
    
    ThreeArgConsumerReference<T, P1, P2, P3> third(P3 third);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class ThreeArgConsumerReferenceImpl<T, P1, P2, P3> 
    extends AbstractMethodReference<T, ThreeArgConsumer<T, P1, P2, P3>, Void>
    implements ThreeArgConsumerReference<T, P1, P2, P3> {

        public ThreeArgConsumerReferenceImpl(ThreeArgConsumer<T, P1, P2, P3> method) {
            super(method);
        }

        @Override
        public ThreeArgConsumerReference<T, P1, P2, P3> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public ThreeArgConsumerReference<T, P1, P2, P3> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public ThreeArgConsumerReference<T, P1, P2, P3> third(P3 third) {
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
        protected Void invoke(T obj, ThreeArgConsumer<T, P1, P2, P3> method, Object[] args) throws Throwable {
            method.accept(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]));
            return null;
        }
        
    }
}
