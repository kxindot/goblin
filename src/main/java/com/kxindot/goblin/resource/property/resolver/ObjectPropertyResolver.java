package com.kxindot.goblin.resource.property.resolver;

/**
 * @author ZhaoQingJiang
 */
public class ObjectPropertyResolver extends AbstractPropertyResolver<Object> {

	public ObjectPropertyResolver() {
		super();
	}

	public ObjectPropertyResolver(String name) {
		super(name);
	}

	@Override
	public Object resolve(String property) {
		return property;
	}

}
