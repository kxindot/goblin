package com.kxindot.goblin.method.function;

import java.io.Serializable;
import java.util.function.Function;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 * @param <P3> Method Third Parameter Type
 * @param <P4> Method Fourth Parameter Type
 * @param <P5> Method Fifth Parameter Type
 * @param <P6> Method Sixth Parameter Type
 * @param <R> Method Return type
 */
@FunctionalInterface
public interface SixArgFunction<T, P1, P2, P3, P4, P5, P6, R> extends Serializable {
    
    R apply(T t, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6);
    
    default <V> SixArgFunction<T, P1, P2, P3, P4, P5, P6, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNotNull(after);
        return (T t, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6) -> after.apply(apply(t, p1, p2, p3, p4, p5, p6));
    }
}