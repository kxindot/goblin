package com.kxindot.goblin.resource.property;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author ZhaoQingJiang
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Property {

	
	String value() default "";
	
	
}
