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
		super(name, List.class, resolver);
	}

	@Override
	protected void set(List<E> c, int index, E value) {
	    //TODO 若按下标顺序插入元素则要考虑扩容,目前只实现添加
	    c.add(value);
	}

	@Override
	protected List<E> instance() {
		return newArrayList();
	}

}
