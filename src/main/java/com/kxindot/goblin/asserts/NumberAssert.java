package com.kxindot.goblin.asserts;

/**
 * @author ZhaoQingJiang
 */
public interface NumberAssert<T extends Number & Comparable<T>> extends Assert<T, NumberAssert<T>> {

    AssertOption<T, NumberAssert<T>> isZero();
    
    AssertOption<T, NumberAssert<T>> isPositive();
    
    AssertOption<T, NumberAssert<T>> isNegative();
    
    AssertOption<T, NumberAssert<T>> isEqual(T n);
    
    AssertOption<T, NumberAssert<T>> isGt(T n);
    
    AssertOption<T, NumberAssert<T>> isGte(T n);
    
    AssertOption<T, NumberAssert<T>> isLt(T n);
    
    AssertOption<T, NumberAssert<T>> isLte(T n);
    
    AssertOption<T, NumberAssert<T>> isBewteen(T low, T high);
    
}
