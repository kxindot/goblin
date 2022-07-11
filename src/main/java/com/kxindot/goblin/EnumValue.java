package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.isEqual;
import static com.kxindot.goblin.Objects.newArrayList;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * 通用枚举值接口定义
 * @author zhaoqingjiang
 */
public interface EnumValue<T> {
    
    /**
     * 根据枚举值获取枚举对象
     * @param <T>
     * @param <E>
     * @param value 枚举值
     * @param type 枚举对象类型
     * @return 枚举对象
     */
    public static <T, E extends Enum<?> & EnumValue<T>> E valueOf(T value, Class<E> type) {
        return Stream.of(type.getEnumConstants())
                .filter(e -> isEqual(value, EnumValue.class.cast(e).value()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 根据枚举值获取枚举对象
     * @param <T>
     * @param <E>
     * @param value 枚举值
     * @param object 枚举对象
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends EnumValue<T>> E valueOf(T value, E object) {
        return (E) object.allValues().stream()
                .filter(e -> isEqual(value, EnumValue.class.cast(e).value()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 返回枚举值
     * @return T
     */
    T value();
    
    /**
     * 返回所有枚举值
     * @param <E>
     * @return {@code Collection<E>}
     */
    @SuppressWarnings("unchecked")
    default <E extends EnumValue<T>> Collection<E> allValues() {
        Class<?> type = getClass();
        if (!type.isEnum()) {
            throw new AbstractMethodError("Method must be override!");
        }
        return Collection.class.cast(newArrayList(type.getEnumConstants()));
    }
}
