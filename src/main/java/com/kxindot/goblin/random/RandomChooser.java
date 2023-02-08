package com.kxindot.goblin.random;

import java.util.Collection;

/**
 * 随机选择器接口
 * @author ZhaoQingJiang
 */
public interface RandomChooser<T> {
    
    /**
     * 判断项目是否已存在。
     * @param item T 项目
     * @return 项目是否已存在
     */
    boolean contains(T item);
    
    /**
     * 添加项目至选择器。
     * @param item T 项目
     */
    void add(T item);
    
    /**
     * 添加集合中所有项目至选择器。
     * @see #add(Object)
     * @param items {@code Collection<T>} 项目集合
     * @throws NullPointerException 若入参集合items==null，则抛出此异常
     */
    void addAll(Collection<T> items);
    
    /**
     * 从选择器中移除项目。
     * 对象实例则使用{@link Object#equals(Object)}方法判定是否相等。
     * @param item T 项目
     */
    void remove(T item);
    
    /**
     * 返回添加到选择器中的项目集合。
     * @return {@code Collection<T>}
     */
    Collection<T> items();

    /**
     * 随机选择项目。
     * @return T 项目
     * @throws IllegalArgumentException 若未添加任何项目至选择器,则抛出此异常
     */
    T choose();
    
}
