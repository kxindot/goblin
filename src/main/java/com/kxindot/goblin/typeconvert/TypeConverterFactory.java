package com.kxindot.goblin.typeconvert;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TypeConverterFactory {
    
    private static final Map<Class<?>, List<AbstractTypeConverter>> converterMap = new ConcurrentHashMap<>();
    static {ServiceLoader.load(AbstractTypeConverter.class).forEach(c -> register(c));}
    
    /**
     * 根据转换目标类型查询所有可用的类型转换器
     * @param target {@code Class<?>} - 转换目标类型
     * @return TypeConverter[] - 若无可用类型转换器,则返回null
     */
    public static TypeConverter[] getTypeConverter(Class<?> target) {
        target = Objects.primitiveToWrapper(target);
        List<AbstractTypeConverter> cs = converterMap.get(target);
        if (cs == null) return null;
        return cs.toArray(new TypeConverter[cs.size()]);
    }
    
    /**
     * 根据来源及目标类型查询可用类型转换器
     * @param source {@code Class<?>} 来源类型
     * @param target {@code Class<?>} 目标类型
     * @return {@code TypeConverter<S, T>} - 若无可用类型转换器,则返回null
     */
    public static <S, T> TypeConverter<S, T> getTypeConverter(Class<S> source, Class<T> target) {
        if (source == null || target == null) return null;
        target = (Class<T>) Objects.primitiveToWrapper(target);
        List<AbstractTypeConverter> cs = converterMap.get(target);
        if (cs == null) return null;
        return cs.stream().filter(e -> e.convertable(source)).findFirst().orElse(null);
    }
    
    /**
     * 注册类型转换器
     * @param converter - 类型转换器
     * @throws NullPointerException 当converter==null时,抛出该异常
     */
    public static <C extends AbstractTypeConverter> void register(C converter) {
        Class type = converter.convertTargetType();
        List<AbstractTypeConverter> cs = converterMap.get(type);
        if (cs == null) {
            cs = new CopyOnWriteArrayList<>();
            converterMap.put(type, cs);
        }
        if (!cs.stream().filter(e -> converter.convertSourceType() 
                == e.convertSourceType()).findFirst().isPresent()) {
            cs.add(converter);
        }
    }
}
