package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Objects.newHashSet;

import java.util.Set;

/**
 * @author ZhaoQingJiang
 */
public class SetPropertyResolver<E> extends CollectionPropertyResolver<E, Set<E>> {

	public SetPropertyResolver(PropertyResolver<E> resolver) {
		this(null, resolver);
	}
	
	public SetPropertyResolver(String name, PropertyResolver<E> resolver) {
		super(name, resolver);
	}

	@Override
	protected void set(Set<E> collection, int index, E value) {
		collection.add(value);
	}

	@Override
	protected Set<E> instance() {
		return newHashSet();
	}

}
