package com.kxindot.goblin.method.reference;

import com.kxindot.goblin.method.MethodReference;
import com.kxindot.goblin.method.function.NoArgConsumer;

/**
 * @author ZhaoQingJiang
 */
public interface NoArgConsumerReference<T> extends MethodReference<T, Void> {

    
    /**
     * @author ZhaoQingJiang
     */
    class NoArgConsumerReferenceImpl<T> 
    extends AbstractMethodReference<T, NoArgConsumer<T>, Void>
    implements NoArgConsumerReference<T> {

        public NoArgConsumerReferenceImpl(NoArgConsumer<T> method) {
            super(method);
        }

        @Override
        protected int len() {
            return 0;
        }

        @Override
        protected int max() {
            return 0;
        }

        @Override
        protected Void invoke(T obj, NoArgConsumer<T> method, Object[] args) throws Throwable {
            method.accept(obj);
            return null;
        }
        
    }
    
}
