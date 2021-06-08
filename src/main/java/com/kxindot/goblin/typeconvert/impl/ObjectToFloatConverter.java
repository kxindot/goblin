package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToFloatConverter extends ObjectConverter<Float> {

    @Override
    protected Float convertValue(Object p) {
        return Float.valueOf(p.toString());
    }

}
