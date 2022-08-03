package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.OneArgConsumer;

/**
 * @author ZhaoQingJiang
 * @param <T>
 * @param <P>
 */
public interface OneArgConsumerReference<T, P> extends MethodReference<T, Void> {

    
    MethodReference<T, Void> param(P param);
    
    
    
    /**
     * @author ZhaoQingJiang
     */
    class OneArgConsumerReferenceImpl<T, P> 
    extends AbstractMethodReference<T, OneArgConsumer<T, P>, Void>
    implements OneArgConsumerReference<T, P> {

        public OneArgConsumerReferenceImpl(OneArgConsumer<T, P> method) {
            super(method);
        }

        @Override
        public MethodReference<T, Void> param(P param) {
            add(param, 0);
            return this;
        }

        @Override
        protected int len() {
            return 1;
        }

        @Override
        protected int max() {
            return 1;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Void invoke(T obj, OneArgConsumer<T, P> method, Object[] args) throws Throwable {
            method.accept(obj, ((P) args[0]));
            return null;
        }
        
    }
    
}
