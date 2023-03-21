package com.kxindot.goblin.asserts;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 */
class CollectionAssertImpl<E, T extends Collection<E>> 
extends AbstractAssert<T, CollectionAssert<E, T>> 
implements CollectionAssert<E, T> {

    CollectionAssertImpl(T t) {
        super(t);
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> isEmpty() {
        return predicate(Objects.isEmpty(t));
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> isNotEmpty() {
        return predicate(Objects.isNotEmpty(t));
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> isList() {
        return predicate(List.class.isInstance(t));
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> isSet() {
        return predicate(Set.class.isInstance(t));
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> contains(E element) {
        return predicate(t != null && t.contains(element));
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> sizeGreatThan(int size) {
        return predicate(t != null && t.size() > size);
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> sizeGreatThanOrEqual(int size) {
        return predicate(t != null && t.size() >= size);
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> sizeLessThan(int size) {
        return predicate(t != null && t.size() < size);
    }

    @Override
    public AssertOption<T, CollectionAssert<E, T>> sizeLessThanOrEqual(int size) {
        return predicate(t != null && t.size() <= size);
    }

    @Override
    CollectionAssert<E, T> self() {
        return this;
    }

}
