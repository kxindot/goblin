package com.kxindot.goblin.method.reference;

import java.util.function.BiConsumer;

import com.kxindot.goblin.Reflections.MethodLambda;
import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.FiveArgConsumer;

/**
 * @author ZhaoQingJiang
 */
public interface FiveArgConsumerReference<T, P1, P2, P3, P4, P5> extends MethodReference<T, Void> {

	FiveArgConsumerReference<T, P1, P2, P3, P4, P5> first(P1 first);
    
    FiveArgConsumerReference<T, P1, P2, P3, P4, P5> second(P2 second);
    
    FiveArgConsumerReference<T, P1, P2, P3, P4, P5> third(P3 third);
    
    FiveArgConsumerReference<T, P1, P2, P3, P4, P5> fourth(P4 fourth);
    
    FiveArgConsumerReference<T, P1, P2, P3, P4, P5> fifth(P5 fifth);
    
    
    /**
     * @return {@code FiveArgConsumerReference<T, P1, P2, P3, P4, P5>}
     */
    @Override
    FiveArgConsumerReference<T, P1, P2, P3, P4, P5> throwable(BiConsumer<MethodLambda, Throwable> handler);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class FiveArgConsumerReferenceImpl<T, P1, P2, P3, P4, P5> 
    extends AbstractMethodReference<T, FiveArgConsumer<T, P1, P2, P3, P4, P5>, Void>
    implements FiveArgConsumerReference<T, P1, P2, P3, P4, P5> {

        public FiveArgConsumerReferenceImpl(FiveArgConsumer<T, P1, P2, P3, P4, P5> method) {
            super(method);
        }

        @Override
        public FiveArgConsumerReference<T, P1, P2, P3, P4, P5> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public FiveArgConsumerReference<T, P1, P2, P3, P4, P5> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public FiveArgConsumerReference<T, P1, P2, P3, P4, P5> third(P3 third) {
            add(third, 2);
            return this;
        }
        
        @Override
        public FiveArgConsumerReference<T, P1, P2, P3, P4, P5> fourth(P4 fourth) {
        	add(fourth, 3);
        	return this;
        }
        
        @Override
        public FiveArgConsumerReference<T, P1, P2, P3, P4, P5> fifth(P5 fifth) {
        	add(fifth, 4);
        	return this;
        }

        @Override
        protected int len() {
            return 5;
        }

        @Override
        protected int max() {
            return 5;
        }
        
        @Override
        public FiveArgConsumerReference<T, P1, P2, P3, P4, P5> throwable(BiConsumer<MethodLambda, Throwable> handler) {
        	super.throwable(handler);
        	return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Void invoke(T obj, FiveArgConsumer<T, P1, P2, P3, P4, P5> method, Object[] args) throws Throwable {
            method.accept(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]), ((P4) args[3]), ((P5) args[4]));
            return null;
        }
        
    }
}
