package com.kxindot.goblin.resource.property.resolver;

import java.util.List;
import java.util.Properties;

import com.kxindot.goblin.Reflections;

/**
 * @author ZhaoQingJiang
 */
public class ArrayPropertyResolver<T> extends AbstractPropertyResolver<T[]> {

	private ListPropertyResolver<T> resolver;

	public ArrayPropertyResolver(Class<T[]> type) {
		this(null, type);
	}

	@SuppressWarnings("unchecked")
	public ArrayPropertyResolver(String name, Class<T[]> type) {
		super(name);
		setType(type);
		Class<T> componentType = (Class<T>) type.getComponentType();
		PrimitivePropertyResolver<T> primitivePropertyResolver = new PrimitivePropertyResolver<>(componentType);
		resolver = new ListPropertyResolver<>(name, primitivePropertyResolver);
	}

	public Class<T> getElementType() {
		return resolver.getElementType();
	}

	@Override
	public T[] resolve(String property) {
		return toArray(resolver.resolve(property));
	}

	@Override
	protected T[] resovle(String name, Properties properties) {
		return toArray(resolver.resovle(name, properties));
	}
	
	T[] toArray(List<T> list) {
		return list.toArray(Reflections.newArrayInstance(type, 0));
	}

}
