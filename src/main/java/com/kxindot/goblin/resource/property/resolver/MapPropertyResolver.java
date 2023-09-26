package com.kxindot.goblin.resource.property.resolver;

import static com.kxindot.goblin.Objects.Comma;
import static com.kxindot.goblin.Objects.EqualSign;
import static com.kxindot.goblin.Objects.isNotBlank;
import static com.kxindot.goblin.Objects.newLinkedHashMap;
import static com.kxindot.goblin.Objects.substringAfter;
import static com.kxindot.goblin.Objects.substringBefore;
import static com.kxindot.goblin.Objects.substringBetween;
import static com.kxindot.goblin.Throws.threx;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.kxindot.goblin.Regex;

/**
 * @author ZhaoQingJiang
 */
public class MapPropertyResolver<V> extends AbstractPropertyResolver<Map<String, V>> {

    private static final String PA = "^\\{[\\s\\S]*\\}$";
    private String seperator;
    private String connector;
    private String keyValPattern;
    private PropertyResolver<V> resolver;

    public MapPropertyResolver(PropertyResolver<V> resolver) {
        this(null, resolver);
    }
    
    public MapPropertyResolver(String name, PropertyResolver<V> resolver) {
        this(name, Comma, EqualSign, resolver);
    }

    public MapPropertyResolver(String name, String seperator, String connector, PropertyResolver<V> resolver) {
        super(name);
        this.seperator = seperator;
        this.connector = connector;
        this.resolver = resolver;
        this.keyValPattern = "^[\\s\\S]+?\\" + connector + "[\\s\\S]*$";
        setType(Map.class);
    }

    public Class<?> getValueType() {
        return resolver.getType();
    }

    @Override
    public Map<String, V> resolve(String property) {
        Map<String, V> map = instance();
        if (isNotBlank(property)) {
            String[] keyValues = property.split(seperator);
            for (String keyValue : keyValues) {
                if (!Regex.isMatch(keyValue, keyValPattern)) {
                    threx(PropertyResolveException::new, "不合法的Map键值对语法: %s", keyValue);
                }
                String key = substringBefore(keyValue, connector);
                String value = substringAfter(keyValue, connector);
                map.put(key, resolver.resolve(value));
            }
        }
        return map;
    }

    @Override
    protected Map<String, V> resovle(String name, Properties properties) {
        Map<String, V> map = resolve(properties.getProperty(name));
        Set<Object> pks = properties.keySet();
        for (Object obj : pks) {
            if (!String.class.isInstance(obj)) {
                continue;
            }
            String pk = String.class.cast(obj);
            if (pk.equals(name) || !pk.startsWith(name)) {
                continue;
            }
            String suffix = substringAfter(pk, name);
            if (!Regex.isMatch(suffix, PA)) {
                threx(PropertyResolveException::new, "不合法的Map键(Key)定义: %s", pk);
            }
            String key = substringBetween(suffix, "{", "}");
            V value = resolver.resolve(properties.getProperty(pk));
            map.put(key, value);
        }
        return map;
    }

    Map<String, V> instance() {
        return newLinkedHashMap();
    }

}
