package com.kxindot.goblin.method.reference;

import java.util.function.BiConsumer;

import com.kxindot.goblin.Reflections.MethodLambda;
import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.FourArgConsumer;

/**
 * @author ZhaoQingJiang
 */
public interface FourArgConsumerReference<T, P1, P2, P3, P4> extends MethodReference<T, Void> {

	FourArgConsumerReference<T, P1, P2, P3, P4> first(P1 first);
    
    FourArgConsumerReference<T, P1, P2, P3, P4> second(P2 second);
    
    FourArgConsumerReference<T, P1, P2, P3, P4> third(P3 third);
    
    FourArgConsumerReference<T, P1, P2, P3, P4> fourth(P4 fourth);
    
    
    /**
     * @return {@code FourArgConsumerReference<T, P1, P2, P3, P4>}
     */
    @Override
    FourArgConsumerReference<T, P1, P2, P3, P4> throwable(BiConsumer<MethodLambda, Throwable> handler);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class FourArgConsumerReferenceImpl<T, P1, P2, P3, P4> 
    extends AbstractMethodReference<T, FourArgConsumer<T, P1, P2, P3, P4>, Void>
    implements FourArgConsumerReference<T, P1, P2, P3, P4> {

        public FourArgConsumerReferenceImpl(FourArgConsumer<T, P1, P2, P3, P4> method) {
            super(method);
        }

        @Override
        public FourArgConsumerReference<T, P1, P2, P3, P4> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public FourArgConsumerReference<T, P1, P2, P3, P4> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public FourArgConsumerReference<T, P1, P2, P3, P4> third(P3 third) {
            add(third, 2);
            return this;
        }
        
        @Override
        public FourArgConsumerReference<T, P1, P2, P3, P4> fourth(P4 fourth) {
        	add(fourth, 3);
        	return this;
        }

        @Override
        protected int len() {
            return 4;
        }

        @Override
        protected int max() {
            return 4;
        }
        
        @Override
        public FourArgConsumerReference<T, P1, P2, P3, P4> throwable(BiConsumer<MethodLambda, Throwable> handler) {
        	super.throwable(handler);
        	return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Void invoke(T obj, FourArgConsumer<T, P1, P2, P3, P4> method, Object[] args) throws Throwable {
            method.accept(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]), ((P4) args[3]));
            return null;
        }
        
    }
}
