package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToStringConverter extends CharSequenceConverter<String> {

    @Override
    protected String convertValue(CharSequence p) {
        return p.toString();
    }

}
