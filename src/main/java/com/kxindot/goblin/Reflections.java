package com.kxindot.goblin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhaoQingJiang
 */
public final class Reflections {

    private Reflections() {}
    
    
    /**
     * 创建对象实例
     * @param cls Class
     * @return T 对象实例
     * @throws ReflectionException 若创建实例失败,则抛出该异常
     */
    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException 
                | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }
    
    /**
     * 创建对象实例
     * @param cls Class
     * @param constructArgs 对象构造器入参
     * @return T 对象实例
     * @throws ReflectionException 若创建实例失败,则抛出该异常
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T newInstance(Class<T> cls, Object... constructArgs) {
        if (Objects.isEmpty(constructArgs)) return newInstance(cls);
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
        
        public ReflectionException(String message) {
            super(message);
        }

        public ReflectionException(Throwable cause) {
            super(cause);
        }
        
        public ReflectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
}
