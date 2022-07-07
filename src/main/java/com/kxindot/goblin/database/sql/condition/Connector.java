package com.kxindot.goblin.database.sql.condition;

import com.kxindot.goblin.EnumValue;

/**
 * @author zhaoqingjiang
 */
public enum Connector implements EnumValue<String> {

    And("and"),
    
    Or("or");
    
    private String value;

    private Connector(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
