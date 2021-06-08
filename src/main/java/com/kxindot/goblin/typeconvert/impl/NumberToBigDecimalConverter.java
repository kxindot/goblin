package com.kxindot.goblin.typeconvert.impl;

import java.math.BigDecimal;

/**
 * @author ZhaoQingJiang
 */
public class NumberToBigDecimalConverter extends NumberConverter<BigDecimal> {

    @Override
    protected BigDecimal convertValue(Number p) {
        return BigDecimal.valueOf(p.doubleValue());
    }

}
