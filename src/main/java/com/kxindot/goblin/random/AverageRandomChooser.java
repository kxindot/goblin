package com.kxindot.goblin.random;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <p>
 * 平均权重随机选择器
 * </p>
 * 添加到此选择器内的项目拥有相同的被选中几率。
 * @author ZhaoQingJiang
 */
public class AverageRandomChooser<T> implements RandomChooser<T> {

    private WeightRandomChooser<T> chooser;

    /**
     * 构造平均权重随机选择器
     */
    public AverageRandomChooser() {
        this.chooser = new WeightRandomChooser<>();
    }
    
    /**
     * 构造平均权重随机选择器
     * @see #addAll(Collection)
     * @param items {@code Collection<T>} 项目集合
     * @throws NullPointerException 若入参集合items==null,则抛出此异常
     */
    public AverageRandomChooser(Collection<T> items) {
        this();
        addAll(items);
    }
    
    @Override
    public Collection<T> items() {
        return chooser.items().stream()
                .map(WeightItem::getItem).collect(Collectors.toList());
    }
    
    @Override
    public boolean contains(T item) {
        return chooser.contains(new WeightItem<>(item, 1.0d));
    }

    /**
     * {@inheritDoc}
     * 支持添加值等于null的项目。
     */
    @Override
    public void add(T item) {
        chooser.add(item, 1.0d);
    }

    /**
     * @see #add(Object)
     * @throws NullPointerException 若入参集合items==null，则抛出此异常
     */
    @Override
    public void addAll(Collection<T> items) {
        items.forEach(this::add);
    }
    
    @Override
    public void remove(T item) {
        chooser.remove(new WeightItem<>(item, 1.0d));
    }

    /**
     * 等几率随机选择项目。
     * 若向选择器中添加了值等于null的项目,则返回值可能是null。
     * @throws IllegalArgumentException 若未添加任何项目至选择器,则抛出此异常
     */
    @Override
    public T choose() {
        return chooser.choose().getItem();
    }

    @Override
    public String toString() {
        return chooser.items().stream()
                .map(WeightItem::getItem)
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
