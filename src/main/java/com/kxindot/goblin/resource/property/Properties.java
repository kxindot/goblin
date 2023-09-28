package com.kxindot.goblin.resource.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author ZhaoQingJiang
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Properties {

	
	String prefix() default "";
	
	
	boolean ignoreNotFound() default true;
	
}
