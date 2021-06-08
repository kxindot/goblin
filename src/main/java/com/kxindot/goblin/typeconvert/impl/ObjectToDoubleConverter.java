package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToDoubleConverter extends ObjectConverter<Double> {

    @Override
    protected Double convertValue(Object p) {
        return Double.valueOf(p.toString());
    }

}
