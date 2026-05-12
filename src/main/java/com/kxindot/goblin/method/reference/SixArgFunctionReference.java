package com.kxindot.goblin.method.reference;

import java.util.function.BiConsumer;

import com.kxindot.goblin.Reflections.MethodLambda;
import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.SixArgFunction;

/**
 * @author ZhaoQingJiang
 */
public interface SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> extends MethodReference<T, R> {
    
    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> first(P1 first);
    
    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> second(P2 second);
    
    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> third(P3 third);
    
    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> fourth(P4 fourth);

    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> fifth(P5 fifth);

    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> sixth(P6 sixth);
    
    
    /**
     * @return {@code SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R>}
     */
    @Override
    SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> throwable(BiConsumer<MethodLambda, Throwable> handler);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class SixArgMethodReferenceImpl<T, P1, P2, P3, P4, P5, P6, R>
    extends AbstractMethodReference<T, SixArgFunction<T, P1, P2, P3, P4, P5, P6, R>, R>
    implements SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> {

        public SixArgMethodReferenceImpl(SixArgFunction<T, P1, P2, P3, P4, P5, P6, R> method) {
            super(method);
        }

        @Override
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> third(P3 third) {
            add(third, 2);
            return this;
        }
        
        @Override
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> fourth(P4 fourth) {
        	add(fourth, 3);
        	return this;
        }
        
        @Override
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> fifth(P5 fifth) {
        	add(fifth, 4);
        	return this;
        }
        
        @Override
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> sixth(P6 sixth) {
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
        public SixArgFunctionReference<T, P1, P2, P3, P4, P5, P6, R> throwable(BiConsumer<MethodLambda, Throwable> handler) {
        	super.throwable(handler);
        	return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected R invoke(T obj, SixArgFunction<T, P1, P2, P3, P4, P5, P6, R> method, Object[] args) throws Throwable {
            return method.apply(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]), ((P4) args[3]), ((P5) args[4]), ((P6) args[5]));
        }

    }
    
}