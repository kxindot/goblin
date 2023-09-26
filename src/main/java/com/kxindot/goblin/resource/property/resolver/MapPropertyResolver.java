package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Objects.newHashMap;

import java.util.Map;

/**
 * @author ZhaoQingJiang
 */
public class MapPropertyResolver<V> extends AbstractPropertyResolver<Map<String, V>> {
	
	private Map<String, V> map;
	private PropertyResolver<V> valueResolver;
	
	public MapPropertyResolver(PropertyResolver<V> resolver) {
		this(null, resolver);
	}

	@SuppressWarnings("unchecked")
	public MapPropertyResolver(String name, PropertyResolver<V> resolver) {
		super(name);
		this.map = newHashMap();
		setType(((Class<Map<String, V>>) map.getClass()));
	}
	
	
	public Class<V> getValueType() {
		return valueResolver.getType();
	}
	

}
