package com.kxindot.goblin.asserts;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 */
class CharSequenceAssertImpl<T extends CharSequence> 
extends AbstractAssert<T, CharSequenceAssert<T>> 
implements CharSequenceAssert<T> {

    CharSequenceAssertImpl(T t) {
        super(t);
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> isEmpty() {
        return predicate(Objects.isEmpty(t));
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> isBlank() {
        return predicate(Objects.isBlank(t));
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> isNotEmpty() {
        return predicate(!Objects.isEmpty(t));
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> isNotBlank() {
        return predicate(Objects.isNotBlank(t));
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> contains(T substring) {
        return predicate(t != null && t.toString().contains(substring));
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> lengthGreatThan(int len) {
        return predicate(t != null && t.length() > len);
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> lengthGreatThanOrEqual(int len) {
        return predicate(t != null && t.length() >= len);
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> lengthLessThan(int len) {
        return predicate(t != null && t.length() < len);
    }

    @Override
    public AssertOption<T, CharSequenceAssert<T>> lengthLessThanOrEqual(int len) {
        return predicate(t != null && t.length() <= len);
    }

    @Override
    CharSequenceAssert<T> self() {
        return this;
    }
}
