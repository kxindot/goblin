package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Classes.isInstance;
import static com.kxindot.goblin.Objects.Comma;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.substringAfter;
import static com.kxindot.goblin.Objects.substringBetween;
import static com.kxindot.goblin.Throws.threx;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import com.kxindot.goblin.Regex;

/**
 * @author ZhaoQingJiang
 */
public abstract class CollectionPropertyResolver<E, C extends Collection<E>> extends AbstractPropertyResolver<C> {

	private static final String PA = "^\\[(0|[1-9]+?\\d*)\\]$";
	protected String seperator;
	protected PropertyResolver<E> resolver;

	public CollectionPropertyResolver(Class<?> type, PropertyResolver<E> resolver) {
	    this(null, type, resolver);
	}

	public CollectionPropertyResolver(String name, Class<?> type, PropertyResolver<E> resolver) {
	    this(name, Comma, type, resolver);
	}

	public CollectionPropertyResolver(String name, String seperator, Class<?> type, PropertyResolver<E> resolver) {
		super(name);
		this.seperator = requireNotBlank(seperator, "分隔符不能为空!");
		this.resolver = requireNotNull(resolver, "集合元素属性解析器不能为空!");
		setType(type);
	}

	public Class<?> getElementType() {
		return resolver.getType();
	}
	
	@Override
	public C resolve(String property) {
		C c = instance();
		if (isNotNull(property)) {
			String[] array = property.split(seperator);
			for (String prop : array) {
				c.add(resolver.resolve(prop));
			}
		}
		return c;
	}

	@Override
	protected C resovle(String name, Properties properties) {
		C c = resolve(properties.getProperty(name));
		Set<Object> keys = properties.keySet();
		for (Object value : keys) {
			if (!isInstance(String.class, value)) {
				continue;
			}
			String key = String.class.cast(value);
			if (key.equals(name) || !key.startsWith(name)) {
				continue;
			}
			String index = substringAfter(key, name);
			if (!Regex.isMatch(index, PA)) {
				threx(PropertyResolveException::new, "不合法的数组集合定义语法: %s", key);
			}
			E e = resolver.resolve(properties.getProperty(key));
			int i = Integer.valueOf(substringBetween(index, "[", "]"));
			set(c, i, e);
		}
		return c;
	}
	
	protected abstract void set(C collection, int index, E value);

	protected abstract C instance();

}
