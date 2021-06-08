package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToDoubleConverter extends CharSequenceConverter<Double> {

    @Override
    protected Double convertValue(CharSequence p) {
        return Double.valueOf(p.toString());
    }

}
