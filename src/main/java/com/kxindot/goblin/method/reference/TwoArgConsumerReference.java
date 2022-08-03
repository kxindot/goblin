package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.TwoArgConsumer;

/**
 * 
 * @author ZhaoQingJiang
 * @param <T>
 * @param <P1>
 * @param <P2>
 */
public interface TwoArgConsumerReference<T, P1, P2> extends MethodReference<T, Void> {

    TwoArgConsumerReference<T, P1, P2> first(P1 first);
    
    TwoArgConsumerReference<T, P1, P2> second(P2 second);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class TwoArgConsumerReferenceImpl<T, P1, P2> 
    extends AbstractMethodReference<T, TwoArgConsumer<T, P1, P2>, Void> 
    implements TwoArgConsumerReference<T, P1, P2> {

        public TwoArgConsumerReferenceImpl(TwoArgConsumer<T, P1, P2> method) {
            super(method);
        }

        @Override
        public TwoArgConsumerReference<T, P1, P2> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public TwoArgConsumerReference<T, P1, P2> second(P2 second) {
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
        protected Void invoke(T obj, TwoArgConsumer<T, P1, P2> method, Object[] args) throws Throwable {
            method.accept(obj, ((P1) args[0]), ((P2) args[1]));
            return null;
        }
        
    }
    
}
