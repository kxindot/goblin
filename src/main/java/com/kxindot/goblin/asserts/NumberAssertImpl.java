package com.kxindot.goblin.asserts;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 */
class NumberAssertImpl<T extends Number & Comparable<T>> 
extends AbstractAssert<T, NumberAssert<T>>
implements NumberAssert<T> {
    
    NumberAssertImpl(T t) {
        super(t);
    }
    
    @Override
    public AssertOption<T, NumberAssert<T>> isZero() {
        return predicate(t != null && t.doubleValue() == 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isPositive() {
        return predicate(t != null && t.doubleValue() > 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isNegative() {
        return predicate(t != null && t.doubleValue() < 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isEqual(T n) {
        return predicate(Objects.isEqual(t, n));
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isGt(T n) {
        return predicate(t != null && t.compareTo(n) > 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isGte(T n) {
        return predicate(t != null && t.compareTo(n) >= 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isLt(T n) {
        return predicate(t != null && t.compareTo(n) < 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isLte(T n) {
        return predicate(t != null && t.compareTo(n) <= 0);
    }

    @Override
    public AssertOption<T, NumberAssert<T>> isBewteen(T low, T high) {
        return predicate(t != null && t.compareTo(low) >= 0 && t.compareTo(high) <= 0);
    }

    @Override
    NumberAssert<T> self() {
        return this;
    }

}
