package com.kxindot.goblin.method.reference;

import java.util.function.BiConsumer;

import com.kxindot.goblin.Reflections.MethodLambda;
import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.FourArgFunction;

/**
 * @author ZhaoQingJiang
 */
public interface FourArgFunctionReference<T, P1, P2, P3, P4, R> extends MethodReference<T, R> {
    
    FourArgFunctionReference<T, P1, P2, P3, P4, R> first(P1 first);
    
    FourArgFunctionReference<T, P1, P2, P3, P4, R> second(P2 second);
    
    FourArgFunctionReference<T, P1, P2, P3, P4, R> third(P3 third);
    
    FourArgFunctionReference<T, P1, P2, P3, P4, R> fourth(P4 fourth);
    
    
    /**
     * @return {@code FourArgFunctionReference<T, P1, P2, P3, P4, R>}
     */
    @Override
    FourArgFunctionReference<T, P1, P2, P3, P4, R> throwable(BiConsumer<MethodLambda, Throwable> handler);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class FourArgMethodReferenceImpl<T, P1, P2, P3, P4, R>
    extends AbstractMethodReference<T, FourArgFunction<T, P1, P2, P3, P4, R>, R>
    implements FourArgFunctionReference<T, P1, P2, P3, P4, R> {

        public FourArgMethodReferenceImpl(FourArgFunction<T, P1, P2, P3, P4, R> method) {
            super(method);
        }

        @Override
        public FourArgFunctionReference<T, P1, P2, P3, P4, R> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public FourArgFunctionReference<T, P1, P2, P3, P4, R> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public FourArgFunctionReference<T, P1, P2, P3, P4, R> third(P3 third) {
            add(third, 2);
            return this;
        }
        
        @Override
        public FourArgFunctionReference<T, P1, P2, P3, P4, R> fourth(P4 fourth) {
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
        public FourArgFunctionReference<T, P1, P2, P3, P4, R> throwable(BiConsumer<MethodLambda, Throwable> handler) {
        	super.throwable(handler);
        	return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected R invoke(T obj, FourArgFunction<T, P1, P2, P3, P4, R> method, Object[] args) throws Throwable {
            return method.apply(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]), ((P4) args[3]));
        }

    }
    
}