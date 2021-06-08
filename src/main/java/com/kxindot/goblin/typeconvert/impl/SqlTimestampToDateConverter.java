package com.kxindot.goblin.typeconvert.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.kxindot.goblin.typeconvert.AbstractTypeConverter;

/**
 * @author ZhaoQingJiang
 */
public class SqlTimestampToDateConverter extends AbstractTypeConverter<Timestamp, Date> {

    @Override
    protected boolean convertible(Class<?> type) {
        return Timestamp.class.isAssignableFrom(type);
    }

    @Override
    protected Date convertValue(Timestamp p) {
        return (Date) p;
    }

}
