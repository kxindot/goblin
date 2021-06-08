package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToIntegerConverter extends CharSequenceConverter<Integer> {

    @Override
    protected Integer convertValue(CharSequence p) {
        return Integer.valueOf(p.toString());
    }

}
