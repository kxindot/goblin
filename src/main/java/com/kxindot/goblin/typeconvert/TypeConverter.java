package com.kxindot.goblin.typeconvert;

/**
 * @author ZhaoQingJiang
 */
public interface TypeConverter<P, R> {
    
    boolean convertable(Object object);
    
    boolean convertable(Class<?> type);

    R convert(P p) throws TypeConvertException;
}
