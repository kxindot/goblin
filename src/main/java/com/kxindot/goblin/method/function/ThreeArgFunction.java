package com.kxindot.goblin.method.function;

import java.util.function.Function;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 * @param <P3> Method Third Parameter Type
 * @param <R> Method Return type
 */
@FunctionalInterface
public interface ThreeArgFunction<T, P1, P2, P3, R> {
    
    R apply(T t, P1 p1, P2 p2, P3 p3);
    
    default <V> ThreeArgFunction<T, P1, P2, P3, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNotNull(after);
        return (T t, P1 p1, P2 p2, P3 p3) -> after.apply(apply(t, p1, p2, p3));
    }
}