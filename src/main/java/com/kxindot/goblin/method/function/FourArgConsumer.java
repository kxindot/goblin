package com.kxindot.goblin.method.function;

import java.io.Serializable;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 * @param <P3> Method Third Parameter Type
 * @param <P4> Method Fourth Parameter Type
 */
@FunctionalInterface
public interface FourArgConsumer<T, P1, P2, P3, P4> extends Serializable {

	void accept(T t, P1 p1, P2 p2, P3 p3, P4 p4);

    default FourArgConsumer<T, P1, P2, P3, P4> andThen(FourArgConsumer<? super T, ? super P1, ? super P2, ? super P3, ? super P4> after) {
        Objects.requireNotNull(after);
        return (l, r1, r2, r3, r4) -> {
            accept(l, r1, r2, r3, r4);
            after.accept(l, r1, r2, r3, r4);
        };
    }
}
