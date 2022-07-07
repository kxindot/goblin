package com.kxindot.goblin.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author ZhaoQingJiang
 */
public interface Callback extends MethodInterceptor {

    boolean accept(Class<?> type, Method method);
    
}
