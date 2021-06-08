package com.kxindot.goblin.typeconvert.impl;

import com.kxindot.goblin.typeconvert.AbstractTypeConverter;

/**
 * @author ZhaoQingJiang
 */
public abstract class NumberConverter<R> extends AbstractTypeConverter<Number, R> {

    @Override
    protected boolean convertible(Class<?> type) {
        return Number.class.isAssignableFrom(type);
    }
    
}
