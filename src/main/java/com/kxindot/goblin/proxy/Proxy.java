package com.kxindot.goblin.proxy;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author zhaoqingjiang
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Proxy {

}
