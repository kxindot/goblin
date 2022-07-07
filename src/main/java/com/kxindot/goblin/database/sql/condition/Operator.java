package com.kxindot.goblin.database.sql.condition;

import com.kxindot.goblin.EnumValue;

/**
 * SQL-Where条件操作(比较)符号枚举
 * @author zhaoqingjiang
 */
public enum Operator implements EnumValue<String> {
    
    EQ("="), NE("!="),
    GT(">"), LT("<"),
    GTE(">="), LTE("<="),
    LIKE("like");
    
    private String value;

    private Operator(String value) {
        this.value = value;
    }
    
    @Override
    public String value() {
        return value;
    }
}