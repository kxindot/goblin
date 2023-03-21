package com.kxindot.goblin.asserts;

import java.util.Collection;

/**
 * @author ZhaoQingJiang
 */
public interface CollectionAssert<E, T extends Collection<E>> extends Assert<T, CollectionAssert<E, T>> {

    AssertOption<T, CollectionAssert<E, T>> isEmpty();
    
    AssertOption<T, CollectionAssert<E, T>> isNotEmpty();
    
    AssertOption<T, CollectionAssert<E, T>> isList();
    
    AssertOption<T, CollectionAssert<E, T>> isSet();
    
    AssertOption<T, CollectionAssert<E, T>> contains(E element);
    
    AssertOption<T, CollectionAssert<E, T>> sizeGreatThan(int size);
    
    AssertOption<T, CollectionAssert<E, T>> sizeGreatThanOrEqual(int size);
    
    AssertOption<T, CollectionAssert<E, T>> sizeLessThan(int size);
    
    AssertOption<T, CollectionAssert<E, T>> sizeLessThanOrEqual(int size);
    
}
