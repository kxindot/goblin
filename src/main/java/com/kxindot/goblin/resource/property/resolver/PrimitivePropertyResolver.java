package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Classes.isPrimitiveOrWrapper;
import static com.kxindot.goblin.Objects.convert;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Throws.threx;

import java.util.Properties;

import com.kxindot.goblin.resource.property.PropertyUtil;
import com.kxindot.goblin.typeconvert.TypeConvertException;

/**
 * @author ZhaoQingJiang
 */
public class PrimitivePropertyResolver<T> extends AbstractPropertyResolver<T> {

	public PrimitivePropertyResolver(Class<T> type) {
		this(null, type);
	}

	public PrimitivePropertyResolver(String name, Class<T> type) {
		super(name, type);
		if (!isPrimitiveOrWrapper(type)) {
			threx(IllegalArgumentException::new, "%s不是Java语言基础类型或基础包装类型!", type.getName());
		}
	}

	@Override
	protected T resovle(String name, Properties properties) {
		return PropertyUtil.getProperty(properties, name, type);
	}

	@Override
	public T resolve(String property) {
		T t = null;
		if (isNotNull(property)) {
			try {
				t = convert(property, type);
			} catch (TypeConvertException e) {
				threx(PropertyResolveException::new, e, "解析字符串%s到%s类型异常", property, type.getName());
			}
		}
		return t;
	}
}
