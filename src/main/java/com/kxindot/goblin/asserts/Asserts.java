package com.kxindot.goblin.asserts;

import java.util.Collection;
import java.util.Map;

/**
 * @author ZhaoQingJiang
 */
public interface Asserts {

    
    public static <T extends CharSequence> CharSequenceAssert<T> of(T cs) {
        return new CharSequenceAssertImpl<>(cs);
    }
    
    
    public static <T extends Number & Comparable<T>> NumberAssert<T> of(T number) {
        return new NumberAssertImpl<>(number);
    }
    
    
    public static <E, T extends Collection<E>> CollectionAssert<E, T> of(T collection) {
        return new CollectionAssertImpl<>(collection);
    }
    
    
    public static <K, V, T extends Map<K, V>> MapAssert<K, V, T> of(T map) {
        return new MapAssertImpl<>(map);
    }
}
