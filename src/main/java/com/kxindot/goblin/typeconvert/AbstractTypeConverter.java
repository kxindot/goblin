package com.kxindot.goblin.typeconvert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ZhaoQingJiang
 */
public abstract class AbstractTypeConverter<P, R> implements TypeConverter<P, R> {

    private Class<P> source;
    private Class<R> target;
    
    @SuppressWarnings("unchecked")
    public AbstractTypeConverter() {
        Class<?> cls = getClass();
        Type[] types = ((ParameterizedType)cls 
                .getGenericSuperclass()).getActualTypeArguments();
        if (types.length == 1) {
            this.target = (Class<R>) types[0];
            types = ((ParameterizedType)cls.getSuperclass()
                    .getGenericSuperclass()).getActualTypeArguments();
            this.source = (Class<P>) types[0];
        } else {
            this.source = (Class<P>) types[0];
            this.target = (Class<R>) types[1];
        }
        
    }
    
    public Class<P> convertSourceType() {
        return source;
    }
    
    public Class<?> convertTargetType() {
        return target;
    }
    
    @Override
    public boolean convertable(Object object) {
        return object == null ? true 
                : convertable(object.getClass());
    }
    
    @Override
    public boolean convertable(Class<?> type) {
        return type == null ? false : convertible(type);
    }
    
    @Override
    public R convert(P p) throws TypeConvertException {
        try {
            return p == null ? null : convertValue(p);
        } catch (Throwable e) {
            throw new TypeConvertException(this, p, target, e);
        }
    }    
    
    protected abstract boolean convertible(Class<?> type);
    
    protected abstract R convertValue(P p);
    
    @Override
    public String toString() {
        return "TypeConverter[source=" 
                + source.getName() + ", target=" + target.getName() + "]";
    }   
}
