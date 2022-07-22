package com.kxindot.goblin.map;

import static com.kxindot.goblin.Objects.isEqual;
import static com.kxindot.goblin.Objects.requireNotNull;
import static java.lang.String.format;

import java.io.Serializable;

/**
 * 键值对抽象接口
 * 
 * @author ZhaoQingJiang
 */
public interface Pair<K, V> {

    /**
     * 获取键的值
     * @return 键的值
     */
    K getKey();
    
    /**
     * 获取键对应的值
     * @return 键对应的值
     */
    V getValue();
    
    /**
     * 设置键值对
     * @param k 键的值
     * @param v 键对应的值
     */
    void set(K k, V v);
    
    /**
     * 设置键的值
     * @param k 新键的值
     * @return 旧键的值
     */
    K setKey(K k);
    
    /**
     * 设置键对应的值
     * @param v 新键对应的值
     * @return 旧键对应的值
     */
    V setValue(V v);
    
    /**
     * 比较键是否相等.
     * 若给定值类型是{@link Pair}类型,则进行键{@code -}键比对;
     * 否则,直接与键的值进行比对.
     * @param obj Object
     * @return 若相等返回true,否则返回false.
     */
    boolean isKeyEqual(Object obj);
    
    /**
     * 比较键对应的值是否相等.
     * 若给定值类型是{@link Pair}类型,则进行值{@code -}值比对;
     * 否则,直接与键对应的值进行比对.
     * @param obj Object
     * @return 若相等返回true,否则返回false.
     */
    boolean isValueEqual(Object obj);
    
    /**
     * 重写equal方法
     * @param obj Object
     * @return boolean
     */
    boolean equals(Object obj);

    /**
     * 重写toString方法
     * @return String
     */
    String toString();
    
    
    /**
     * {@link Pair}接口默认实现
     * 
     * @author ZhaoQingJiang
     */
    @SuppressWarnings("rawtypes")
    public class DefaultPair<K, V> implements Pair<K, V>, Serializable {

        private static final long serialVersionUID = 5409788880932605858L;

        private K key;

        private V value;
        
        public DefaultPair() {}
        
        public DefaultPair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void set(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override
        public K setKey(K k) {
            K old = key;
            this.key = k;
            return old;
        }

        @Override
        public V setValue(V v) {
            V old = value;
            this.value = v;
            return old;
        }

        @Override
        public boolean isKeyEqual(Object obj) {
            if (this != obj) {
                if (null != obj && obj instanceof Pair) {
                    Pair pair = Pair.class.cast(obj);
                    return isEqual(key, pair.getKey());
                } else {
                    return isEqual(key, obj);
                }
            }
            return true;
        }

        @Override
        public boolean isValueEqual(Object obj) {
            if (this != obj) {
                if (null != obj && obj instanceof Pair) {
                    Pair pair = Pair.class.cast(obj);
                    return isEqual(value, pair.getValue());
                } else {
                    return isEqual(value, obj);
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (null != obj && obj instanceof Pair) {
                Pair pair = Pair.class.cast(obj);
                return isEqual(key, pair.getKey()) 
                        && isEqual(value, pair.getValue());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            if (key == null && value == null) {
                return super.hashCode();
            } else if (key != null && value == null) {
                return key.hashCode();
            } else if (key == null && value != null) {
                return value.hashCode();
            } else {
                return toString().hashCode();
            }
        }

        @Override
        public String toString() {
            return format("%s[%s : %s]", 
                    getClass().getSimpleName(), key, value);
        }

    }
    
    
    /**
     * {@link Pair}接口的一种实现,继承自{@link DefaultPair}.<br>
     * 本实现中,键与值均不能为null.<br>
     * 在键值对通过构造器初始化后,不能再次进行赋值修改.<br>
     * 若调用{@link UnmodifiablePair#set(Object, Object)}
     * 或{@link UnmodifiablePair#setKey(Object)}
     * 或{@link UnmodifiablePair#setValue(Object)},
     * 则会抛出UnsupportedOperationException
     * 
     * @author ZhaoQingJiang
     */
    public class UnmodifiablePair<K, V> extends DefaultPair<K, V> {

        private static final long serialVersionUID = -6029131459571696827L;

        public UnmodifiablePair(K k, V v) {
            super(requireNotNull(k, "Not accept null key")
                    , requireNotNull(v, "Not accept null value"));
        }
        
        @Override
        public void set(K k, V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K setKey(K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }
        
    }
    
    
}
