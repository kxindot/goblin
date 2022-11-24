package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.convert;
import static com.kxindot.goblin.Objects.isEmpty;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.kxindot.goblin.exception.RuntimeException;
import com.kxindot.goblin.typeconvert.TypeConvertException;

/**
 * @author ZhaoQingJiang
 */
public final class Reflections {
    
    /** 空Object数组 */
    static final Object[] Empty_Objects = new Object[0];
    
    
    public static Method findGetter(Class<?> cls, String fieldName) {
        
        return null;
    }
    
    public static Method findSetter(Class<?> cls, String fieldName) {
        
        return null;
    }
    
    public static Method findMethod(String methodName) {
        
        return null;
    }
    
    
    public static PropertyDescriptor findPropertyDescriptor(Class<?> cls, String fieldName) {
        
        return null;
    }
    
    
    /**
     * 创建类实例
     * @param cls Class
     * @return T 类实例
     * @throws NullPointerException 若cls==null,则抛出此异常
     * @throws ReflectionException 若创建实例失败,则抛出此异常
     */
    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException 
                | IllegalAccessException e) {
            throw new ReflectionException(e, "反射创建%s对象失败", cls.getSimpleName());
        }
    }
    
    /**
     * 创建类实例
     * @param cls Class
     * @param constructArgs 类构造器入参
     * @return T 类实例
     * @throws NullPointerException 若cls==null,则抛出此异常
     * @throws ReflectionException 若创建实例失败,则抛出此异常
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T newInstance(Class<T> cls, Object... constructArgs) {
        if (isEmpty(constructArgs)) return newInstance(cls);
        if (Objects.isAllNotNull(constructArgs)) {
            Class<?>[] types = Objects.copyOf(constructArgs, Class[].class, e -> e.getClass());
            try {
                Constructor<T> constructor = cls.getConstructor(types);
                if (constructor == null) {
                    throw new ReflectionException(String.
                            format("Can't find constructor with parameter type : %s on class %s", types, cls.getName()));
                }
                return newInstance(constructor, constructArgs);
            } catch (NoSuchMethodException 
                    | SecurityException e) {
                throw new ReflectionException(e);
            }
        }
        int len = constructArgs.length;
        List<Constructor<?>> cs = Arrays.stream(cls.getConstructors())
                .filter(e -> len == e.getParameterCount()).collect(Collectors.toList());
        if (Objects.isNotEmpty(cs)) {
            RuntimeException ex = null;
            for (Constructor constructor : cs) {
                try {
                    return (T) newInstance(constructor, constructArgs);
                } catch (ReflectionException e) {
                    ex = e;
                }
            }
            if (ex != null) {
                throw ex;
            }
        }
        throw new ReflectionException(String.
                format("Can't find constructor whith parameterCount=%d on class %s", len, cls.getName()));
    }
    
    /**
     * 创建一个指定长度的数组对象
     * @param cls 数组类型
     * @param length 数组长度
     * @return T[] - 数组对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArrayInstance(Class<? extends T[]> cls, int length) {
        return (T[]) Array.newInstance(cls.getComponentType(), length);
    }
    
    /**
     * 设置对象field的值
     * @param obj Object
     * @param field Field
     * @param val Object
     */
    public static void setValue(Object obj, Field field, Object val) {
        try {
            if (!field.isAccessible())
                field.setAccessible(true);
            field.set(obj, val);
        } catch (IllegalArgumentException 
                | IllegalAccessException e) {
            throw new ReflectionException(e, "Failed to set field value, field : %s, class : %s, value : %s"
                    , field.getName(), obj.getClass().getSimpleName(), val);
        }
    }
    
    public static Object invokeGetter(Object obj, String fieldName) {
        
        return null;
    }
    
    public static Object invokeSetter(Object obj, String fieldName, Object... args) {
        
        return null;
    }
    
    public static Object invoke(Object obj, Method method, Object... args) {
        try {
            if (!method.isAccessible())
                method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (IllegalAccessException 
                | IllegalArgumentException 
                | InvocationTargetException e) {
            throw new ReflectionException(e, "Fail to invoke method, object : %s, method : %s, arguments : %s"
                    , obj.getClass().getSimpleName(), method.getName(), args);
        }
    }
    
    
    public static Object smartInvoke(Object obj, Method method, Object... args) {
        if (args == null) args = Empty_Objects;
        int len = args.length;
        if (len != method.getParameterCount()) {
            throw new ReflectionException("Method parameter count not match, require %d, input %d"
                    , method.getParameterCount(), len);
        }
        try {
            return invoke(obj, method, args);
        } catch (ReflectionException e) {
            if (!IllegalArgumentException.class.isInstance(e.getCause())) {
                // not argument type miss match
                throw e;
            }
            Object[] sargs = new Object[len];
            System.arraycopy(args, 0, sargs, 0, len);
            Class<?>[] types = method.getParameterTypes();
            try {
                for (int i = 0; i < len; i++) {
                    if (!types[i].isInstance(sargs[i])) {
                        sargs[i] = convert(sargs[i], types[i]);
                    }
                }
                return invoke(obj, method, sargs);
            } catch (TypeConvertException ex) {
                // type convert failed, throw original
                throw e;
            } catch (ReflectionException ex) {
                // InvocationTargetException would be the cause
                throw ex;
            }
        }
    }
    
    
    /**
     * 开放类域访问权限
     * @param field Field
     */
    public static void makeAccessiable(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }
    
    /**
     * 开放方法访问权限
     * @param method Method
     */
    public static void makeAccessiable(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
    }
    
    
    /**
     * 调用构造器创建实例对象
     */
    private static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException 
                | IllegalArgumentException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * @author ZhaoQingJiang
     */
    static class ReflectionException extends RuntimeException {
        
        private static final long serialVersionUID = 212321960269231201L;

        public ReflectionException(String message, Object... args) {
            super(message, args);
        }

        public ReflectionException(String message) {
            super(message);
        }

        public ReflectionException(Throwable cause, String message, Object... args) {
            super(cause, message, args);
        }

        public ReflectionException(Throwable cause, String message) {
            super(cause, message);
        }

        public ReflectionException(Throwable cause) {
            super(cause);
        }
    }
    
}
