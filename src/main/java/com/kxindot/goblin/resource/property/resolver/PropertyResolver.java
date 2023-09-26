package com.kxindot.goblin.resource.property.resolver;

import java.util.Properties;

/**
 * @author ZhaoQingJiang
 */
public interface PropertyResolver<T> {
	
    
    String getName();
    
    
    Class<?> getType();
    
	
	T resolve(Properties properties);
	
	
	T resolve(String property);
	
	
	void setName(String name);
	
}
