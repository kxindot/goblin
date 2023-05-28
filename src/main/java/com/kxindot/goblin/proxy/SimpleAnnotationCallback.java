package com.kxindot.goblin.proxy;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.newHashMap;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.proxy.MethodProxy;

/**
 * @author ZhaoQingJiang
 */
public abstract class SimpleAnnotationCallback<T, A extends Annotation> extends SimpleCallback<T> {
    
    private Class<A> annotation;
    private Map<String, A> annotationCache;

    public SimpleAnnotationCallback(Class<T> superclass, Class<A> annotation) {
        super(superclass, null);
        this.annotation = requireNotNull(annotation, "annotation == null");
        this.annotationCache = newHashMap();
    }

    @Override
    public boolean accept(Class<?> type, Method method) {
        A instance = findAnnotation(type, method, annotation);
        if (isNotNull(instance) && accept(type, method, instance)) {
            annotationCache.put(method.toString(), instance);
            return true;
        }
        return false;
    }
    
    protected abstract boolean accept(Class<?> type, Method method, A annotation);
    
    protected A findAnnotation(Class<?> type, Method method, Class<A> annotation) {
        return method.getAnnotation(annotation);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return intercept(obj, method, args, annotationCache.get(method.toString()), proxy);
    }

    protected abstract Object intercept(Object obj, Method method, Object[] args, A annotation, MethodProxy proxy) throws Throwable;
    
}
