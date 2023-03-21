package com.kxindot.goblin.asserts;

/**
 * @author ZhaoQingJiang
 */
public interface CharSequenceAssert<T extends CharSequence> extends Assert<T, CharSequenceAssert<T>> {

    AssertOption<T, CharSequenceAssert<T>> isEmpty();
    
    AssertOption<T, CharSequenceAssert<T>> isBlank();
    
    AssertOption<T, CharSequenceAssert<T>> isNotEmpty();
    
    AssertOption<T, CharSequenceAssert<T>> isNotBlank();
    
    AssertOption<T, CharSequenceAssert<T>> contains(T substring);
    
    AssertOption<T, CharSequenceAssert<T>> lengthGreatThan(int len);
    
    AssertOption<T, CharSequenceAssert<T>> lengthGreatThanOrEqual(int len);
    
    AssertOption<T, CharSequenceAssert<T>> lengthLessThan(int len);
    
    AssertOption<T, CharSequenceAssert<T>> lengthLessThanOrEqual(int len);
    
}
