package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToFloatConverter extends NumberConverter<Float> {

    @Override
    protected Float convertValue(Number p) {
        return Float.valueOf(p.floatValue());
    }

}
