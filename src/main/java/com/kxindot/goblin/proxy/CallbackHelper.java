package com.kxindot.goblin.proxy;

import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.newArrayList;

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
    protected List<com.kxindot.goblin.proxy.Callback> list;

    public CallbackHelper(Class superclass, Class[] interfaces) {
        this(superclass, interfaces, new com.kxindot.goblin.proxy.Callback[0]);
    }
    
    public CallbackHelper(Class superclass, Class[] interfaces, com.kxindot.goblin.proxy.Callback... callbacks) {
    	this.superClass = superclass;
    	if (isNotEmpty(callbacks)) {
			this.list = newArrayList(callbacks);
		}
    	List methods = new ArrayList();
    	Enhancer.getMethods(superclass, interfaces, methods);
    	Map indexes = new HashMap();
    	for (int i = 0, size = methods.size(); i < size; i++) {
    		Method method = (Method) methods.get(i);
    		Object callback = getCallback(method);
    		if (callback == null) {
				
			}
    		if (callback == null)
    			throw new IllegalStateException("getCallback cannot return null");
    		boolean isCallback = callback instanceof Callback;
    		if (!(isCallback || (callback instanceof Class)))
    			throw new IllegalStateException("getCallback must return a Callback or a Class");
    		if (i > 0 && ((this.callbacks.get(i - 1) instanceof Callback) ^ isCallback))
    			throw new IllegalStateException(
    					"getCallback must return a Callback or a Class consistently for every Method");
    		Integer index = (Integer) indexes.get(callback);
    		if (index == null) {
    			index = new Integer(this.callbacks.size());
    			indexes.put(callback, index);
    		}
    		methodMap.put(method, index);
    		this.callbacks.add(callback);
    	}
    }

    protected abstract Object getCallback(Method method);

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
