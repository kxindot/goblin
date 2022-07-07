package com.kxindot.goblin.database.sql.condition;

/**
 * @author zhaoqingjiang
 */
public interface Operate {

    Where operate(String operator, Object value);
    
    Where eq(Object value);
    
    Where ne(Object value);
    
    Where gt(Object value);
    
    Where gte(Object value);
    
    Where lt(Object value);
    
    Where lte(Object value);
    
    Where like(String value);
}
