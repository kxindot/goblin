package com.kxindot.goblin.database.sql.condition;

import static com.kxindot.goblin.Objects.isEqual;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.database.sql.condition.Operator.EQ;
import static com.kxindot.goblin.database.sql.condition.Operator.NE;
import static java.lang.String.format;

/**
 * @author zhaoqingjiang
 */
public class Condition {

    private String column;
    private Operator operator;
    private Object value;
    
    protected Condition(String column) {
        this.column = requireNotBlank(column, "column can't be null or blank");
    }

    void setValue(Operator operator, Object value) {
        if (operator != EQ && operator != NE && value == null) {
            throw new IllegalArgumentException("value can't be null when operator is " + operator.value());
        }
        this.operator = operator;
        this.value = value;
    }

    public boolean isNullValue() {
        return value == null;
    }
    
    public boolean isNotNullValue() {
        return !isNullValue();
    }
    
    public String statement() {
        String stat = null;
        if (isNullValue()) {
            if (operator == EQ) {
                stat = format("%s is null", column);
            } else if (operator == NE) {
                stat = format("%s is not null", column);
            }
        }
        return stat != null ? stat : format("%s %s ?", column, operator.value());
    }
    
    public Object value() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null 
                || !Condition.class.isInstance(obj)) {
            return false;
        }
        Condition c = Condition.class.cast(obj);
        return isEqual(column, c.column) 
                && isEqual(operator, c.operator) 
                && isEqual(value, c.value);
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return format("%s %s %s", column, 
                operator.value(), String.valueOf(value));
    }
}