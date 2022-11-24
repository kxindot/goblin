package com.kxindot.goblin.bean;

import static com.kxindot.goblin.Objects.stringFormat;

import java.io.Serializable;

/**
 * @author ZhaoQingJiang
 */
public class Bean<T> implements Serializable, Comparable<Bean<T>> {

    private static final long serialVersionUID = 6612161448676927687L;

    private Class<T> type;
    
    private BeanProperty[] properties;
    
    
    public Bean(Class<T> type) {
        this.type = type;
    }


    public T copyFrom(Object source) {
        
        return null;
    }
    

    @Override
    public int compareTo(Bean<T> o) {
        return type.equals(o.type) ? 0 : type.getName().compareTo(o.type.getName());
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && obj instanceof Bean) {
            return type.equals(Bean.class.cast(obj).type);
        }
        return false;
    }

    @Override
    public String toString() {
        return stringFormat("%s[%s]", getClass().getSimpleName(), type.getName());
    }
}
