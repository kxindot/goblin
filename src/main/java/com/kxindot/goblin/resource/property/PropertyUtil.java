package com.kxindot.goblin.resource.property;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Throws.silentThrex;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import com.kxindot.goblin.typeconvert.TypeConvertException;
import com.kxindot.goblin.typeconvert.TypeConverter;
import com.kxindot.goblin.typeconvert.TypeConverterFactory;

/**
 * @author ZhaoQingJiang
 */
public class PropertyUtil {
	
	
	public static Properties newProperties(InputStream in) {
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			silentThrex(e, "加载数据流异常!");
		}
		return properties;
	}
	
	
	public static <T> T getProperty(Class<T> bean, InputStream in) {
		
		return null;
	}

	
	public static Byte getByte(Properties properties, String name) {
		return getByte(properties, name, null);
	}
	
	
	public static Byte getByte(Properties properties, String name, Byte defaultValue) {
		return getProperty(properties, name, Byte.class, defaultValue);
	}
	
	
	public static Short getShort(Properties properties, String name) {
		return getShort(properties, name, null);
	}
	
	
	public static Short getShort(Properties properties, String name, Short defaultValue) {
		return getProperty(properties, name, Short.class, defaultValue);
	}
	
	
	public static Integer getInteger(Properties properties, String name) {
		return getInteger(properties, name, null);
	}
	
	
	public static Integer getInteger(Properties properties, String name, Integer defaultValue) {
		return getProperty(properties, name, Integer.class, defaultValue);
	}
	
	
	public static Long getLong(Properties properties, String name) {
		return getLong(properties, name, null);
	}
	
	
	public static Long getLong(Properties properties, String name, Long defaultValue) {
		return getProperty(properties, name, Long.class, defaultValue);
	}
	
	
	public static Float getFloat(Properties properties, String name) {
		return getFloat(properties, name, null);
	}
	
	
	public static Float getFloat(Properties properties, String name, Float defaultValue) {
		return getProperty(properties, name, Float.class, defaultValue);
	}
	
	
	public static Double getDouble(Properties properties, String name) {
		return getDouble(properties, name, null);
	}
	
	
	public static Double getDouble(Properties properties, String name, Double defaultValue) {
		return getProperty(properties, name, Double.class, defaultValue);
	}
	
	
	public static BigDecimal getBigDecimal(Properties properties, String name) {
		return getBigDecimal(properties, name, null);
	}
	
	
	public static BigDecimal getBigDecimal(Properties properties, String name, BigDecimal defaultValue) {
		return getProperty(properties, name, BigDecimal.class, defaultValue);
	}
	
	
	public static String getString(Properties properties, String name) {
		return getString(properties, name, null);
	}
	
	
	public static String getString(Properties properties, String name, String defaultValue) {
		return getProperty(properties, name, String.class, defaultValue);
	}
	
	
	public static <T> T getProperty(Properties properties, String name, Class<T> type) {
		return getProperty(properties, name, type, null);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Properties properties, String name, Class<T> type, T defaultValue) {
		requireNotNull(type, "type == null");
		T value = defaultValue;
		String property = properties.getProperty(requireNotNull(name, "name == null"));
		if (isNotNull(property)) {
			if (type == String.class) {
				return (T) property;
			}
			TypeConverter<String, T> converter = TypeConverterFactory.getTypeConverter(String.class, type);
			if (isNull(converter)) {
				silentThrex(TypeConvertException::new, "Can't convert %s to %s", property, type);
			}
			try {
				value = converter.convert(property);
			} catch (TypeConvertException e) {
				silentThrex(e, "Convert %s to %s failed", property, type);
			}
		}
		return value;
	}
	
}
