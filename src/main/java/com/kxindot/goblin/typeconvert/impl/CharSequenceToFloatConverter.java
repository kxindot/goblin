package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToFloatConverter extends CharSequenceConverter<Float> {

    @Override
    protected Float convertValue(CharSequence p) {
        return Float.valueOf(p.toString());
    }

}
