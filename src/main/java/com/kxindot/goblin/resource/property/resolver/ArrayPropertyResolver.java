package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Throws.threx;

import java.util.List;
import java.util.Properties;

import com.kxindot.goblin.Reflections;

/**
 * @author ZhaoQingJiang
 */
public class ArrayPropertyResolver<T> extends AbstractPropertyResolver<T[]> {

	private ListPropertyResolver<T> resolver;

	public ArrayPropertyResolver(Class<?> type, PropertyResolver<T> resolver) {
		this(null, type, resolver);
	}

	public ArrayPropertyResolver(String name, Class<?> type, PropertyResolver<T> resolver) {
		super(name);
		if (!type.isArray()) {
			threx(PropertyResolveException::new, "%s不是数组类型!", type.getName());
		} else if (type.getComponentType() != resolver.getType()) {
			threx(PropertyResolveException::new, "%s不是%s的数组类型", type.getName(), resolver.getType().getName());
		}
		setType(type);
		this.resolver = new ListPropertyResolver<>(name, resolver);
	}

	public Class<?> getElementType() {
		return resolver.getElementType();
	}

	@Override
	public T[] resolve(String property) {
		return toArray(resolver.resolve(property));
	}

	@Override
	public T[] resolve(Properties properties) {
		return toArray(resolver.resolve(properties));
	}

	T[] toArray(List<T> list) {
		return list.toArray(Reflections.newArrayInstance(type, 0));
	}

}
