package com.kxindot.goblin.method.function;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P1> Method First Parameter Type
 * @param <P2> Method Second Parameter Type
 */
@FunctionalInterface
public interface TwoArgConsumer<T, P1, P2> {

    
    void accept(T t, P1 p1, P2 p2);

    
    default TwoArgConsumer<T, P1, P2> andThen(TwoArgConsumer<? super T, ? super P1, ? super P2> after) {
        Objects.requireNotNull(after);
        return (l, r1, r2) -> {
            accept(l, r1, r2);
            after.accept(l, r1, r2);
        };
    }
    
}
