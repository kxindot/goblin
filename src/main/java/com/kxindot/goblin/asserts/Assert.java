package com.kxindot.goblin.asserts;

import java.util.function.Predicate;

/**
 * @author ZhaoQingJiang
 */
public interface Assert<T, A extends Assert<T, A>> {

    AssertOption<T, A> isNull();

    AssertOption<T, A> isNotNull();
    
    AssertOption<T, A> isTrue(Predicate<T> predicate);
    
    AssertOption<T, A> isInstanceOf(Class<?> type);
}
