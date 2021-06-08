package com.kxindot.goblin.typeconvert.impl;

import java.math.BigDecimal;

/**
 * @author ZhaoQingJiang
 */
public class ObjectToBigDecimalConverter extends ObjectConverter<BigDecimal> {

    @Override
    protected BigDecimal convertValue(Object p) {
        return new BigDecimal(p.toString());
    }

}
