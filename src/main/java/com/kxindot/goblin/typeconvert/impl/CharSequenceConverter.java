package com.kxindot.goblin.typeconvert.impl;

import com.kxindot.goblin.typeconvert.AbstractTypeConverter;

/**
 * @author ZhaoQingJiang
 */
public abstract class CharSequenceConverter<R> extends AbstractTypeConverter<CharSequence, R> {

    @Override
    protected boolean convertible(Class<?> type) {
        return CharSequence.class.isAssignableFrom(type);
    }
}
