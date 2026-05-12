package com.kxindot.goblin.method.reference;

import java.util.function.BiConsumer;

import com.kxindot.goblin.Reflections.MethodLambda;
import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.SixArgConsumer;

/**
 * @author ZhaoQingJiang
 */
public interface SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> extends MethodReference<T, Void> {

	SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> first(P1 first);
    
    SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> second(P2 second);
    
    SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> third(P3 third);
    
    SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> fourth(P4 fourth);
    
    SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> fifth(P5 fifth);
    
    SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> sixth(P6 sixth);
    
    
    /**
     * @return {@code SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6>}
     */
    @Override
    SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> throwable(BiConsumer<MethodLambda, Throwable> handler);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class SixArgConsumerReferenceImpl<T, P1, P2, P3, P4, P5, P6> 
    extends AbstractMethodReference<T, SixArgConsumer<T, P1, P2, P3, P4, P5, P6>, Void>
    implements SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> {

        public SixArgConsumerReferenceImpl(SixArgConsumer<T, P1, P2, P3, P4, P5, P6> method) {
            super(method);
        }

        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> third(P3 third) {
            add(third, 2);
            return this;
        }
        
        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> fourth(P4 fourth) {
        	add(fourth, 3);
        	return this;
        }
        
        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> fifth(P5 fifth) {
        	add(fifth, 4);
        	return this;
        }
        
        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> sixth(P6 sixth) {
        	add(sixth, 5);
        	return this;
        }

        @Override
        protected int len() {
            return 6;
        }

        @Override
        protected int max() {
            return 6;
        }
        
        @Override
        public SixArgConsumerReference<T, P1, P2, P3, P4, P5, P6> throwable(BiConsumer<MethodLambda, Throwable> handler) {
        	super.throwable(handler);
        	return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Void invoke(T obj, SixArgConsumer<T, P1, P2, P3, P4, P5, P6> method, Object[] args) throws Throwable {
            method.accept(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]), ((P4) args[3]), ((P5) args[4]), ((P6) args[5]));
            return null;
        }
        
    }
}
