package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToShortConverter extends CharSequenceConverter<Short> {

    @Override
    protected Short convertValue(CharSequence p) {
        return Short.valueOf(p.toString());
    }

}
