package com.kxindot.goblin.asserts;

import static com.kxindot.goblin.Objects.EMPTY_OBJ_ARRAY;
import static com.kxindot.goblin.Objects.stringFormat;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author ZhaoQingJiang
 */
abstract class AbstractAssert<T, A extends Assert<T, A>> implements Assert<T, A>, AssertOption<T, A> {

    protected T t;
    private boolean r;
    private boolean or;

    AbstractAssert(T t) {
        this.t = t;
        this.r = false;
        this.or = true;
    }
    
    abstract A self();
    
    final AssertOption<T, A> predicate(boolean b) {
        r = or ? r || b : r && b;
        return this;
    }

    @Override
    public AssertOption<T, A> isNull() {
        return predicate(t == null);
    }
    
    @Override
    public AssertOption<T, A> isNotNull() {
        return predicate(t != null);
    }
    
    @Override
    public AssertOption<T, A> isTrue(Predicate<T> predicate) {
        return predicate(predicate.test(t));
    }
    
    @Override
    public AssertOption<T, A> isInstanceOf(Class<?> type) {
        return predicate(type.isInstance(t));
    }
    
    @Override
    public A and() {
        or = false;
        return self();
    }
    
    @Override
    public A or() {
        or = true;
        return self();
    }
    
    
    @Override
    public T threw() {
        return threw(IllegalArgumentException::new);
    }
    
    @Override
    public T threw(String message) {
        return threw(message, EMPTY_OBJ_ARRAY);
    }
    
    @Override
    public T threw(String format, Object... args) {
        return threw(IllegalArgumentException::new, format, args);
    }
    
    @Override
    public <E extends RuntimeException> T threw(Supplier<E> supplier) {
        return exception(r, supplier);
    }
    
    @Override
    public <E extends RuntimeException> T threw(Function<String, E> function, String message) {
        return threw(function, message, EMPTY_OBJ_ARRAY);
    }
    
    @Override
    public <E extends RuntimeException> T threw(Function<String, E> function, String format, Object... args) {
        return exception(r, function, format, args);
    }
    
    @Override
    public T elseThrow() {
        return elseThrow(IllegalArgumentException::new);
    }
    
    @Override
    public T elseThrow(String message) {
        return elseThrow(message, EMPTY_OBJ_ARRAY);
    }
    
    @Override
    public T elseThrow(String format, Object... args) {
        return elseThrow(IllegalArgumentException::new, format, args);
    }
    
    @Override
    public <E extends RuntimeException> T elseThrow(Supplier<E> supplier) {
        return exception(!r, supplier);
    }
    
    @Override
    public <E extends RuntimeException> T elseThrow(Function<String, E> function, String message) {
        return elseThrow(function, message, EMPTY_OBJ_ARRAY);
    }
    
    @Override
    public <E extends RuntimeException> T elseThrow(Function<String, E> function, String format, Object... args) {
        return exception(!r, function, format, args);
    }
    
    final <E extends RuntimeException> T exception(boolean r, Supplier<E> supplier) {
        if (r) {
            throw supplier.get();
        }
        return t;
    }
    
    final <E extends RuntimeException> T exception(boolean r, Function<String, E> function, String format, Object... args) {
        if (r) {
            throw function.apply(stringFormat(format, args));
        }
        return t;
    }

}
