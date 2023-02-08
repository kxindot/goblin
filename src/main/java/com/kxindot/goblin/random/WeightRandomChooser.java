package com.kxindot.goblin.random;

import static com.kxindot.goblin.Objects.newArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * <p>
 * 权重随机选择器
 * </p>
 * @author ZhaoQingJiang
 */
public final class WeightRandomChooser<T> implements RandomChooser<WeightItem<T>> {

    private static Logger logger = LoggerFactory.getLogger(WeightRandomChooser.class);
    private volatile boolean prepared;
    private double[] section;
    private List<WeightItem<T>> items;

    /**
     * 构造权重随机选择器
     */
    public WeightRandomChooser() {
        this.items = newArrayList();
    }

    /**
     * 构造权重随机选择器
     * @see #addAll(Collection)
     * @param items {@code Collection<WeightItem<T>>} 权重项目集合
     * @throws NullPointerException 若入参集合items==null,则抛出此异常
     */
    public WeightRandomChooser(Collection<WeightItem<T>> items) {
        this();
        addAll(items);
    }
    
    /**
     * 返回随机选择器内的权重项目集合
     * @see WeightItem
     * @return {@code Collection<WeightItem<T>>}
     */
    @Override
    public Collection<WeightItem<T>> items() {
        return newArrayList(items);
    }
    
    /**
     * 判断权重项目是否存在
     */
    @Override
    public boolean contains(WeightItem<T> item) {
        return items.contains(item);
    }
    
    /**
     * 向随机选择器添加权重项目。
     * <pre>
     * 若权重为NaN，则将此项目权重重设为1.0d；
     * 若权重为无穷大，则将此项目权重重设为10000.0d;
     * 若权重为负数，则此项目将被选择器忽略，不会出现在待选列表中；
     * 若添加已存在的项目（使用{@link WeightItem#equals(Object)}判断），则已存在项目将会被覆盖；
     * </pre>
     * @see WeightItem
     * @see WeightItem#equals
     * @param item WeightItem 权重项目
     */
    @Override
    public void add(WeightItem<T> item) {
        double weight = item.getWeight();
        if (weight <= 0) {
            logger.warn("发现负权重项目,此项目将被忽略: 权重={}, 项目={}", weight, item);
            return;
        } else if (Double.isInfinite(weight)) {
            item = new WeightItem<>(item.getItem(), 10000.0d);
        } else if (Double.isNaN(weight)) {
            item = new WeightItem<>(item.getItem(), 1.0d);
        }
        int i = -1;
        if ((i = items.indexOf(item)) != -1) {
            items.set(i, item);
        } else {
            items.add(item);
        }
        prepared = false;
    }

    /**
     * 向随机选择器添加项目及其权重。
     * 根据入参首先生成{@link WeightItem}对象,然后调用{@link #add(WeightItem)}方法添加。
     * @see #add(WeightItem)
     * @param item 项目
     * @param weight 权重
     */
    public void add(T item, double weight) {
        add(new WeightItem<>(item, weight));
    }

    /**
     * 向随机选择器添加权重项目集合。遍历集合调用{@link #add(WeightItem)}方法添加。
     * @see #add(WeightItem)
     * @param items {@code Collection<WeightItem<T>>} 权重项目集合
     * @throws NullPointerException 若入参集合items==null，则抛出此异常
     */
    @Override
    public void addAll(Collection<WeightItem<T>> items) {
        items.forEach(this::add);
    }
    
    @Override
    public void remove(WeightItem<T> item) {
        items.remove(item);
        prepared = false;
    }

    /**
     * 按权重比例随机选择项目。
     * @throws IllegalArgumentException 若未添加任何项目至选择器,则抛出此异常
     */
    @Override
    public WeightItem<T> choose() {
        prepare();
        double num = ThreadLocalRandom.current().nextDouble();
        int pos = Arrays.binarySearch(section, num);
        WeightItem<T> item = pos >= 0 ? items.get(pos)
                : ((pos = -pos - 1) < section.length && num < section[pos]) ? items.get(pos) : items.get(0);
        return item;
    }

    /**
     * 随机选择前的数据准备
     */
    private void prepare() {
        if (prepared) return;
        if (items.isEmpty()) throw new IllegalArgumentException("需要添加待随机选择的权重项目!");
        double pos = 0D;
        this.section = new double[items.size()];
        double total = items.stream().mapToDouble(WeightItem::getWeight).sum();
        for (int i = 0; i < items.size(); i++) {
            section[i] = pos += items.get(i).getWeight() / total;
        }
        prepared = true;
    }

    
    @Override
    public String toString() {
        return items.stream().map(WeightItem::toString).collect(Collectors.joining("\n"));
    }
}
