package com.kxindot.goblin.resource.property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.kxindot.goblin.test.JunitTests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

class PropertyUtilTest extends JunitTests {
	
	private java.util.Properties properties;
	
	@BeforeAll
	public void beforeAll() {
		super.beforeAll();
		properties = new java.util.Properties();
		properties.setProperty("school.name", "思中");
		properties.setProperty("school.teachers", "老王,老顾");
		properties.setProperty("school.teachers[1]", "老朱");
		properties.setProperty("school.address.province", "黔地");
		properties.setProperty("school.address.city", "东南");
		properties.setProperty("school.address.detail", "府后大道二十二号");
		properties.setProperty("school.students", "麻子=一年级");
		properties.setProperty("school.students{王二}", "二年级");
		properties.setProperty("school.students{张三}", "三年级");
		properties.setProperty("school.students{李四}", "四年级");
		properties.setProperty("school.principal.name", "老赵");
		properties.setProperty("school.principal.age", "38");
		properties.setProperty("school.principal.height", "170.3");
		properties.setProperty("school.principal.sex", "男");
		properties.setProperty("school.principal.married", "true");
		properties.setProperty("student.name", "小赵");
		properties.setProperty("student.age", "13");
		properties.setProperty("student.height", "163.8");
		properties.setProperty("student.sex", "男");
		properties.setProperty("student.married", "false");
		properties.setProperty("student.grade", "1");
		properties.setProperty("student.scores{语文}", "135.2");
		properties.setProperty("student.scores{数学}", "142.3");
		properties.setProperty("student.scores{英语}", "148");
		properties.setProperty("student.home.province", "黔地");
		properties.setProperty("student.home.city", "东南");
		properties.setProperty("student.home.detail", "江边大道八十八号");
	}
	
	@Data
	@Properties(prefix = "school")
	public static class School {
		private String name;
		@Property("teachers")
		private LinkedList<String> teacherNames;
		private Address address;
		private Map<String, String> students;
		private Person principal;
	}
	

	@Data
	public static class Person {
		private String name;
		private Short age;
		private Double height;
		private CharSequence sex;
		private Boolean married;
	}
	
	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	@Properties(prefix = "student")
	public static class Student extends Person {
		private Integer grade;
		private Map<String, BigDecimal> scores;
		@Property("home")
		private Address address;
	}
	
	@Data
	public static class Address {
		private String province;
		private String city;
		private String detail;
	}

	@Test
	void testGetShortStringProperties() {
		assertEquals((short) 38, PropertyUtil.getShort("school.principal.age", properties));
	}

	@Test
	void testGetShortStringPropertiesShort() {
		assertEquals((short) 38, PropertyUtil.getShort("school.age", properties, (short) 38));
	}

	@Test
	void testGetIntegerStringProperties() {
		assertEquals(1, PropertyUtil.getInteger("student.grade", properties));
	}

	@Test
	void testGetIntegerStringPropertiesInteger() {
		assertEquals(1, PropertyUtil.getInteger("grade", properties, 1));
	}

	@Test
	void testGetLongStringProperties() {
	}

	@Test
	void testGetLongStringPropertiesLong() {
	}

	@Test
	void testGetFloatStringProperties() {
	}

	@Test
	void testGetFloatStringPropertiesFloat() {
	}

	@Test
	void testGetDoubleStringProperties() {
		assertEquals(170.3d, PropertyUtil.getDouble("school.principal.height", properties));
	}

	@Test
	void testGetDoubleStringPropertiesDouble() {
		assertEquals(170.3d, PropertyUtil.getDouble("school.height", properties, 170.3d));
	}

	@Test
	void testGetBigDecimalStringProperties() {
		assertEquals(BigDecimal.valueOf(170.3d), PropertyUtil.getBigDecimal("school.principal.height", properties));
	}

	@Test
	void testGetBigDecimalStringPropertiesBigDecimal() {
		assertEquals(BigDecimal.valueOf(170.3d), PropertyUtil.getBigDecimal("school.height", properties, BigDecimal.valueOf(170.3d)));
	}

	@Test
	void testGetStringStringProperties() {
		assertEquals("老赵", PropertyUtil.getString("school.principal.name", properties));
	}

	@Test
	void testGetStringStringPropertiesString() {
		assertEquals("老赵", PropertyUtil.getString("school.principal", properties, "老赵"));
	}

	@Test
	void testGetJavaBeanClassOfTProperties() {
		School school = PropertyUtil.getJavaBean(School.class, properties);
		Student student = PropertyUtil.getJavaBean(Student.class, properties);
		println("school: {}", school);
		println("student: {}", student);
	}

}
