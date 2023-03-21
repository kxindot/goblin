package com.kxindot.goblin.asserts;

import java.util.Map;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 */
class MapAssertImpl<K, V, T extends Map<K, V>> 
extends AbstractAssert<T, MapAssert<K, V, T>> 
implements MapAssert<K, V, T> {

    MapAssertImpl(T t) {
        super(t);
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> isEmpty() {
        return predicate(Objects.isEmpty(t));
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> isNotEmpty() {
        return predicate(Objects.isNotEmpty(t));
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> containKey(K key) {
        return predicate(t != null && t.containsKey(key));
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> containValue(V value) {
        return predicate(t != null && t.containsValue(value));
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> sizeGreatThan(int size) {
        return predicate(t != null && t.size() > size);
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> sizeGreatThanOrEqual(int size) {
        return predicate(t != null && t.size() >= size);
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> sizeLessThan(int size) {
        return predicate(t != null && t.size() < size);
    }

    @Override
    public AssertOption<T, MapAssert<K, V, T>> sizeLessThanOrEqual(int size) {
        return predicate(t != null && t.size() <= size);
    }

    @Override
    MapAssert<K, V, T> self() {
        return this;
    }

}
