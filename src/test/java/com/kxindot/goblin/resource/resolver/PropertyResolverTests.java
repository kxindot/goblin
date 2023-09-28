package com.kxindot.goblin.resource.resolver;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kxindot.goblin.resource.property.Property;
import com.kxindot.goblin.resource.property.PropertyUtil;
import com.kxindot.goblin.resource.property.resolver.ListPropertyResolver;
import com.kxindot.goblin.resource.property.resolver.MapPropertyResolver;
import com.kxindot.goblin.resource.property.resolver.PrimitivePropertyResolver;
import com.kxindot.goblin.resource.property.resolver.StringPropertyResolver;
import com.kxindot.goblin.testkit.JunitTests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoQingJiang
 */
public class PropertyResolverTests extends JunitTests {

	Properties properties;

	@BeforeAll
	public void beforeAll() {
		super.beforeAll();
		properties = new Properties();
		properties.setProperty("p1", "pv1");
		properties.setProperty("p2", "12");
		properties.setProperty("p3", "98.34");
		properties.setProperty("array", "1,2,34,5");
		properties.setProperty("array[0]", "12");
		properties.setProperty("array[12]", "67");
		properties.setProperty("map", "k1=12,k2=13,k3=14");
		properties.setProperty("map{k6}", "16");
		properties.setProperty("map{k7}", "17");
	}

	@Test
	public void primitivePropertyResolverTest() {
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
		Student student = PropertyUtil.getJavaBean(Student.class, properties);
		System.out.printf("JavaBeanPropertyResolver测试: %s\n", student);
	}

	@Getter
	@ToString
	@com.kxindot.goblin.resource.property.Properties()
	public static class Student {
		@Setter
		@Property("p1")
		private String name;
		@Setter
		@Property("p2")
		private int age;
		@Property("p3")
		private BigDecimal score;
		@Property("array")
		private Long[] arr;
		private Map<String, Double> map;
	}

}
