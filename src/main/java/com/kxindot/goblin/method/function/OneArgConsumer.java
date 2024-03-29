package com.kxindot.goblin.method.function;

import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface OneArgConsumer<T, R> extends BiConsumer<T, R>, Serializable {

}
