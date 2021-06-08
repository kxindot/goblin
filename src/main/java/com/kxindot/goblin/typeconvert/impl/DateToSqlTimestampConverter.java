package com.kxindot.goblin.typeconvert.impl;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
public class DateToSqlTimestampConverter extends DateConverter<Timestamp> {

    @Override
    protected Timestamp convertValue(Date p) {
        return new Timestamp(p.getTime());
    }

}
