package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToLongConverter extends NumberConverter<Long> {

    @Override
    protected Long convertValue(Number p) {
        return Long.valueOf(p.longValue());
    }

}
