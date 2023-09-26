package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Objects.newArrayList;

import java.util.List;

/**
 * @author ZhaoQingJiang
 */
public class ListPropertyResolver<E> extends CollectionPropertyResolver<E, List<E>> {
	
	public ListPropertyResolver(PropertyResolver<E> resolver) {
		this(null, resolver);
	}
	
	public ListPropertyResolver(String name, PropertyResolver<E> resolver) {
		super(name, resolver);
	}

	@Override
	protected void set(List<E> collection, int index, E value) {
		collection.add(index, value);
	}

	@Override
	protected List<E> instance() {
		return newArrayList();
	}

}
