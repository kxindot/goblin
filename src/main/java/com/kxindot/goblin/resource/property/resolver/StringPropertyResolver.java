package com.kxindot.goblin.resource.property.resolver;

/**
 * @author ZhaoQingJiang
 */
public class StringPropertyResolver extends AbstractPropertyResolver<String> {

	public StringPropertyResolver() {
		this(null);
	}

	public StringPropertyResolver(String name) {
		super(name, String.class);
	}

	@Override
	public String resolve(String property) {
		return property;
	}

}
