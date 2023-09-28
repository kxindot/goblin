package com.kxindot.goblin.resource.property.resolver;

import java.util.Optional;
import java.util.Properties;

/**
 * 配置解析器接口.
 * 
 * @author ZhaoQingJiang
 */
public interface PropertyResolver<T> {
	
    /**
     * 获取配置值对应的名称(Key).
     * @return String 配置值名称
     */
    String getName();
    
    /**
     * 设置配置值对应的名称(Key).
     * @param name 配置值名称
     */
    void setName(String name);
    
    /**
     * 获取配置值类型.
     * @return {@code Class<?>} 配置值类型
     */
    Class<?> getType();
    
    /**
     * 解析配置对象{@link Properties},返回配置值.
     * @param properties {@link Properties}
     * @return T 配置值
     * @throws IllegalArgumentException 若properties=null或配置值名称等于null,则抛出此异常.
     */
    T resolve(Properties properties);
    
	/**
	 * 解析字符串,返回配置值.若property=null,则返回null.
	 * @param property String
	 * @return T 配置值
	 */
    T resolve(String property);
    
	/**
	 * 解析字符串,返回配置值.若返回值等于null,则返回默认值.
	 * @param property String
	 * @param defaultValue 默认值
	 * @return T 配置值
	 */
    default T resolve(String property, T defaultValue) {
    	return Optional.ofNullable(resolve(property)).orElse(defaultValue);
    }
	
}
