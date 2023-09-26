package com.kxindot.goblin.resource.property.resolver;

import java.lang.reflect.Field;

/**
 * @author ZhaoQingJiang
 */
public class JavaBeanFieldPropertyResolver extends AbstractPropertyResolver<Object> {

	private Field field;
	private PropertyResolver fieldResolver;
	private JavaBeanPropertyResolver beanResolver;
	
	public JavaBeanFieldPropertyResolver(Field field, JavaBeanPropertyResolver resolver) {
		
	}
	
	
}
