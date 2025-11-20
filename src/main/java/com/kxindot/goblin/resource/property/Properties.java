package com.kxindot.goblin.resource.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 配置注解。
 * 
 * @author ZhaoQingJiang
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Properties {
	
	/**
	 * 配置（资源）文件名称。
	 * 
	 * @return String
	 */
	String resource() default "";

	/**
	 * 配置项前缀。
	 * 
	 * @return String
	 */
	String prefix() default "";
	
	/**
	 * 是否忽略未找到的配置属性。
	 * 
	 * @return boolean
	 */
	boolean ignoreNotFound() default true;
	
}
