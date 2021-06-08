package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToIntegerConverter extends ObjectConverter<Integer> {

    @Override
    protected Integer convertValue(Object p) {
        return Integer.valueOf(p.toString());
    }

}
