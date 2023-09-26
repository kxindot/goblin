package com.kxindot.goblin.resource.property.resolver;

import java.util.List;

/**
 * @author ZhaoQingJiang
 */
public class JavaBeanPropertyResolver extends AbstractPropertyResolver<Object> {

	private Object bean;
	private boolean ignoreNotFound;
	private List<JavaBeanFieldPropertyResolver> fields;
	
	public JavaBeanPropertyResolver(Class<?> bean) {
		
	}

	public JavaBeanPropertyResolver(String name, Class<?> bean, boolean ignoreNotFound) {
		
	}
	

}
