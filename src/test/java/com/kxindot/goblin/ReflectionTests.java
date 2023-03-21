package com.kxindot.goblin;

import static com.kxindot.goblin.Reflections.findGenericParameterType;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kxindot.goblin.testkit.JunitTests;

public class ReflectionTests extends JunitTests {

    @Override
    public void beforeEach() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterEach() {
        // TODO Auto-generated method stub
        
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
