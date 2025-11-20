package com.kxindot.goblin.resource.property;

import static com.kxindot.goblin.Classes.getAvailableClassLoader;
import static com.kxindot.goblin.Classes.isAssignableFrom;
import static com.kxindot.goblin.Classes.isPrimitiveOrWrapper;
import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Throws.silentThrex;
import static com.kxindot.goblin.Throws.threx;
import static com.kxindot.goblin.io.IO.openInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import com.kxindot.goblin.resource.property.resolver.BigDecimalPropertyResolver;
import com.kxindot.goblin.resource.property.resolver.JavaBeanPropertyResolver;
import com.kxindot.goblin.resource.property.resolver.PrimitivePropertyResolver;
import com.kxindot.goblin.resource.property.resolver.PropertyResolveException;
import com.kxindot.goblin.resource.property.resolver.PropertyResolver;
import com.kxindot.goblin.resource.property.resolver.StringPropertyResolver;

/**
 * 配置工具类.
 * 
 * @author ZhaoQingJiang
 */
public class PropertyUtil {
	
	/**
	 * 从文件中加载配置.返回{@link Properties}对象.
	 * @param file File
	 * @return {@link Properties}
	 */
	public static Properties loadProperties(File file) {
		return loadProperties(openInputStream(file));
	}
	
	/**
	 * 从文件中加载配置.返回{@link Properties}对象.
	 * @param file Path
	 * @return {@link Properties}
	 */
	public static Properties loadProperties(Path file) {
		return loadProperties(openInputStream(file));
	}
	
	/**
	 * 从输入流中加载配置.返回{@link Properties}对象.
	 * @param in InputStream
	 * @return {@link Properties}
	 */
    public static Properties loadProperties(InputStream in) {
        return loadProperties(in, false);
    }
    
    /**
     * 从输入流中加载配置.返回{@link Properties}对象.
     * @param in InputStream
     * @param ordered 按顺序加载
     * @return {@link Properties}
     */
    public static Properties loadProperties(InputStream in, boolean ordered) {
    	Properties properties = ordered ? new OrderedProperties() : new Properties();
    	try {
    		properties.load(in);
    	} catch (IOException e) {
    		silentThrex(e, "加载配置：读取字节输入流异常");
    	}
    	return properties;
    }
    
	/**
	 * 从字符输入流中加载配置.返回{@link Properties}对象.
	 * @param reader Reader
	 * @return {@link Properties}
	 */
    public static Properties loadProperties(Reader reader) {
        return loadProperties(reader, false);
    }
    
    /**
     * 从字符输入流中加载配置.返回{@link Properties}对象.
     * @param reader Reader
     * @param ordered 按顺序加载
     * @return {@link Properties}
     */
    public static Properties loadProperties(Reader reader, boolean ordered) {
    	Properties properties = ordered ? new OrderedProperties() : new Properties();
    	try {
    		properties.load(reader);
    	} catch (IOException e) {
    		silentThrex(e, "加载配置：读取字符输入流异常");
    	}
    	return properties;
    }

    /**
     * 获取short类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return Short
     */
    public static Short getShort(String name, Properties properties) {
    	return getShort(name, properties, null);
    }

    /**
     * 获取short类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return Short
     */
    public static Short getShort(String name, Properties properties, Short defaultValue) {
        return getProperty(name, properties, Short.class, defaultValue);
    }

    /**
     * 获取int类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return Integer
     */
    public static Integer getInteger(String name, Properties properties) {
        return getInteger(name, properties, null);
    }

    /**
     * 获取int类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return Integer
     */
    public static Integer getInteger(String name, Properties properties, Integer defaultValue) {
        return getProperty(name, properties, Integer.class, defaultValue);
    }

    /**
     * 获取long类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return Long
     */
    public static Long getLong(String name, Properties properties) {
        return getLong(name, properties, null);
    }

    /**
     * 获取long类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return Long
     */
    public static Long getLong(String name, Properties properties, Long defaultValue) {
        return getProperty(name, properties, Long.class, defaultValue);
    }

    /**
     * 获取float类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return Float
     */
    public static Float getFloat(String name, Properties properties) {
        return getFloat(name, properties, null);
    }

    /**
     * 获取float类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return Float
     */
    public static Float getFloat(String name, Properties properties, Float defaultValue) {
        return getProperty(name, properties, Float.class, defaultValue);
    }

    /**
     * 获取double类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return Double
     */
    public static Double getDouble(String name, Properties properties) {
        return getDouble(name, properties, null);
    }

    /**
     * 获取double类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return Double
     */
    public static Double getDouble(String name, Properties properties, Double defaultValue) {
        return getProperty(name, properties, Double.class, defaultValue);
    }

    /**
     * 获取BigDecimal类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return BigDecimal
     */
    public static BigDecimal getBigDecimal(String name, Properties properties) {
        return getBigDecimal(name, properties, null);
    }

    /**
     * 获取BigDecimal类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return BigDecimal
     */
    public static BigDecimal getBigDecimal(String name, Properties properties, BigDecimal defaultValue) {
        return getProperty(name, properties, BigDecimal.class, defaultValue);
    }

    /**
     * 获取String类型的配置值.若无此配置则返回null.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @return String
     */
    public static String getString(String name, Properties properties) {
        return getString(name, properties, null);
    }

    /**
     * 获取String类型的配置值.若无此配置或配置值等于null,则返回默认值.
     * @param name 配置名称
     * @param properties {@link Properties}
     * @param defaultValue 默认值
     * @return String
     */
    public static String getString(String name, Properties properties, String defaultValue) {
        return getProperty(name, properties, String.class, defaultValue);
    }
    
    
    public static <T> T getProperties(Class<T> beanType) {
    	return getProperties(beanType, getAvailableClassLoader());
    }
    
    
    public static <T> T getProperties(Class<T> beanType, String resourceName) {
    	return getProperties(beanType, null, resourceName);
    }
    
    
    public static <T> T getProperties(Class<T> beanType, ClassLoader classLoader) {
    	com.kxindot.goblin.resource.property.Properties annotation = 
    			beanType.getAnnotation(com.kxindot.goblin.resource.property.Properties.class);
    	if (isNull(annotation)) {
			threx(IllegalArgumentException::new, "类需标注@Properties注解: %s", beanType.getName());
		}
    	String name = annotation.resource();
    	if (isBlank(name)) {
			threx(IllegalArgumentException::new, "请使用@Properties#file字段指定资源文件名称: ", beanType.getName());
		}
    	return getProperties(beanType, classLoader, name);
    }
    
    
    public static <T> T getProperties(Class<T> beanType, ClassLoader classLoader, String resourceName) {
    	beanType.getName();
    	requireNotBlank(resourceName, "资源文件名称不能等于null或空字符串");
    	if (isNull(classLoader)) {
			classLoader = getAvailableClassLoader();
		}
    	InputStream inputStream = classLoader.getResourceAsStream(resourceName);
    	return isNull(inputStream) ? null : getProperties(beanType, inputStream);
    }
    
    
    public static <T> T getProperties(Class<T> beanType, File file) {
    	return getProperties(beanType, file.toPath());
    }
    
    
    public static <T> T getProperties(Class<T> beanType, Path file) {
    	return getProperties(beanType, openInputStream(file));
    }
    
    
    public static <T> T getProperties(Class<T> beanType, InputStream inputStream) {
    	return getProperties(beanType, loadProperties(inputStream));
    }
    
    
    public static <T> T getProperties(Class<T> beanType, Reader reader) {
    	return getProperties(beanType, loadProperties(reader));
    }
    
    
    @SuppressWarnings("unchecked")
	public static <T> T getProperties(Class<T> beanType, Properties properties) {
    	com.kxindot.goblin.resource.property.Properties annotation = 
    			beanType.getAnnotation(com.kxindot.goblin.resource.property.Properties.class);
    	final String prefix = isNull(annotation) ? EMP : annotation.prefix();
    	final boolean ignoreNotFound = isNull(annotation) ? true : annotation.ignoreNotFound();
    	JavaBeanPropertyResolver resolver = new JavaBeanPropertyResolver(prefix, beanType, ignoreNotFound);
        return (T) resolver.resolve(properties);
    }
    
    
    /**
     * 解析JavaBean信息,并将配置文件对应信息映射到JavaBean中并返回.
     * 
     * @param <T>
     * @param bean {@code Class<T>}
     * @param in InputStream
     * @return T 
     * @deprecated 1.1.9版本废弃，建议使用{@link #getProperties(Class, InputStream)}
     */
    @Deprecated
    public static <T> T getJavaBean(Class<T> bean, InputStream in) {
        return getJavaBean(bean, loadProperties(in));
    }

    /**
     * 解析JavaBean信息,并将配置文件对应信息映射到JavaBean中并返回.
     * 
     * @param <T>
     * @param bean {@code Class<T>}
     * @param properties Properties
     * @return T 
     * @deprecated 1.1.9版本废弃，建议使用{@link #getProperties(Class, Properties)}
     */
    @Deprecated
    @SuppressWarnings("unchecked")
	public static <T> T getJavaBean(Class<T> bean, Properties properties) {
    	com.kxindot.goblin.resource.property.Properties annotation = 
    			bean.getAnnotation(com.kxindot.goblin.resource.property.Properties.class);
    	if (isNull(annotation)) {
			threx(PropertyResolveException::new, "不合法的类型: %s, 类上需标注@Properties注解!", bean.getName());
		}
    	final String prefix = annotation.prefix();
    	final boolean ignoreNotFound = annotation.ignoreNotFound();
    	JavaBeanPropertyResolver resolver = new JavaBeanPropertyResolver(prefix, bean, ignoreNotFound);
        return (T) resolver.resolve(properties);
    }
    
    /**
     * 获取配置值并将其映射为对应的类型.
     */
    private static <T> T getProperty(String name, Properties properties, Class<T> type, T defaultValue) {
    	return getResolver(type).resolve(properties.getProperty(name), defaultValue);
    }

    /**
     * 解析器缓存
     */
	private static Map<Class<?>, PropertyResolver<?>> resolvers;
	
	/**
	 * 获取解析器
	 */
	@SuppressWarnings("unchecked")
	public static <T> PropertyResolver<T> getResolver(Class<T> type) {
		if (resolvers == null) {
			synchronized (PropertyUtil.class) {
				if (null == resolvers) {
					resolvers = newConcurrentHashMap();
				}
			}
		}
		PropertyResolver<?> resolver = resolvers.get(type);
		if (isNull(resolver)) {
			if (String.class.equals(type) || isAssignableFrom(type, String.class)) {
				resolver = new StringPropertyResolver();
			} else if (isPrimitiveOrWrapper(type)) {
				resolver = new PrimitivePropertyResolver<>(type);
			} else if (BigDecimal.class.equals(type) || isAssignableFrom(type, BigDecimal.class)) {
				resolver = new BigDecimalPropertyResolver();
			} else {
				threx(AbstractMethodError::new);
			}
			resolvers.put(type, resolver);
		}
		return (PropertyResolver<T>) resolver;
	}
	
}
