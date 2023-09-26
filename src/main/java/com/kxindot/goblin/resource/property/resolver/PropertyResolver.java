package com.kxindot.goblin.resource.property.resolver;

import java.util.Properties;

/**
 * @author ZhaoQingJiang
 */
public interface PropertyResolver<T> {
	
	
	T resolve(Properties properties);
	
	
	T resolve(String property);
	
	
	String getName();
	
	
	Class<T> getType();
	
	
	void setName(String name);
	
}
