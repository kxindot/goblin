package com.kxindot.goblin.asserts;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ZhaoQingJiang
 */
public interface AssertOption<T, A extends Assert<T, A>> {

    A and();

    A or();

    T threw();

    T threw(String message);

    T threw(String format, Object... args);

    <E extends RuntimeException> T threw(Supplier<E> supplier);

    <E extends RuntimeException> T threw(Function<String, E> function, String message);

    <E extends RuntimeException> T threw(Function<String, E> function, String format, Object... args);

    T elseThrow();

    T elseThrow(String message);

    T elseThrow(String format, Object... args);

    <E extends RuntimeException> T elseThrow(Supplier<E> supplier);

    <E extends RuntimeException> T elseThrow(Function<String, E> function, String message);

    <E extends RuntimeException> T elseThrow(Function<String, E> function, String format, Object... args);
}
