package com.kxindot.goblin.resource.resolver;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.resource.property.resolver.ListPropertyResolver;
import com.kxindot.goblin.resource.property.resolver.MapPropertyResolver;
import com.kxindot.goblin.resource.property.resolver.PrimitivePropertyResolver;
import com.kxindot.goblin.resource.property.resolver.StringPropertyResolver;
import com.kxindot.goblin.testkit.JunitTests;

/**
 * @author ZhaoQingJiang
 */
public class PropertyResolverTests extends JunitTests {

    Properties properties = new Properties();

    @Override
    public void beforeAll() {
        super.beforeAll();
        properties.setProperty("p1", "pv1");
        properties.setProperty("p2", "12");
        properties.setProperty("array", "1,2,34,5");
        properties.setProperty("array[0]", "12");
        properties.setProperty("array[12]", "67");
        properties.setProperty("map", "k1=12,k2=13,k3=14");
        properties.setProperty("map{k6}", "16");
        properties.setProperty("map{k7}", "17");
    }

    @Test
    public void primitivePropertyResolverTest() {
        StringPropertyResolver resolver1 = new StringPropertyResolver("p1");
        System.out.println(resolver1.resolve(properties));
        PrimitivePropertyResolver<Double> resolver2 = new PrimitivePropertyResolver<>("p2", double.class);
        System.out.println(resolver2.resolve(properties));
        ListPropertyResolver<Long> resolver3 = new ListPropertyResolver<>("array", new PrimitivePropertyResolver<>(Long.class));
        System.out.println(resolver3.resolve(properties));
        MapPropertyResolver<Integer> resolver4 = new MapPropertyResolver<>("map", new PrimitivePropertyResolver<>(Integer.class));
        System.out.println(resolver4.resolve(properties));
    }

    @Test
    public void stringPropertyResolverTest() {

    }

    @Test
    public void listPropertyResolverTest() {

    }

    @Test
    public void setPropertyResolverTest() {

    }

    @Test
    public void arrayPropertyResolverTest() {

    }

    @Test
    public void mapPropertyResolverTest() {

    }

    @Test
    public void javaBeanPropertyResolverTest() {

    }

}
