package com.kxindot.goblin.calculator.notation;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.function.BinaryOperator;

import com.kxindot.goblin.Objects;

/**
 * 运算符
 * @author ZhaoQingJiang
 */
public abstract class Operator<T> extends Notation {

    private BinaryOperator<Object> operator;
    
    public Operator(String notation, BinaryOperator<Object> operator) {
        super(notation);
        this.operator = Objects.requireNotNull(operator, "operator can't be null");
    }
    
    public Object operate(Object a, Object b) {
        return null;
    }
}
