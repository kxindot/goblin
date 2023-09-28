package com.kxindot.goblin.resource.property.resolver;

import java.math.BigDecimal;

/**
 * @author ZhaoQingJiang
 */
public class BigDecimalPropertyResolver extends AbstractPropertyResolver<BigDecimal> {

    public BigDecimalPropertyResolver() {
    	this(null);
    }

    public BigDecimalPropertyResolver(String name) {
    	super(name, BigDecimal.class);
    }
    
}
