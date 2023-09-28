package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Classes.isPrimitiveOrWrapper;
import static com.kxindot.goblin.Throws.threx;

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
}
