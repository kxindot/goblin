package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToShortConverter extends NumberConverter<Short> {

    @Override
    protected Short convertValue(Number p) {
        return Short.valueOf(p.shortValue());
    }

}
