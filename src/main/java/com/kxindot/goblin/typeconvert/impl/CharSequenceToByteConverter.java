package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToByteConverter extends CharSequenceConverter<Byte> {

    @Override
    protected Byte convertValue(CharSequence p) {
        return Byte.valueOf(p.toString());
    }

}
