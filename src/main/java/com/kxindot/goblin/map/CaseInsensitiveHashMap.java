package com.kxindot.goblin.map;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author ZhaoQingJiang
 */
public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {

    private static final long serialVersionUID = 6459790064706846998L;
    private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

    @Override
    public boolean containsKey(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.containsKey(realKey);
    }

    @Override
    public V get(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.get(realKey);
    }

    @Override
    public V put(String key, V value) {
        Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
        V oldValue = super.remove(oldKey);
        super.put(key, value);
        return oldValue;
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        m.forEach((k, v) -> put(k, v));
    }    

    @Override
    public V remove(Object key) {
        Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
        return super.remove(realKey);
    }
    
}
