package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToStringConverter extends NumberConverter<String> {

    @Override
    protected String convertValue(Number p) {
        return p.toString();
    }

}
