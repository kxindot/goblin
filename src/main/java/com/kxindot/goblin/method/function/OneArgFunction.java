package com.kxindot.goblin.method.function;

import java.util.function.BiFunction;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <P> Method Parameter Type
 * @param <R> Method Return type
 */
@FunctionalInterface
public interface OneArgFunction<T, P, R> extends BiFunction<T, P, R> {}