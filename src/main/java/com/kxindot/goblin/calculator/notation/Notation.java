package com.kxindot.goblin.calculator.notation;

import com.kxindot.goblin.Objects;

/**
 * 符号抽象类
 * @author ZhaoQingJiang
 */
public abstract class Notation {
    
    private String notation;
    
    public Notation(String notation) {
        this.notation = Objects.requireNotBlank(notation, "notation can't be null or blank");
    }

    public String getNotation() {
        return notation;
    }
    
    @Override
    public int hashCode() {
        return notation.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (null != obj && obj instanceof Notation) {
            return Objects.isEqual(notation, ((Notation) obj).notation);
        }
        return false;
    }

    @Override
    public String toString() {
        return notation;
    }
    
}
