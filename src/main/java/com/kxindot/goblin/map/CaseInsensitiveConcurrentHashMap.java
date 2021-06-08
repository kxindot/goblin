package com.kxindot.goblin.map;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhaoQingJiang
 */
public class CaseInsensitiveConcurrentHashMap<V> extends ConcurrentHashMap<String, V>  {

    private static final long serialVersionUID = -2655035560191123268L;
    private final Map<String, String> lowerCaseMap = new ConcurrentHashMap<String, String>();
    
    @Override
    public boolean containsKey(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return realKey == null ? false : super.containsKey(realKey);
    }

    @Override
    public V get(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return realKey == null ? null : super.get(realKey);
    }

    @Override
    public V put(String key, V value) {
        Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
        V oldValue;
        if (oldKey != null) {
            oldValue = super.remove(oldKey);
        }
        oldValue = super.put(key, value);
        return oldValue;
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        m.forEach((k, v) -> put(k, v));
    }    

    @Override
    public V remove(Object key) {
        Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
        return realKey == null ? null : super.remove(realKey);
    }
}
