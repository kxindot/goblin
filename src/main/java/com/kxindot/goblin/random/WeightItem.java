package com.kxindot.goblin.random;

import static com.kxindot.goblin.Objects.isEqual;
import static java.math.BigDecimal.valueOf;

/**
 * 权重项目类
 * @author ZhaoQingJiang
 */
public class WeightItem<T> implements Comparable<WeightItem<T>> {

    private T item;
    private double weight;

    public WeightItem(T item, double weight) {
        this.item = item;
        this.weight = weight;
    }
    
    public T getItem() {
        return item;
    }

    public double getWeight() {
        return weight;
    }
    
    @Override
    public int compareTo(WeightItem<T> o) {
        return valueOf(weight).compareTo(valueOf(o.weight));
    }
    
    @Override
    public int hashCode() {
        return item == null ? 0 : item.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && obj instanceof WeightItem) {
            return isEqual(item, WeightItem.class.cast(obj).item);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("(weight=").append(weight)
                .append(", item=").append(item).append(")").toString();
    }
}
