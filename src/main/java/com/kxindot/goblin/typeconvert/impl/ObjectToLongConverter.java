package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToLongConverter extends ObjectConverter<Long> {

    @Override
    protected Long convertValue(Object p) {
        return Long.valueOf(p.toString());
    }

}
