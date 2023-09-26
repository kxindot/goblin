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
@Retention(RUNTIME)
@Target(TYPE)
public @interface PropertySource {

	
	String[] value();
	
	
	boolean ignoreNotFound() default true;
	
}
