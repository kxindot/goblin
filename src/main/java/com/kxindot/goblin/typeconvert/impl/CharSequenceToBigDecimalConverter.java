package com.kxindot.goblin.typeconvert.impl;

import java.math.BigDecimal;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToBigDecimalConverter extends CharSequenceConverter<BigDecimal> {

    @Override
    protected BigDecimal convertValue(CharSequence p) {
        return new BigDecimal(p.toString());
    }
}
