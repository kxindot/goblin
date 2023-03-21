package com.kxindot.goblin.asserts;

import java.util.Map;

/**
 * @author ZhaoQingJiang
 */
public interface MapAssert<K, V, T extends Map<K, V>> extends Assert<T, MapAssert<K, V, T>> {

    AssertOption<T, MapAssert<K, V, T>> isEmpty();
    
    AssertOption<T, MapAssert<K, V, T>> isNotEmpty();
    
    AssertOption<T, MapAssert<K, V, T>> containKey(K key);
    
    AssertOption<T, MapAssert<K, V, T>> containValue(V value);
    
    AssertOption<T, MapAssert<K, V, T>> sizeGreatThan(int size);
    
    AssertOption<T, MapAssert<K, V, T>> sizeGreatThanOrEqual(int size);
    
    AssertOption<T, MapAssert<K, V, T>> sizeLessThan(int size);
    
    AssertOption<T, MapAssert<K, V, T>> sizeLessThanOrEqual(int size);
    
}
