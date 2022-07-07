package com.kxindot.goblin.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author ZhaoQingJiang
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class CallbackHelper implements CallbackFilter {

    private Class superClass;
    private Map methodMap = new HashMap();
    private List callbacks = new ArrayList();

    public CallbackHelper(Class superclass, Class[] interfaces) {
        this.superClass = superclass;
        List methods = new ArrayList();
        Enhancer.getMethods(superclass, interfaces, methods);
        Map indexes = new HashMap();
        for (int i = 0, size = methods.size(); i < size; i++) {
            Method method = (Method) methods.get(i);
            Object callback = getCallback(method);
            if (callback == null)
                throw new IllegalStateException("getCallback cannot return null");
            boolean isCallback = callback instanceof Callback;
            if (!(isCallback || (callback instanceof Class)))
                throw new IllegalStateException("getCallback must return a Callback or a Class");
            if (i > 0 && ((callbacks.get(i - 1) instanceof Callback) ^ isCallback))
                throw new IllegalStateException(
                        "getCallback must return a Callback or a Class consistently for every Method");
            Integer index = (Integer) indexes.get(callback);
            if (index == null) {
                index = new Integer(callbacks.size());
                indexes.put(callback, index);
            }
            methodMap.put(method, index);
            callbacks.add(callback);
        }
    }

    abstract protected Object getCallback(Method method);

    public Callback[] getCallbacks() {
        if (callbacks.size() == 0)
            return new Callback[0];
        if (callbacks.get(0) instanceof Callback) {
            return (Callback[]) callbacks.toArray(new Callback[callbacks.size()]);
        } else {
            throw new IllegalStateException(
                    "getCallback returned classes, not callbacks; call getCallbackTypes instead");
        }
    }

    public Class[] getCallbackTypes() {
        if (callbacks.size() == 0)
            return new Class[0];
        if (callbacks.get(0) instanceof Callback) {
            return ReflectUtils.getClasses(getCallbacks());
        } else {
            return (Class[]) callbacks.toArray(new Class[callbacks.size()]);
        }
    }
    
    public Class getSuperClass() {
        return superClass;
    }

    public int accept(Method method) {
        try {
            return ((Integer) methodMap.get(method)).intValue();
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public int hashCode() {
        return methodMap.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof CallbackHelper))
            return false;
        return methodMap.equals(((CallbackHelper) o).methodMap);
    }
}
