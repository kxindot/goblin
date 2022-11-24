package com.kxindot.goblin;

import java.lang.reflect.Method;

import com.sun.beans.TypeResolver;

public class BeanTests {
    
    
    public static void main(String[] args) {
        Method method = null;
        Method[] methods = TestClass.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("---------------------");
            System.out.println(m.toGenericString());
            System.out.println(m.getDeclaringClass());
            Class<?> r1 = getReturnType(null, m);
            Class<?> r2 = m.getReturnType();
            System.out.printf("r1 : %s\n", r1.toGenericString());
            System.out.printf("r2 : %s\n", r2.toGenericString());
            System.out.printf("r1 = r2 : %s\n", r1.equals(r2));
            System.out.println("---------------------\n");
        }
    }

    
    static Class<?> getReturnType(Class<?> base, Method method) {
        if (base == null) {
            base = method.getDeclaringClass();
        }
        return TypeResolver.erase(TypeResolver.resolveInClass(base, method.getGenericReturnType()));
    }
    
    
    public interface TestInterface<T> {
        void set(T t);
        T get();
    }
    
    public static class TestClass implements TestInterface<String> {
        private Integer age;
        
        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
        @Override
        public void set(String t) {
        }
        @Override
        public String get() {
            return null;
        }
        
    }
    
}
