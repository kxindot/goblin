package com.kxindot.goblin.method.function;

import java.util.function.Function;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 * @param <R> Method Return type
 */
@FunctionalInterface
public interface TwoArgFunction<T, P1, P2, R> {
    
    R apply(T t, P1 p1, P2 p2);
    
    default <V> TwoArgFunction<T, P1, P2, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNotNull(after);
        return (T t, P1 p1, P2 p2) -> after.apply(apply(t, p1, p2));
    }
}