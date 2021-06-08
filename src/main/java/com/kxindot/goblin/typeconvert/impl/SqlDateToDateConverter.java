package com.kxindot.goblin.typeconvert.impl;

import java.sql.Date;

import com.kxindot.goblin.typeconvert.AbstractTypeConverter;

/**
 * @author ZhaoQingJiang
 */
public class SqlDateToDateConverter extends AbstractTypeConverter<Date, java.util.Date> {

    @Override
    protected boolean convertible(Class<?> type) {
        return Date.class.isAssignableFrom(type);
    }

    @Override
    protected java.util.Date convertValue(Date p) {
        return (java.util.Date) p;
    }

}
