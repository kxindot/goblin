package com.kxindot.goblin.asserts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
class DateAssertImpl<T extends Date> 
extends AbstractAssert<T, DateAssert<T>>
implements DateAssert<T> {

    DateAssertImpl(T t) {
        super(t);
    }

    @Override
    public AssertOption<T, DateAssert<T>> isEqual(T date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isEqual(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isEqual(LocalDate date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isEqual(LocalTime time) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBefore(T date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBefore(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBefore(LocalDate date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBefore(LocalTime time) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isAfter(T date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isAfter(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isAfter(LocalDate date) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isAfter(LocalTime time) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBewteen(T begin, T end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBewteen(LocalDateTime begin, LocalDateTime end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBewteen(LocalDate begin, LocalDate end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isBewteen(LocalTime begin, LocalTime end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isNotBewteen(T begin, T end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isNotBewteen(LocalDateTime begin, LocalDateTime end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isNotBewteen(LocalDate begin, LocalDate end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssertOption<T, DateAssert<T>> isNotBewteen(LocalTime begin, LocalTime end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    DateAssert<T> self() {
        return this;
    }
}
