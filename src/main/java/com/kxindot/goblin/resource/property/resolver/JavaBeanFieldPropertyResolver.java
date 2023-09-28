package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Classes.isAssignableFrom;
import static com.kxindot.goblin.Classes.isPrimitiveOrWrapper;
import static com.kxindot.goblin.Objects.Dot;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.stringFormat;
import static com.kxindot.goblin.Reflections.findGenericParameterType;
import static com.kxindot.goblin.Reflections.invokeSetter;
import static com.kxindot.goblin.Reflections.setValue;
import static com.kxindot.goblin.Throws.threx;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.kxindot.goblin.Reflections.ReflectionException;
import com.kxindot.goblin.resource.property.Property;

/**
 * @author ZhaoQingJiang
 */
@SuppressWarnings("rawtypes")
public class JavaBeanFieldPropertyResolver extends AbstractPropertyResolver<Object> {

	private Field field;
	private PropertyResolver resolver;

	public JavaBeanFieldPropertyResolver(String prefix, Field field, boolean ignoreNotFound) {
		this.field = field;
		name = getName(prefix, field);
		setType(field.getType());
		setIgnoreNotFound(ignoreNotFound);
		this.resolver = getResolver(name, field, ignoreNotFound);
	}

	@Override
	public Object resolve(String property) {
		throw new AbstractMethodError();
	}

	@Override
	public Object resolve(Properties properties) {
		return resolver.resolve(properties);
	}

	protected Object resolve(Object bean, Properties properties) {
		Object value = resolve(properties);
		try {
			invokeSetter(bean, field.getName(), value);
		} catch (ReflectionException e) {
			setValue(bean, field, value);
		}
		return value;
	}

	private String getName(String prefix, Field field) {
		Property property = field.getAnnotation(Property.class);
		name = isNull(property) ? field.getName() : isBlank(property.value()) ? field.getName() : property.value();
		return isBlank(prefix) ? name : prefix + Dot + name;
	}

	@SuppressWarnings("unchecked")
	private PropertyResolver getResolver(String name, Field field, boolean ignoreNotFound) {
		Class<?> type = field.getType();
		PropertyResolver resolver = getResolver(name, type);
		if (isNull(resolver)) {
			if (type.isArray()) {
				resolver = getElementResolver(name, type.getComponentType(), "不支持的数组类型: %s", type.getName());
				resolver = new ArrayPropertyResolver<>(name, type, resolver);
			} else if (isAssignableFrom(Collection.class, type)) {
				Class<?> eleType = findGenericParameterType(field, 0);
				resolver = getElementResolver(name, eleType, "不支持的集合元素类型: %s", eleType.getName());
				if (isAssignableFrom(List.class, type)) {
					resolver = new ListPropertyResolver<>(name, resolver);
				} else if (isAssignableFrom(Set.class, type)) {
					resolver = new SetPropertyResolver<>(name, resolver);
				} else {
					threx(PropertyResolveException::new, "未识别的集合类型: %s", type.getName());
				}
			} else if (isAssignableFrom(Map.class, type)) {
				Class<?> keyType = findGenericParameterType(field, 0);
				if (!String.class.equals(keyType)) {
					threx(PropertyResolveException::new, "Map键类型只能为String类型: %s", keyType.getName());
				}
				Class<?> valType = findGenericParameterType(field, 1);
				resolver = getElementResolver(name, valType, "不支持的Map-Value类型: %s", valType.getName());
				resolver = new MapPropertyResolver<>(name, resolver);
			} else {
				resolver = new JavaBeanPropertyResolver(name, type, ignoreNotFound);
			}
		}
		AbstractPropertyResolver.class.cast(resolver).setType(type);
		return resolver;
	}

	private PropertyResolver getResolver(String name, Class<?> type) {
		if (String.class.equals(type) || isAssignableFrom(type, String.class)) {
			return new StringPropertyResolver(name);
		} else if (isPrimitiveOrWrapper(type)) {
			return new PrimitivePropertyResolver<>(name, type);
		} else if (BigDecimal.class.equals(type) || isAssignableFrom(type, BigDecimal.class)) {
			return new BigDecimalPropertyResolver(name);
		}
		return null;
	}

	private PropertyResolver getElementResolver(String name, Class<?> type, String errMsg, Object... args) {
		PropertyResolver resolver = getResolver(name, type);
		if (isNull(resolver)) {
			if (!Object.class.equals(type)) {
				threx(PropertyResolveException::new, stringFormat(errMsg, args));
			}
			resolver = new ObjectPropertyResolver(name);
		}
		return resolver;
	}

}
