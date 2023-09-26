package com.kxindot.goblin.resource.property.resolver;

import java.util.Properties;

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
	protected String resovle(String name, Properties properties) {
		return resolve(properties.getProperty(name));
	}

	@Override
	public String resolve(String property) {
		return property;
	}

}
