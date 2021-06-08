package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToDoubleConverter extends NumberConverter<Double> {

    @Override
    protected Double convertValue(Number p) {
        return Double.valueOf(p.doubleValue());
    }

}
