package com.kxindot.goblin.asserts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
public interface DateAssert<T extends Date> extends Assert<T, DateAssert<T>> {

    AssertOption<T, DateAssert<T>> isEqual(T date);
    
    AssertOption<T, DateAssert<T>> isEqual(LocalDateTime dateTime);
    
    AssertOption<T, DateAssert<T>> isEqual(LocalDate date);

    AssertOption<T, DateAssert<T>> isEqual(LocalTime time);
    
    AssertOption<T, DateAssert<T>> isBefore(T date);
    
    AssertOption<T, DateAssert<T>> isBefore(LocalDateTime dateTime);
    
    AssertOption<T, DateAssert<T>> isBefore(LocalDate date);
    
    AssertOption<T, DateAssert<T>> isBefore(LocalTime time);
    
    AssertOption<T, DateAssert<T>> isAfter(T date);
    
    AssertOption<T, DateAssert<T>> isAfter(LocalDateTime dateTime);
    
    AssertOption<T, DateAssert<T>> isAfter(LocalDate date);
    
    AssertOption<T, DateAssert<T>> isAfter(LocalTime time);
    
    AssertOption<T, DateAssert<T>> isBewteen(T begin, T end);
    
    AssertOption<T, DateAssert<T>> isBewteen(LocalDateTime begin, LocalDateTime end);
    
    AssertOption<T, DateAssert<T>> isBewteen(LocalDate begin, LocalDate end);
    
    AssertOption<T, DateAssert<T>> isBewteen(LocalTime begin, LocalTime end);
    
    AssertOption<T, DateAssert<T>> isNotBewteen(T begin, T end);
    
    AssertOption<T, DateAssert<T>> isNotBewteen(LocalDateTime begin, LocalDateTime end);
    
    AssertOption<T, DateAssert<T>> isNotBewteen(LocalDate begin, LocalDate end);
    
    AssertOption<T, DateAssert<T>> isNotBewteen(LocalTime begin, LocalTime end);
}
