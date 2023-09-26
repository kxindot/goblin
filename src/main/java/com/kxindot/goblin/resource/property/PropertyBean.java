package com.kxindot.goblin.resource.property;

import static com.kxindot.goblin.Classes.isAssignableFrom;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.unmodifiableEmptyList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kxindot.goblin.Reflections;

/**
 * @author ZhaoQingJiang
 */
class PropertyBean {

	private String prefix;
	
	private Class<?> type;
	
	private boolean ignoreUnknown;
	
	private List<PropertyBeanField> fields;
	

	PropertyBean(Class<?> type) {
		Properties props = type.getAnnotation(Properties.class);
		if (isNotNull(props)) {
			prefix = props.prefix();
			ignoreUnknown = props.ignoreUnknown();
		}
		this.type = type;
		init();
	}
	
	
	PropertyBean(Class<?> type, String prefix, boolean ignoreUnknown) {
		this.type = type;
		this.prefix = prefix;
		this.ignoreUnknown = ignoreUnknown;
		init();
	}
	
	
	void init() {
		List<Field> members = Reflections.listMemberFields(type, true);
		if (isEmpty(members)) {
			fields = unmodifiableEmptyList();
		} else {
			
		}
	}
	
	
	void addField(Field field) {
		if (isNull(fields)) {
			fields = newArrayList();
		}
		fields.add(new PropertyBeanField(field, prefix, ignoreUnknown));
	}
	

	/**
	 * @author ZhaoQingJiang
	 */
	class PropertyBeanField extends PropertyBean {
		
		private String name;
		private Object value;
		private Field field;
		
		
		PropertyBeanField(Field field, String prefix, boolean ignoreUnknown) {
			super(field.getType(), prefix, ignoreUnknown);
			name = field.getName();
			Property prop = field.getAnnotation(Property.class);
			if (isNotNull(prop)) {
				name = prop.value();
				if (isBlank(name)) {
					name = field.getName();
				}
			}
			type = field.getType();
			PropertyDefaultValue propValue = field.getAnnotation(PropertyDefaultValue.class);
			if (isNotNull(propValue)) {
				String[] value = propValue.value();
				if (isNotEmpty(value)) {
					this.value = value;
				}
			}
			this.field = field;
		}
		
	}
	
}
