package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.asList;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Reflections.findGenericParameterType;
import static com.kxindot.goblin.Reflections.listMethods;
import static com.kxindot.goblin.Reflections.listMethodsByName;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kxindot.goblin.testkit.JunitTests;

import lombok.Data;

public class ReflectionTests extends JunitTests {
    
    public interface IIBean {
        void beanTest();
    }
    
    public interface IBean extends IIBean {
        @Override
        default void beanTest() {
            
        }
    }
    
    
    @Data
    public static class Bean implements IBean {
        private String name;
        private Integer age;

        @Override
        public void beanTest() {
            
        }
    }
    
    @Data
    public static class InheritBean extends Bean {
        private String address;
        
        @Override
        public void beanTest() {
        }
    }
    
    
    public static void main(String[] args) throws Exception {
//        List<Method> list = listMethodsByName(InheritBean.class, "beanTest", true);
        List<Method> list = listMethods(InheritBean.class, true);
        
        list.forEach(System.out::println);
    }
    
    
    

    @Override
    public void beforeEach() {
        
    }

    @Override
    public void afterEach() {
        
    }
    
    @Test
    public void test() {
        Type[] genericInterfaces = ChildClass.class.getGenericInterfaces();
        System.out.println(genericInterfaces);
        Type[] genericInterfaces2 = ParentClass.class.getGenericInterfaces();
        System.out.println(genericInterfaces2);
    }
    
    @Test
    public void findGenericParameterTypeTest() {
        Class<?> result = findGenericParameterType(ChildClass.class, 0);
        Assertions.assertEquals(result, Integer.class);
        result = findGenericParameterType(ChildClass.class, ParentClass.class, 0);
        Assertions.assertEquals(result, Integer.class);
//        result = findGenericParameterType(ChildClass.class, ParentInterface.class, 0);
//        Assertions.assertEquals(result, Integer.class);
        result = findGenericParameterType(ChildClass.class, GrandParentClass.class, 0);
        Assertions.assertEquals(result, Integer.class);
        result = findGenericParameterType(ChildClass.class, GrandParentInterface.class, 0);
        Assertions.assertEquals(result, Integer.class);
        
    }
    
    interface GrandParentInterface<T> {
        
    }
    
    static class GrandParentClass<T> implements GrandParentInterface<T> {
        
    }

    interface ParentInterface<T> {
        
    }
    
    static class ParentClass<T> extends GrandParentClass<T> implements ParentInterface<T> {
        
    }
    
    static class ChildClass extends ParentClass<Integer> {
        
    }
}
