package com.kxindot.goblin.resource.property;

import static com.kxindot.goblin.Classes.isAssignableFrom;
import static com.kxindot.goblin.Classes.isPrimitiveOrWrapper;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Throws.silentThrex;
import static com.kxindot.goblin.Throws.threx;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
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
	 * 从输入流中加载配置.返回{@link Properties}对象.
	 * @param in InputStream
	 * @return {@link Properties}
	 */
    public static Properties load(InputStream in) {
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            silentThrex(e, "加载数据流异常!");
        }
        return properties;
    }
    
	/**
	 * 从字符输入流中加载配置.返回{@link Properties}对象.
	 * @param reader Reader
	 * @return {@link Properties}
	 */
    public static Properties load(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            silentThrex(e, "加载字符数据流异常!");
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
    
    /**
     * 解析JavaBean信息,并将配置文件对应信息映射到JavaBean中并返回.
     * @param <T>
     * @param bean {@code Class<T>}
     * @param in InputStream
     * @return T 
     */
    public static <T> T getJavaBean(Class<T> bean, InputStream in) {
        return getJavaBean(bean, load(in));
    }

    /**
     * 解析JavaBean信息,并将配置文件对应信息映射到JavaBean中并返回.
     * @param <T>
     * @param bean {@code Class<T>}
     * @param properties Properties
     * @return T 
     */
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
	private static <T> PropertyResolver<T> getResolver(Class<T> type) {
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
