package com.kxindot.goblin.source;

import java.lang.reflect.Modifier;

import com.kxindot.goblin.EnumValue;

/**
 * Java修饰符枚举
 * 
 * @author ZhaoQingJiang
 */
public enum JavaModifier implements EnumValue<Integer> {
    
    Public(Modifier.PUBLIC),
    
    Private(Modifier.PRIVATE),
    
    Protected(Modifier.PROTECTED),
    
    Static(Modifier.STATIC),
    
    Final(Modifier.FINAL),
    
    Synchronized(Modifier.SYNCHRONIZED),
    
    Volatile(Modifier.VOLATILE),
    
    Transient(Modifier.TRANSIENT),
    
    Native(Modifier.NATIVE),
    
    Interface(Modifier.INTERFACE),
    
    Abstract(Modifier.ABSTRACT),
    
    Strict(Modifier.STRICT),
    
    ;
    
    private Integer value;
    
    private JavaModifier(Integer value) {
        this.value = value;
    }
    
    @Override
    public Integer value() {
        return value;
    }
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
