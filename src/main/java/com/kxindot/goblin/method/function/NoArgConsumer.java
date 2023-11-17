package com.kxindot.goblin.method.function;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author ZhaoQingJiang
 * @param <T> Method Class Type
 */
@FunctionalInterface
public interface NoArgConsumer<T> extends Consumer<T>, Serializable {}
