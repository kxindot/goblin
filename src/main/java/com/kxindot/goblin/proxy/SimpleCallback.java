package com.kxindot.goblin.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.NoOp;

/**
 * @author ZhaoQingJiang
 */
@SuppressWarnings("rawtypes")
public abstract class SimpleCallback<T> extends CallbackHelper implements Callback {

    public SimpleCallback(Class<T> superclass, Class[] interfaces) {
        super(superclass, interfaces);
    }

    @Override
    protected Object getCallback(Method method) {
        return accept(getSuperClass(), method) ? this : NoOp.INSTANCE;
    }

}
