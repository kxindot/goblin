package com.kxindot.goblin.method.function;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 * @param <P3> Method Third Parameter Type
 */
@FunctionalInterface
public interface ThreeArgConsumer<T, P1, P2, P3> {

    void accept(T t, P1 p1, P2 p2, P3 p3);

    default ThreeArgConsumer<T, P1, P2, P3> andThen(ThreeArgConsumer<? super T, ? super P1, ? super P2, ? super P3> after) {
        Objects.requireNotNull(after);
        return (l, r1, r2, r3) -> {
            accept(l, r1, r2, r3);
            after.accept(l, r1, r2, r3);
        };
    }
}
