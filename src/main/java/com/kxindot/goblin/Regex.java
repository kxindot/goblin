package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.newConcurrentHashMap;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * @author ZhaoQingJiang
 */
public class Regex {

    /** 正则Pattern缓存 */
    private static Map<String, Pattern> patterns = newConcurrentHashMap();
    
    
    /**
     * 判断指定的字符串是否匹配对应的正则表达式
     * 
     * @param text 待匹配字符串
     * @param regex 正则表达式
     * @return boolean 若匹配返回true,反之false
     */
    public static boolean isMatch(CharSequence text, String regex) {
        if (text == null || regex == null) {
            return false;
        }
        return compile(regex).matcher(text).matches();
    }
    
    /**
     * TODO : 1.2.0<br>
     * 
     * 对正则关键字进行转义。
     * <pre>
     * 
     * </pre>
     * 
     * @param keyword char
     * @return String
     */
    public static String escapeKeyword(char keyword) {
    	throw new AbstractMethodError();
    }
    
    /**
     * TODO : 1.2.0<br>
     * 
     * 对正则关键字进行转义。
     * <pre>
     * 
     * </pre>
     * 
     * @param str String
     * @return String
     */
    public static String escapeString(String str) {
    	throw new AbstractMethodError();
    }
    
    /**
     * 获取Pattern对象
     */
    private static Pattern compile(String regex) {
        Pattern pattern = patterns.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patterns.putIfAbsent(regex, pattern);
        }
        return pattern;
    }
    
}
