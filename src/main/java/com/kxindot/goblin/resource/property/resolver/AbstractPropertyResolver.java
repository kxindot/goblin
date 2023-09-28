package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Throws.threx;
import static com.kxindot.goblin.typeconvert.TypeConverterFactory.getTypeConverter;

import java.util.Properties;

import com.kxindot.goblin.typeconvert.TypeConvertException;
import com.kxindot.goblin.typeconvert.TypeConverter;

/**
 * @author ZhaoQingJiang
 */
public abstract class AbstractPropertyResolver<T> implements PropertyResolver<T> {

	protected String name;
	protected Class<T> type;
	protected boolean ignoreNotFound;

	public AbstractPropertyResolver() {
		this(null, null);
	}

	public AbstractPropertyResolver(String name) {
		this(name, null);
	}

	public AbstractPropertyResolver(String name, Class<T> type) {
		this(name, type, true);
	}
	
	public AbstractPropertyResolver(String name, Class<T> type, boolean ignoreNotFound) {
		this.name = name;
		this.type = type;
		this.ignoreNotFound = ignoreNotFound;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getType() {
		return type;
	}
	
	boolean ignoreNotFound() {
		return ignoreNotFound;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
    void setType(Class<?> type) {
		this.type = (Class<T>) type;
	}
	
	void setIgnoreNotFound(boolean ignoreNotFound) {
		this.ignoreNotFound = ignoreNotFound;
	}

	@Override
	public T resolve(Properties properties) {
		requireNotBlank(name, "配置名称不能为空!");
		requireNotNull(properties, "properties == null");
		return resolve(name, properties);
	}

	@Override
	public T resolve(String property) {
		if (isNull(property)) {
			return null;
		}
		T t = null;
		TypeConverter<String, T> converter = getTypeConverter(String.class, type);
		if (isNull(converter)) {
			threx(PropertyResolveException::new, "无法将字符串%s解析为%s类型!", property, type.getName());
		}
		try {
			t = converter.convert(property);
		} catch (TypeConvertException e) {
			threx(PropertyResolveException::new, e, "解析字符串%s到%s类型失败!", property, type.getName());
		}
		return t;
	}
	
	protected T resolve(String name, Properties properties) {
		return resolve(getProperty(name, properties));
	}
	
	String getProperty(String name, Properties properties) {
		if (!ignoreNotFound && !properties.containsKey(name)) {
			threx(PropertyResolveException::new, "未找到%s对应的配置信息!", name);
		}
		return properties.getProperty(name);
	}

}
