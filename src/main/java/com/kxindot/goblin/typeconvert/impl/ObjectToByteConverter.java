package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToByteConverter extends ObjectConverter<Byte> {

    @Override
    protected Byte convertValue(Object p) {
        return Byte.valueOf(p.toString());
    }

}
