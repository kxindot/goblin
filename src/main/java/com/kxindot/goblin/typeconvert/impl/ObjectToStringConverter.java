package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToStringConverter extends ObjectConverter<String> {

    @Override
    protected String convertValue(Object p) {
        return p.toString();
    }

}
