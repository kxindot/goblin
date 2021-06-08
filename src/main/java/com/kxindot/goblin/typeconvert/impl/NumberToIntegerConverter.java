package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToIntegerConverter extends NumberConverter<Integer> {

    @Override
    protected Integer convertValue(Number p) {
        return Integer.valueOf(p.intValue());
    }

}
