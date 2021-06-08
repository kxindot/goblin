package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToLongConverter extends CharSequenceConverter<Long> {

    @Override
    protected Long convertValue(CharSequence p) {
        return Long.valueOf(p.toString());
    }

}
