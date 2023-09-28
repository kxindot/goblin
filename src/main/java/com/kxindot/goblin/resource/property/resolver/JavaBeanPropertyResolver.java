package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.unmodifiableEmptyList;
import static com.kxindot.goblin.Reflections.newInstance;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import com.kxindot.goblin.Reflections;

/**
 * @author ZhaoQingJiang
 */
public class JavaBeanPropertyResolver extends AbstractPropertyResolver<Object> {

	private List<JavaBeanFieldPropertyResolver> fields;
	
	public JavaBeanPropertyResolver(Class<?> bean, boolean ignoreNotFound) {
		this(null, bean, ignoreNotFound);
	}

	public JavaBeanPropertyResolver(String name, Class<?> bean, boolean ignoreNotFound) {
		super(name);
		setType(bean);
		setIgnoreNotFound(ignoreNotFound);
		this.fields = listFields(name, bean, ignoreNotFound);
	}
	
	@Override
	public Object resolve(String property) {
		throw new AbstractMethodError();
	}

	@Override
	public Object resolve(Properties properties) {
		Object bean = newInstance(type);
		for (JavaBeanFieldPropertyResolver resolver : fields) {
			resolver.resolve(bean, properties);
		}
		return bean;
	}
	
	private List<JavaBeanFieldPropertyResolver> listFields(String name, Class<?> type, boolean ignoreNotFound) {
		List<Field> fields = Reflections.listMemberFields(type, true);
		List<JavaBeanFieldPropertyResolver> list = isEmpty(fields) 
				? unmodifiableEmptyList() : newArrayList(fields.size());
		for (Field field : fields) {
			list.add(new JavaBeanFieldPropertyResolver(name, field, ignoreNotFound));
		}
		return list;
	}

}
