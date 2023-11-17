package com.kxindot.goblin.method.function;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author ZhaoQingJiang
 */
@FunctionalInterface
public interface OneArgStaticConsumer<T> extends Consumer<T>, Serializable {

}
