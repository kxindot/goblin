package com.kxindot.goblin.typeconvert.impl;

import com.kxindot.goblin.typeconvert.AbstractTypeConverter;

/**
 * @author ZhaoQingJiang
 */
public abstract class ObjectConverter<R> extends AbstractTypeConverter<Object, R> {

    @Override
    protected boolean convertible(Class<?> type) {
        return type == Object.class;
    }
    
}
