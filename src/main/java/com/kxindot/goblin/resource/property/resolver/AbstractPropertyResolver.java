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

	public AbstractPropertyResolver() {
		this(null, null);
	}

	public AbstractPropertyResolver(String name) {
		this(name, null);
	}

	public AbstractPropertyResolver(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	void setType(Class<T> type) {
		this.type = type;
	}

	@Override
	public T resolve(Properties properties) {
		requireNotNull(properties, "properties == null");
		requireNotBlank(name, "name can'b be null or blank");
		return resovle(name, properties);
	}

	protected T resovle(String name, Properties properties) {
		return resolve(properties.getProperty(name));
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

}
