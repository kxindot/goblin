package com.kxindot.goblin.typeconvert.impl;

import java.util.Date;

import com.kxindot.goblin.typeconvert.AbstractTypeConverter;

/**
 * @author ZhaoQingJiang
 */
public abstract class DateConverter<R> extends AbstractTypeConverter<Date, R> {

    @Override
    protected boolean convertible(Class<?> type) {
        return Date.class.isAssignableFrom(type);
    }
    
}
