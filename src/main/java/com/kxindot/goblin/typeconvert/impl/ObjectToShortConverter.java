package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToShortConverter extends ObjectConverter<Short> {

    @Override
    protected Short convertValue(Object p) {
        return Short.valueOf(p.toString());
    }

}
