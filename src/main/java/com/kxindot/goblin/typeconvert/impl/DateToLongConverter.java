package com.kxindot.goblin.typeconvert.impl;

import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
public class DateToLongConverter extends DateConverter<Long> {

    @Override
    protected Long convertValue(Date p) {
        return p.getTime();
    }

}
