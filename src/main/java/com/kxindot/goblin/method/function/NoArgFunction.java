package com.kxindot.goblin.method.function;

import java.util.function.Function;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 * @param <R> Method Return type
 */
@FunctionalInterface
public interface NoArgFunction<T, R> extends Function<T, R> {}