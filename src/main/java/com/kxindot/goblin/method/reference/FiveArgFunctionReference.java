package com.kxindot.goblin.method.reference;

import java.util.function.BiConsumer;

import com.kxindot.goblin.Reflections.MethodLambda;
import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.FiveArgFunction;

/**
 * @author ZhaoQingJiang
 */
public interface FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> extends MethodReference<T, R> {
    
    FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> first(P1 first);
    
    FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> second(P2 second);
    
    FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> third(P3 third);
    
    FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> fourth(P4 fourth);

    FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> fifth(P5 fifth);
    
    
    /**
     * @return {@code FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R>}
     */
    @Override
    FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> throwable(BiConsumer<MethodLambda, Throwable> handler);
    
    
    /**
     * @author ZhaoQingJiang
     */
    class FiveArgMethodReferenceImpl<T, P1, P2, P3, P4, P5, R>
    extends AbstractMethodReference<T, FiveArgFunction<T, P1, P2, P3, P4, P5, R>, R>
    implements FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> {

        public FiveArgMethodReferenceImpl(FiveArgFunction<T, P1, P2, P3, P4, P5, R> method) {
            super(method);
        }

        @Override
        public FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> first(P1 first) {
            add(first, 0);
            return this;
        }

        @Override
        public FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> second(P2 second) {
            add(second, 1);
            return this;
        }

        @Override
        public FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> third(P3 third) {
            add(third, 2);
            return this;
        }
        
        @Override
        public FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> fourth(P4 fourth) {
        	add(fourth, 3);
        	return this;
        }
        
        @Override
        public FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> fifth(P5 fifth) {
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
        public FiveArgFunctionReference<T, P1, P2, P3, P4, P5, R> throwable(BiConsumer<MethodLambda, Throwable> handler) {
        	super.throwable(handler);
        	return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected R invoke(T obj, FiveArgFunction<T, P1, P2, P3, P4, P5, R> method, Object[] args) throws Throwable {
            return method.apply(obj, ((P1) args[0]), ((P2) args[1]), ((P3) args[2]), ((P4) args[3]), ((P5) args[4]));
        }

    }
    
}