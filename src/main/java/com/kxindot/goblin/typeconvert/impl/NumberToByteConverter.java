package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class NumberToByteConverter extends NumberConverter<Byte> {

    @Override
    protected Byte convertValue(Number p) {
        return Byte.valueOf(p.byteValue());
    }

}
