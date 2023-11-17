package com.kxindot.goblin.resource.property;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 有序Properties.
 * 
 * @author ZhaoQingJiang
 */
public class OrderedProperties extends Properties {

	private static final long serialVersionUID = -7089205660040159160L;

	private Set<Object> keys =  new LinkedHashSet<>();
	
	
	@Override
	public synchronized Object put(Object key, Object value) {
		Object old = super.put(key, value);
		keys.add(key);
		return old;
	}
	
	@Override
	public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(keys);
	}
	
	@Override
	public Set<Object> keySet() {
		return keys;
	}
	
	@Override
	public Set<String> stringPropertyNames() {
		return keys.stream().map(Object::toString).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}
