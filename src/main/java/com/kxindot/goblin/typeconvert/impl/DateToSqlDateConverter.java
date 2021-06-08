package com.kxindot.goblin.typeconvert.impl;

import java.sql.Date;

/**
 * @author ZhaoQingJiang
 */
public class DateToSqlDateConverter extends DateConverter<Date> {

    @Override
    protected Date convertValue(java.util.Date p) {
        return new Date(p.getTime());
    }

}
