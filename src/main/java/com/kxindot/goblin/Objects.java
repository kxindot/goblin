package com.kxindot.goblin;

import static com.kxindot.goblin.reflection.Reflections.newArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kxindot.goblin.exception.RuntimeException;
import com.kxindot.goblin.map.CaseInsensitiveConcurrentHashMap;
import com.kxindot.goblin.map.CaseInsensitiveHashMap;
import com.kxindot.goblin.map.CaseInsensitiveLinkedHashMap;
import com.kxindot.goblin.map.Pair;
import com.kxindot.goblin.map.Pair.DefaultPair;
import com.kxindot.goblin.map.Pair.UnmodifiablePair;
import com.kxindot.goblin.typeconvert.TypeConvertException;
import com.kxindot.goblin.typeconvert.TypeConverter;
import com.kxindot.goblin.typeconvert.TypeConverterFactory;

/**
 * @author ZhaoQingJiang
 */
public final class Objects {
    
    private Objects() {}
    
    
    /**-------------------对象-------------------**/
    
    /**
     * 长度为0的Object数组
     */
    public static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
    
    /**
     * 判断传入的两个参数是否相等
     * @param a Object
     * @param b Object
     * @return - 若传入的两个参数相等则返回true,反之false
     */
    public static boolean isEqual(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
    
    /**
     * 判断传入参数是否 <b><i>等于</i></b> null
     * @param obj Object
     * @return - 若传入参数等于null则返回true,反之false
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    
    /**
     * 判断传入参数是否 <b><i>部分等于</i></b> null
     * <pre>
     * isAnyNull(null)      =  true
     * isAnyNull(null,"a")  =  true
     * isAnyNull(1,"a")     =  false
     * </pre>
     * @param objs {@code Object[]}
     * @return - 若传入参数部分等于null则返回true,反之false
     */
    public static boolean isAnyNull(Object... objs) {
        return !isAllNotNull(objs);
    }
    
    /**
     * 判断传入参数是否 <b><i>全部等于</i></b> null
     * <pre>
     * isAllNull(null)      =  true
     * isAllNull(null,null) =  true
     * isAllNull(null,"a")  =  false
     * </pre>
     * @param objs {@code Object[]}
     * @return - 若传入参数全部等于null则返回true,反之false
     */
    public static boolean isAllNull(Object... objs) {
        boolean b = isNotNull(objs);
        if (b) {
            for (Object obj : objs) {
                b &= isNull(obj);
                if (!b) break;
            }
        }
        return b;
    }
    
    /**
     * 判断传入参数是否 <b><i>不等于</i></b> null
     * @param obj Object
     * @return - 若传入参数不等于null则返回true,反之false
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    /**
     * 判断传入参数是否 <b><i>不全等于</i></b> null
     * <pre>
     * isNotAllNull(null)      =  false
     * isNotAllNull(null,null) =  false
     * isNotAllNull(null,"a")  =  true
     * isNotAllNull(1,"a")     =  true
     * </pre>
     * @param objs {@code Object[]}
     * @return - 若传入参数不全等于null则返回true,反之false
     */
    public static boolean isAnyNotNull(Object... objs) {
        return !isAllNull(objs);
    }
    
    /**
     * 判断传入参数是否 <b><i>全部不等于</i></b> null
     * <pre>
     * isAllNotNull(null)      =  false
     * isAllNotNull(null,"a")  =  false
     * isAllNotNull(1,"a")     =  true
     * </pre>
     * @param objs {@code Object[]}
     * @return - 若传入参数全部不等于null则返回true,反之false
     */
    public static boolean isAllNotNull(Object... objs) {
        boolean b = isNotNull(objs);
        if (b) {
            for (Object obj : objs) {
                b &= isNotNull(obj);
                if (!b) break;
            }
        }
        return b;
    }
    
    /**
     * 对象转换
     * @param source 源对象
     * @param target 目标对象类型
     * @return - 若source=null则返回null,否则返回转换后的对象
     * @throws TypeConvertException 若转换出现错误,则抛出该异常
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <S, T> T convert(S source, Class<T> target) throws TypeConvertException {
        Class<?> st = source.getClass();
        if (st == target) return (T) source;
        TypeConverter c = TypeConverterFactory.getTypeConverter(st, target);
        if (c == null) {
            throw new TypeConvertException("Can't find TypeConverter to convert " 
                    + st.getName() + " to " + target.getName());
        }
        return (T) c.convert(source);
    }
    
    /**-------------------字符串-------------------**/
    
    /** 空字符串（empty）*/
    public static final String EMP = "";
    /** 空格（white space）*/
    public static final String WS = " ";
    /** 制表符（tab）：\t  */
    public static final String TAB = "\t";
    /** 换行符（line feed）：\n */
    public static final String LF = "\n";
    /** 退格符（backspace）：\b */
    public static final String BS = "\b";
    /** 回车符（carriage return）：\r */
    public static final String CR = "\r";
    /** 换页符（form feed）：\f */
    public static final String FF = "\f";
    /** 正斜杠：/ */
    public static final String Slash = "/";
    /** 反斜杠：\ */
    public static final String Backslash = "\\";
    /** 点：. */
    public static final String Dot = ".";
    /** 逗号：, */
    public static final String Comma = ",";
    /** 冒号：: */
    public static final String Colon = ":";
    /** 分号：; */
    public static final String Semicolon = ";";
    /** 短横杠：- */
    public static final String Hyphen = "-";
    /** 星号：* */
    public static final String Asterisk = "*";
    /** 百分号：% */
    public static final String Percent = "%";
    /** 加号：+ */
    public static final String Cross = "+";
    /** 问号：? */
    public static final String Question = "?";
    /** 感叹号：! */
    public static final String Exclamation = "!";
    /** 等号：= */
    public static final String EqualSign = "=";
    /** 竖线：| */
    public static final String VerticalLine = "|";
    /** 美元符号：$ */
    public static final String DollarSign = "$";
    /** at：@ */
    public static final String AtSign = "@";
    /** 井号：# */
    public static final String HashSign = "#";
    /** and：{@code &} */
    public static final String AndSign = "&";
    /** 左圆括号（left parenthesis）：( */
    public static final String LP = "(";
    /** 右圆括号（right parenthesis）：) */
    public static final String RP = ")";
    /** 左中括号（left curly brace）：{ */
    public static final String LCB = "{";
    /** 右中括号（right curly brace）：} */
    public static final String RCB = "}";
    /** 左大括号（left square bracket）：[ */
    public static final String LSB = "[";
    /** 右大括号（right square bracket）：] */
    public static final String RSB = "]";
    /** 左尖括号/小于符号（left angle bracket）：{@code <} */
    public static final String LAB = "<";
    /** 右尖括号/大于符号（right angle bracket）：{@code >} */
    public static final String RAB = ">";
    /** 字符串下标 */
    private static final int Index_Not_Found = -1;
    
    /**
     * 判断传入参数是否 <b><i>等于</i></b> null或""
     * @param cs CharSequence
     * @return - 若传入参数是否等于null或""则返回true,反之false
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    
    /**
     * 判断传入参数是否 <b><i>等于</i></b> null或空字符串(仅含有空格)
     * <pre>
     * isBlank(null)      = true
     * isBlank("")        = true
     * isBlank(" ")       = true
     * isBlank("bob")     = false
     * isBlank("  bob  ") = false
     * </pre>
     * @param cs CharSequence
     * @return - 若传入参数等于null或空字符串(仅含有空格)则返回true,反之false
     */
    public static boolean isBlank(CharSequence cs) {
        int len;
        if (cs == null || (len = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断传入参数是否 <b><i>部分等于</i></b> null或空字符串(仅含有空格)
     * <pre>
     * isAnyBlank(null)             = true
     * isAnyBlank(null, "foo")      = true
     * isAnyBlank(null, null)       = true
     * isAnyBlank("", "bar")        = true
     * isAnyBlank("bob", "")        = true
     * isAnyBlank("  bob  ", null)  = true
     * isAnyBlank(" ", "bar")       = true
     * isAnyBlank("foo", "bar")     = false
     * </pre>
     * @param css {@code CharSequence[]}
     * @return - 若传入参数部分等于null或空字符串(仅含有空格)则返回true,反之false
     */
    public static boolean isAnyBlank(CharSequence... css) {
        if (isEmpty(css)) return true;
        for (CharSequence cs : css) {
            if (isBlank(cs)) return true;
        }
        return false;
    }
    
    /**
     * 判断传入参数是否 <b><i>全部等于</i></b> null或空字符串(仅含有空格)
     * <pre>
     * isAllBlank(null)             = true
     * isAllBlank(null, "   ")      = true
     * isAllBlank(null, null)       = true
     * isAllBlank("", null)         = true
     * isAllBlank("bob", "")        = false
     * isAllBlank("  bob  ", null)  = false
     * isAllBlank(" ", "bar")       = false
     * isAllBlank("foo", "bar")     = false
     * </pre>
     * @param css {@code CharSequence[]}
     * @return - 若传入参数全部等于null或空字符串(仅含有空格)则返回true,反之false
     */
    public static boolean isAllBlank(CharSequence... css) {
        if (isEmpty(css)) return true;
        for (CharSequence cs : css) {
            if (isNotBlank(cs)) return false;
        }
        return true;
    }
    
    /**
     * 判断传入参数是否 <b><i>不等于</i></b> null或空字符串(仅含有空格)
     * <pre>
     * isBlank(null)      = false
     * isBlank("")        = false
     * isBlank(" ")       = false
     * isBlank("bob")     = true
     * isBlank("  bob  ") = true
     * </pre>
     * @param cs CharSequence
     * @return - 若传入参数不等于null或空字符串(仅含有空格)则返回true,反之false
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    
    /**
     * 判断传入参数是否 <b><i>不全等于</i></b> null或空字符串(仅含有空格)
     * <pre>
     * isAnyNotBlank(null)             = false
     * isAnyNotBlank(null, "foo")      = true
     * isAnyNotBlank(null, null)       = false
     * isAnyNotBlank("", "bar")        = true
     * isAnyNotBlank("bob", "")        = true
     * isAnyNotBlank("  bob  ", null)  = true
     * isAnyNotBlank(" ", "   ")       = false
     * isAnyNotBlank("foo", "bar")     = true
     * </pre>
     * @param css {@code CharSequence[]}
     * @return - 若传入参数不全等于null或空字符串(仅含有空格)则返回true,反之false
     */
    public static boolean isAnyNotBlank(CharSequence... css) {
        return !isAllBlank(css);
    }
    
    /**
     * 判断传入字符串数组是否 <b><i>全部不等于</i></b> null或空字符串(仅含有空格)
     * <pre>
     * isAllNotBlank(null)             = false
     * isAllNotBlank(null, "   ")      = false
     * isAllNotBlank(null, null)       = false
     * isAllNotBlank("", null)         = false
     * isAllNotBlank("bob", "")        = false
     * isAllNotBlank("  bob  ", null)  = false
     * isAllNotBlank(" ", "bar")       = false
     * isAllNotBlank("foo", "bar")     = true
     * </pre>
     * @param css {@code CharSequence[]}
     * @return - 若传入参数全部不等于null或空字符串(仅含有空格)则返回true,反之false
     */
    public static boolean isAllNotBlank(CharSequence... css) {
        return !isAnyBlank(css);
    }
    
    /**
     * 判断字符串是否是一个数字
     * @param cs CharSequence
     * @return - 若是数字则返回true,反之false
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) return false;
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 将字符串的首写字符由大写改为小写,例子如下:
     * <pre>
     * decapitalize(null) : null
     * decapitalize("") : ""
     * decapitalize("   ") : "   "
     * decapitalize("FooBar") : "fooBar"
     * decapitalize("fooBar") : "fooBar"
     * decapitalize("X") : "x"
     * decapitalize("x") : "x"
     * decapitalize("URL") : "url"
     * decapitalize("URLParser") : "urlParser"
     * </pre>
     * @param cs CharSequence
     * @return String
     */
    public static String decapitalize(CharSequence cs) {
        if (cs == null) {
            return null;
        } else if (cs.length() == 0) {
            return cs.toString();
        }
        int n = 0;
        int len = cs.length();
        for (int i = 0; i < len; i++) {
            if (Character.isUpperCase(cs.charAt(i)) && i == n) {
                n++;
                continue;
            }
            break;
        }
        
        if (n == 1) {
            return cs.toString();
        } else if (n > 1) {
            n = n < len ? n - 1 : n;
        }
        char chars[] = cs.toString().toCharArray();
        for (int i = 0; i < n; i++) {
            chars[i] = Character.toLowerCase(chars[i]);
        }
        return new String(chars);
    }
    
    public static int countMatch(String text, String search) {
        if (text == null || search == null) {
            return -1;
        }
        return countMatch(text, search, 0);
    }
    
    private static int countMatch(String text, String search, int count) {
        int pos = text.indexOf(search);
        if (pos != Index_Not_Found) {
            return countMatch(text.substring(pos + search.length()), search, ++count);
        }
        return count;
    }
    
    /**
     * 字符串格式化.此方法是对
     * {@link String#format(String, Object...)}方法的包装
     * @see {@link String#format(String, Object...)}
     * @param format String
     * @param args {@code Object[]}
     * @return String
     */
    public static String stringFormat(String format, Object... args) {
        return String.format(format, args);
    }
    
    /**
     * 字符串拼接
     * @param args {@code CharSequence[]}
     * @return String
     */
    public static String stringJoin(final CharSequence... args) {
        return stringJoinWith(EMP, args);
    }
    
    /**
     * 字符串拼接
     * @param args {@code Object[]}
     * @return String
     */
    public static String stringJoin(final Object... args) {
        return stringJoinWith(EMP, args);
    }
    
    /**
     * 字符串拼接
     * @param iterable {@code Iterable<?>}
     * @return String
     */
    public static String stringJoin(Iterable<?> iterable) {
        return stringJoinWith(EMP, iterable);
    }
    
    /**
     * 字符串拼接.使用指定分隔符(separator)拼接字符串
     * @param separator CharSequence
     * @param args {@code CharSequence[]}
     * @return String
     * @throws NullPointerException 当separator为null时抛出
     */
    public static String stringJoinWith(CharSequence separator, final CharSequence... args) {
        return String.join(separator, args);
    }
    
    /**
     * 字符串拼接.使用指定分隔符(separator)拼接字符串
     * @param separator CharSequence
     * @param args {@code Object[]}
     * @return String
     * @throws NullPointerException 当separator为null时抛出
     */
    public static String stringJoinWith(CharSequence separrator, final Object... args) {
        requireNotNull(separrator);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                builder.append(separrator);
            }
            if (args[i] != null) {
                builder.append(args[i]);
            }
        }
        return builder.toString();
    }
    
    /**
     * 字符串拼接.使用指定分隔符(separator)拼接字符串
     * @param separator CharSequence
     * @param iterable {@code Iterable<?>}
     * @return String
     * @throws NullPointerException 当separator为null时抛出
     */
    public static String stringJoinWith(CharSequence separrator, Iterable<?> iterable) {
        requireNotNull(separrator);
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = iterable.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i > 0) {
                builder.append(separrator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                builder.append(obj);
            }
            i++;
        }
        return builder.toString();
    }
    
    /**
     * 
     * @param text
     * @param search
     * @param replacement
     * @return
     */
    public static String stringReplace(String text, String search, String replacement) {
        return stringReplace(text, search, replacement, -1, false);
    }
    
    public static String stringReplaceFirst(String text, String search, String replacement) {
        return stringReplaceFirst(text, search, replacement, 1);
    }
    
    public static String stringReplaceFirst(String text, String search, String replacement, int count) {
        return stringReplace(text, search, replacement, count, false);
    }
    
    /**
     * 字符串替换
     */
    private static String stringReplace(String text, String search
            , String replacement, int count, boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(search) 
                || replacement == null || count == 0) {
            return text;
        }
        String target = text;
        if (ignoreCase) {
            target = text.toLowerCase();
            search = search.toLowerCase();
        }
        int start = 0;
        int end = target.indexOf(search, start);
        if (end == Index_Not_Found) {
            return text;
        }
        final int replLength = search.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= count < 0 ? 16 : count > 64 ? 64 : count;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != Index_Not_Found) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--count == 0) {
                break;
            }
            end = target.indexOf(search, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
    
    /**
     * 
     * @param cs
     * @param startIndex
     * @return
     */
    public static String substring(CharSequence cs, int startIndex) {
        return cs == null ? null : substring(cs, startIndex, cs.length());
    }
    
    /**
     * 
     * @param cs
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public static String substring(CharSequence cs, int beginIndex, int endIndex) {
        if (cs == null) {
            return null;
        }
        int len = cs.length();
        if (beginIndex < 0) {
            beginIndex = len + beginIndex;
        }
        if (endIndex < 0) {
            endIndex = len + endIndex;
        }
        if (endIndex > len) {
            endIndex = len;
        }
        if (beginIndex > endIndex) {
            return EMP;
        }
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        return cs.toString().substring(beginIndex, endIndex);
    }
    
    /**
     * 
     * @param cs
     * @param len
     * @return
     */
    public static String substringLeft(CharSequence cs, int len) {
        if (cs == null) {
            return null;
        } else if (len <= 0) {
            return EMP;
        } else if (len >= cs.length()) {
            return cs.toString();
        }
        return cs.toString().substring(0, len);
    }
    
    /**
     * 
     * @param cs
     * @param len
     * @return
     */
    public static String substringRight(CharSequence cs, int len) {
        if (cs == null) {
            return null;
        } else if (len <= 0) {
            return EMP;
        } else if (len >= cs.length()) {
            return cs.toString();
        }
        return cs.toString().substring(cs.length() - len);
    }
    
    /**
     * 
     * @param cs
     * @param separator
     * @return
     */
    public static String substringBefore(CharSequence cs, CharSequence separator) {
        if (cs == null) {
            return null;
        } else if (isEmpty(cs) 
                || isEmpty(separator)) {
            return EMP;
        }
        String t = cs.toString();
        int p = t.indexOf(separator.toString());
        if (p != Index_Not_Found) {
            t = t.substring(0, p);
        }
        return t;
    }
    
    /**
     * 
     * @param cs
     * @param separator
     * @return
     */
    public static String substringAfter(CharSequence cs, CharSequence separator) {
        if (cs == null) {
            return null;
        } else if (isEmpty(cs) 
                || isEmpty(separator)) {
            return EMP;
        }
        String t = cs.toString();
        int p = t.indexOf(separator.toString());
        if (p != Index_Not_Found) {
            t = t.substring(p + 1);
        }
        return t;
    }

    /**
     * 
     * @param cs
     * @param separator
     * @return
     */
    public static String substringBeforeLast(CharSequence cs, CharSequence separator) {
        if (cs == null) {
            return null;
        } else if (isEmpty(cs) 
                || isEmpty(separator)) {
            return EMP;
        }
        String t = cs.toString();
        int p = t.lastIndexOf(separator.toString());
        if (p != Index_Not_Found) {
            t = t.substring(0, p);
        }
        return t;
    }
    
    /**
     * 
     * @param text
     * @param separator
     * @return
     */
    public static String substringAfterLast(CharSequence text, CharSequence separator) {
        if (text == null) {
            return null;
        } else if (isEmpty(text) || isEmpty(separator)) {
            return EMP;
        }
        String t = text.toString();
        int p = t.lastIndexOf(separator.toString());
        if (p != Index_Not_Found) {
            t = t.substring(p + 1);
        }
        return t;
    }
    
    /**
     * 字符串截取<br>
     * 截取匹配两字符串的中间字串,并返回,且只返回从左到右的首次匹配值.
     * @param str 字符串
     * @param open 待截取的子串之前的字符串,可为null 
     * @param close 待截取的子串之后的字符串,可为null
     * @return 截取的字符串(若无匹配则返回null)
     */
    public static String substringBetween(String str, String open, String close) {
        String subStr = null;
        if (str != null && open != null && close != null) {
            int openIndex = str.indexOf(open);
            if (openIndex != -1) {
                openIndex += open.length();
                int closeIndex = str.indexOf(close, openIndex);
                if (closeIndex != -1) {
                    subStr = str.substring(openIndex, closeIndex);
                }
            }
        }
        return subStr;
    }
    
    /**
     * 
     * @param ex
     * @return
     */
    public static String toString(Throwable ex) {
        try (StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw)) {
            ex.printStackTrace(pw);
            return sw.toString();
        } catch (IOException e) {
            throw IO.newIORuntimeException(e);
        }
    }
    
    /**-------------------断言-------------------**/
    
    /**
     * 断言对象等于null
     * @param obj 断言对象
     * @return 若断言成立则返回断言对象
     * @throws IllegalArgumentException 若断言不成立,则抛出该异常
     */
    public static <T> T requireNull(T obj) {
        return requireNotNull(obj, "input object require to be null");
    }
    
    /**
     * 断言对象等于null
     * @param obj 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws IllegalArgumentException 若断言不成立,则抛出该异常
     */
    public static <T> T requireNull(T obj, String message) {
        if (obj != null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }
    
    /**
     * 断言对象不等于null
     * @param obj 断言对象
     * @return 若断言成立则返回断言对象
     * @throws IllegalArgumentException 若断言不成立,则抛出该异常
     */
    public static <T> T requireNotNull(T obj) {
        return requireNotNull(obj, "input object require not to be null");
    }
    
    /**
     * 断言对象不等于null
     * @param obj 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws NullPointerException 若断言不成立,则抛出该异常
     */
    public static <T> T requireNotNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }
    
    /**
     * 断言字符串不等于null且不等于""
     * @param cs 断言对象
     * @return 若断言成立则返回断言对象
     * @throws BlankCharSequenceException 若断言不成立,则抛出该异常
     */
    public static <T extends CharSequence> T requireNotEmpty(T cs) {
        return requireNotEmpty(cs, "input charsequence require not be null or empty");
    }
    
    /**
     * 断言字符串不等于null且不等于""
     * @param cs 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws BlankCharSequenceException 若断言不成立,则抛出该异常
     */
    public static <T extends CharSequence> T requireNotEmpty(T cs, String message) {
        if (isEmpty(cs)) {
            throw new BlankCharSequenceException(message);
        }
        return cs;
    }
    
    /**
     * 断言字符串不等于null且不是空字符串
     * @param cs 断言对象
     * @return 若断言成立则返回断言对象
     * @throws BlankCharSequenceException 若断言不成立,则抛出该异常
     */
    public static <T extends CharSequence> T requireNotBlank(T cs) {
        return requireNotBlank(cs, "input charsequence require not be null or blank");
    }
    
    /**
     * 断言字符串不等于null且不是空字符串
     * @param cs 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws BlankCharSequenceException 若断言不成立,则抛出该异常
     */
    public static <T extends CharSequence> T requireNotBlank(T cs, String message) {
        if (isBlank(cs)) {
            throw new BlankCharSequenceException(message);
        }
        return cs;
    }
    
    /**
     * 断言数组不等于null且length > 0
     * @param a 断言对象
     * @return 若断言成立则返回断言对象
     * @throws EmptyCollectionException 若断言不成立,则抛出该异常
     */
    public static <T> T[] requireNotEmpty(T[] a) {
        return requireNotEmpty(a, "input array require not be null or empty");
    }
    
    /**
     * 断言数组不等于null且length > 0
     * @param a 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws EmptyCollectionException 若断言不成立,则抛出该异常
     */
    public static <T> T[] requireNotEmpty(T[] a, String message) {
        if (isEmpty(a)) {
            throw new EmptyCollectionException(message);
        }
        return a;
    }
    
    /**
     * 断言集合不等于null且size > 0
     * @param c 断言对象
     * @return 若断言成立则返回断言对象
     * @throws EmptyCollectionException 若断言不成立,则抛出该异常
     */
    public static <C extends Collection<?>> C requireNotEmpty(C c) {
        return requireNotEmpty(c, "input collection require not be null or empty");
    }
    
    /**
     * 断言集合不等于null且size > 0
     * @param c 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws EmptyCollectionException 若断言不成立,则抛出该异常
     */
    public static <C extends Collection<?>> C requireNotEmpty(C c, String message) {
        if (isEmpty(c)) {
            throw new EmptyCollectionException(message);
        }
        return c;
    }
    
    /**
     * 断言Map不等于null且size > 0
     * @param m 断言对象
     * @return 若断言成立则返回断言对象
     * @throws EmptyCollectionException 若断言不成立,则抛出该异常
     */
    public static <M extends Map<?, ?>> M requireNotEmpty(M map) {
        return requireNotEmpty(map, "input map require not be null or empty");
    }
    
    /**
     * 断言Map不等于null且size > 0
     * @param m 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws EmptyCollectionException 若断言不成立,则抛出该异常
     */
    public static <M extends Map<?, ?>> M requireNotEmpty(M map, String message) {
        if (isEmpty(map)) {
            throw new EmptyCollectionException(message);
        }
        return map;
    }
    
    /**
     * 断言expression等于false
     * @param expression 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws IllegalArgumentException 若断言不成立,则抛出该异常
     */
    public static void requireFalse(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * 断言expression等于true
     * @param expression 断言对象
     * @param message 断言不成立时的异常信息
     * @return 若断言成立则返回断言对象
     * @throws IllegalArgumentException 若断言不成立,则抛出该异常
     */
    public static void requireTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    
    
    /**
     * 空字符串警告异常(字符串为null或空串)
     * @author zhaoqingjiang
     */
    public static class BlankCharSequenceException extends RuntimeException {

        private static final long serialVersionUID = -479895746434262540L;

        public BlankCharSequenceException() {
            super();
        }
        
        public BlankCharSequenceException(String message) {
            super(message);
        }

        public BlankCharSequenceException(String message, Object... args) {
            super(message, args);
        }

    }
    
    
    /**
     * 空集合警告异常
     * @author zhaoqingjiang
     */
    public static class EmptyCollectionException extends RuntimeException {

        private static final long serialVersionUID = -5910822621584176391L;

        public EmptyCollectionException() {
            super();
        }
        
        public EmptyCollectionException(String message) {
            super(message);
        }

        public EmptyCollectionException(String message, Object... args) {
            super(message, args);
        }

    }
    
    /**-------------------Class-------------------**/
    
    private static final Map<String, Class<?>> namePrimitiveMap = newHashMap();
    static {
        namePrimitiveMap.put("boolean", Boolean.TYPE);
        namePrimitiveMap.put("byte", Byte.TYPE);
        namePrimitiveMap.put("char", Character.TYPE);
        namePrimitiveMap.put("short", Short.TYPE);
        namePrimitiveMap.put("int", Integer.TYPE);
        namePrimitiveMap.put("long", Long.TYPE);
        namePrimitiveMap.put("double", Double.TYPE);
        namePrimitiveMap.put("float", Float.TYPE);
        namePrimitiveMap.put("void", Void.TYPE);
    }
    private static final Map<String, Class<?>> nameWrapperMap = newHashMap();
    static {
        nameWrapperMap.put(Boolean.class.getSimpleName(), Boolean.class);
        nameWrapperMap.put(Byte.class.getSimpleName(), Byte.class);
        nameWrapperMap.put(Character.class.getSimpleName(), Character.class);
        nameWrapperMap.put(Short.class.getSimpleName(), Short.class);
        nameWrapperMap.put(Integer.class.getSimpleName(), Integer.class);
        nameWrapperMap.put(Long.class.getSimpleName(), Long.class);
        nameWrapperMap.put(Double.class.getSimpleName(), Double.class);
        nameWrapperMap.put(Float.class.getSimpleName(), Float.class);
        nameWrapperMap.put(Void.class.getSimpleName(), Void.class);
    }
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = newHashMap();
    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = newHashMap();
    static {
        primitiveWrapperMap.forEach((k, v) -> {
            if (k.equals(v)) {
                wrapperPrimitiveMap.put(v, k);
            }
        });
    }
    
    /**
     * 基础类型 转 包装类型
     * <pre>
     * 若cls等于null, 则返回null
     * 若cls不等于null但不是基础类型, 则返回其本身
     * 若cls不等于null且是基础类型, 则返回其对应的包装类型Class
     * </pre>
     * @param cls {@code Class<?>}
     * @return {@code Class<?>}
     */
    public static Class<?> primitiveToWrapper(Class<?> cls) {
        if (cls != null && cls.isPrimitive()) {
            return primitiveWrapperMap.get(cls);
        }
        return cls;
    }
    
    /**
     * 包装类型 转 基础类型
     * <pre>
     * 若cls等于null或不是包装类型, 则返回null
     * 若cls不等于null且是包装类型, 则返回其对应的基础类型Class
     * </pre>
     * @param cls {@code Class<?>}
     * @return {@code Class<?>}
     */
    public static Class<?> wrapperToPrimitive(Class<?> cls) {
        return wrapperPrimitiveMap.get(cls);
    }
    
    private static final Map<String, Class<?>> nameClassMap = newConcurrentHashMap();
    static {
        nameClassMap.putAll(namePrimitiveMap);
        nameClassMap.putAll(nameWrapperMap);
        nameClassMap.put(String.class.getSimpleName(), String.class);
        nameClassMap.put(BigDecimal.class.getSimpleName(), BigDecimal.class);
        nameClassMap.put(Date.class.getSimpleName(), Date.class);
        nameClassMap.put(LocalDateTime.class.getSimpleName(), LocalDateTime.class);
        nameClassMap.put(LocalDate.class.getSimpleName(), LocalDate.class);
        nameClassMap.put(LocalTime.class.getSimpleName(), LocalTime.class);
    }
    
    /**
     * 根据类名称获取对应的Class
     * @param className 类名称
     * @return {@code Class<?>}
     */
    public static Class<?> findClass(String className) {
        if (isBlank(className)) return null;
        Class<?> cls = nameClassMap.get(className);
        if (cls == null) {
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(
                        String.format("Class not found: %s", className), e);
            }
            nameClassMap.put(className, cls);
        }
        return cls;
    }
    
/**-------------------数组-------------------**/
    
    /**
     * 判断传入数组是否等于null或长度等于0
     * @param arr T[]
     * @return - 若传入数组等于null或长度等于0则返回true,反之false
     */
    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }
    
    /**
     * 判断传入数组是否不等于null且长度不等于0
     * @param arr T[]
     * @return - 若传入数组不等于null且长度不等于0则返回true,反之false
     */
    public static <T> boolean isNotEmpty(T[] arr) {
        return !isEmpty(arr);
    }
    
    /**
     * 判断字符数组内是否包含指定字符
     * @param cs 字符数组
     * @param c 字符
     * @return - 若包含则返回true,反之false
     */
    public static boolean contains(char[] cs, char c) {
        for (char e : cs) {
            if (e == c) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断数组内是否包含指定元素
     * @param as A[] 数组
     * @param a A 元素
     * @return - 若包含元素则返回true,反之false
     */
    public static <A> boolean contains(A[] as, A a) {
        if (Comparable.class.isInstance(a)) {
            return Arrays.binarySearch(as, a, null) >= 0;
        }
        for (A e : as) {
            if (isEqual(e, a)) return true;
        }
        return false;
    }
    
    /**
     * 判断数组是否包含元素
     * @param as A[] 数组
     * @param a A 元素
     * @param c {@link Comparator} 比较器
     * @return - 若包含元素则返回true,反之false
     */
    public static <A> boolean contains(A[] as, A a, Comparator<A> c) {
        return Arrays.binarySearch(as, a, c) >= 0;
    }
    
    /**
     * 判断数组a内是否包含指定元素b
     * @param as A[] 数组
     * @param b B 元素
     * @param c {@link Comparater} 比较器
     * @return - 若包含元素则返回true,反之false
     */
    public static <A, B> boolean contains(A[] as, B b, Comparater<A, B> c) {
        return binarySearch0(as, 0, as.length, b, c) >= 0;
    }
    
    /**
     * 对象比较接口
     * @author ZhaoQingJiang
     */
    @FunctionalInterface
    public interface Comparater<A, B> {int compare(A a, B b);}
    
    /**
     * 使用Comparater接口对数组进行二分查找
     */
    private static <A, B> int binarySearch0(A[] a, int fromIndex, int toIndex, B k, Comparater<A, B> c) {
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            A midVal = a[mid];
            int cmp = c.compare(midVal, k);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1); // key not found.
    }
    
    /**
     * 对象数组转换为{@link List}列表.
     * 此方法是对{@link Arrays#asList(Object...)}方法的包装.
     * @see Arrays#asList(Object...)
     * @see Arrays.ArrayList
     * @param a 对象数组
     * @return {@code List<T>}
     */
    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }
    
    /**
     * 将字节数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 字节数组
     * @return {@code List<>}
     */
    public static List<Byte> toList(byte[] a) {
        List<Byte> list = newArrayList(a.length);
        for (byte e : a) {list.add(e);}
        return list;
    }
    
    /**
     * 将字符数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 字符数组
     * @return {@code List<Character>}
     */
    public static List<Character> toList(char[] a) {
        List<Character> list = newArrayList(a.length);
        for (char e : a) {list.add(e);}
        return list;
    }
    
    /**
     * 将短整形数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 短整形数组
     * @return {@code List<Short>}
     */
    public static List<Short> toList(short[] a) {
        List<Short> list = newArrayList(a.length);
        for (short e : a) {list.add(e);}
        return list;
    }
    
    /**
     * 将整形数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 整形数组
     * @return {@code List<Integer>}
     */
    public static List<Integer> toList(int[] a) {
        return Arrays.stream(a)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toList());
    }
    
    /**
     * 将长整形数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 长整形数组
     * @return {@code List<Long>}
     */
    public static List<Long> toList(long[] a) {
        return Arrays.stream(a)
                .mapToObj(Long::valueOf)
                .collect(Collectors.toList());
    }
    
    /**
     * 将单精浮点数数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 单精浮点数数组
     * @return {@code List<Float>}
     */
    public static List<Float> toList(float[] a) {
        List<Float> list = newArrayList(a.length);
        for (float e : a) {list.add(e);}
        return list;
    }
    
    /**
     * 将双精浮点数数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 双精浮点数数组
     * @return {@code List<Double>}
     */
    public static List<Double> toList(double[] a) {
        return Arrays.stream(a)
                .mapToObj(Double::valueOf)
                .collect(Collectors.toList());
    }
    
    /**
     * 将对象数组转换为{@link List}列表.
     * 返回列表类型是{@link ArrayList}
     * @param a 对象数组
     * @return {@code List<T>}
     */
    @SafeVarargs
    public static <T> List<T> toList(T... a) {
        return newArrayList(a);
    }
    
    /**
     * 将字符数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 字符数组
     * @return {@code Set<Character>}
     */
    public static Set<Character> toSet(char[] a) {
        Set<Character> set = newHashSet(a.length);
        for (char c : a) {set.add(c);}
        return set;
    }
    
    /**
     * 将短整形数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 短整形数组
     * @return {@code Set<Short>}
     */
    public static Set<Short> toSet(short[] a) {
        Set<Short> set = newHashSet(a.length);
        for (short c : a) {set.add(c);}
        return set;
    }
    
    /**
     * 将整形数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 整形数组
     * @return {@code Set<Integer>}
     */
    public static Set<Integer> toSet(int[] a) {
        return Arrays.stream(a)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toSet());
    }
    
    /**
     * 将长整形数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 长整形数组
     * @return {@code Set<Long>}
     */
    public static Set<Long> toSet(long[] a) {
        return Arrays.stream(a)
                .mapToObj(Long::valueOf)
                .collect(Collectors.toSet());
    }
    
    /**
     * 将单精浮点数数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 单精浮点数数组
     * @return {@code Set<Float>}
     */
    public static Set<Float> toSet(float[] a) {
        Set<Float> set = newHashSet(a.length);
        for (float c : a) {set.add(c);}
        return set;
    }
    
    /**
     * 将双精浮点数数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 双精浮点数数组
     * @return {@code Set<Double>}
     */
    public static Set<Double> toSet(double[] a) {
        return Arrays.stream(a)
                .mapToObj(Double::valueOf)
                .collect(Collectors.toSet());
    }
    
    /**
     * 将对象数组转换为{@link Set}集合.
     * 返回集合类型是{@link HashSet}
     * @param a 对象数组
     * @return {@code Set<T>}
     */
    @SafeVarargs
    public static <T> Set<T> toSet(T... a) {
        return newHashSet(a);
    }
    
    /**
     * 数组拷贝
     * @param a A[]
     * @param bType {@code Class<B>}
     * @param mapper {@link Function}
     * @return B[]
     */
    public static <A, B> B[] copyOf(A[] a, Class<? extends B[]> bType, Function<A, B> mapper) {
        int len = a.length;
        B[] b = newArray(bType, len);
        for (int i = 0; i < len; i++) {
            b[i] = mapper.apply(a[i]);
        }
        return b;
    }
    
    /**
     * 数组拷贝
     * @param a A[] 源数组对象
     * @param newType {@code Class<? extends B[]>} 目标数组类型
     * @return B[] - 若a==null则返回null,否则返回目标数组对象
     * @throws TypeConvertException 若转换数组元素时出现错误,则抛出该异常
     */
    @SuppressWarnings({"unchecked"})
    public static <A, B> B[] copyOf(A[] a, Class<? extends B[]> newType) throws TypeConvertException {
        return copyOf(a, (Class<A>) a.getClass().getComponentType(), newType);
    }
    
    /**
     * 数组拷贝
     * @param a A[] 源数组对象
     * @param aType {@code Class<A>} 源数组元素类型
     * @param newType {@code Class<? extends B[]>} 目标数组类型
     * @return B[] - 若a==null则返回null,否则返回目标数组对象
     * @throws TypeConvertException 若转换数组元素时出现错误,则抛出该异常
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <A, B> B[] copyOf(A[] a, Class<A> aType, Class<? extends B[]> newType) throws TypeConvertException {
        if (a == null) return null;
        int length = a.length;
        Class<?> bType = newType.getComponentType();
        B[] copy = ((Object)newType == (Object)Object[].class)
                ? (B[]) new Object[length]
                : (B[]) Array.newInstance(newType.getComponentType(), length);
        if ((Object)newType == (Object)Object[].class
                || bType.isAssignableFrom(aType)) {
            System.arraycopy(a, 0, copy, 0, length);
        } else {
            TypeConverter c = TypeConverterFactory.getTypeConverter(aType, bType);
            for (int i = 0; i < length; i++) {
                copy[i] = (B) c.convert(a[i]);
            }
        }
        return copy;
    }
    
    /**------------------Collection------------------**/
    
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }
    
    public static <T> List<T> newArrayList(int capacity) {
        return new ArrayList<>(capacity);
    }
    
    @SafeVarargs
    public static <T> List<T> newArrayList(T... array) {
        return newArrayList(asList(array));
    }
    
    public static <T> List<T> newArrayList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(t -> list.add(t));
        return list;
    }
    
    public static <T> List<T> newArrayList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }
    
    public static <T> List<T> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<>();
    }
    
    public static <T> List<T> newCopyOnWriteArrayList(T[] array) {
        return new CopyOnWriteArrayList<>(array);
    }
    
    public static <T> List<T> newCopyOnWriteArrayList(Collection<T> collection) {
        return new CopyOnWriteArrayList<>(collection);
    }
    
    public static <T> List<T> newLinkedList() {
        return new LinkedList<>();
    }
    
    @SafeVarargs
    public static <T> List<T> newLinkedList(T... array) {
        requireNotNull(array);
        List<T> list = new LinkedList<>();
        Stream.of(array).forEach(e -> list.add(e));
        return list;
    }
    
    public static <T> List<T> newLinkedList(Collection<T> collection) {
        return new LinkedList<>(collection);
    }
    
    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }
    
    public static <T> Set<T> newHashSet(int capacity) {
        return new HashSet<>(capacity);
    }
    
    @SafeVarargs
    public static <T> Set<T> newHashSet(T... array) {
        requireNotNull(array);
        Set<T> set = newHashSet(Math.max((int) (array.length/.75f) + 1, 16));
        Stream.of(array).forEach(e -> set.add(e));
        return set;
    }
    
    public static <T> Set<T> newHashSet(Collection<T> collection) {
        return new HashSet<>(collection);
    }
    
    public static <T> BlockingQueue<T> newArrayBlockingQueue(int capacity) {
        return new ArrayBlockingQueue<>(capacity);
    }
    
    
    /**
     * 
     * @return
     */
    public static <T> List<T> unmodifiableEmptyList() {
        return Collections.emptyList();
    }
    
    /**
     * 
     * @return
     */
    public static <T> Set<T> unmodifiableEmptySet() {
        return Collections.emptySet();
    }
    
    /**
     * 
     * @param array
     * @return 
     */
    @SafeVarargs
    public static <T> List<T> newUnmodifiableList(T... array) {
        return Collections.unmodifiableList(Arrays.asList(array));
    }
    
    /**
     * 
     * @param array
     * @return
     */
    @SafeVarargs
    public static <T> Set<T> newUnmodifiableSet(T... array) {
        return Collections.unmodifiableSet(newHashSet(array));
    }
    
    /**
     * 
     * @param <T>
     * @param list
     * @return
     */
    public static <T> List<T> newUnmodifiableList(List<T> list) {
        return Collections.unmodifiableList(list);
    }
    
    /**
     * 
     * @param set
     * @return
     */
    public static <T> Set<T> newUnmodifiableSet(Set<T> set) {
        return Collections.unmodifiableSet(set);
    }
    
    /**
     * 
     * @param collection
     * @return
     */
    public static <T> List<T> newUnmodifiableList(Collection<T> collection) {
        return Collections.unmodifiableList(newArrayList(collection));
    }
    
    /**
     * 
     * @param collection
     * @return
     */
    public static <T> Set<T> newUnmodifiableSet(Collection<T> collection) {
        return Collections.unmodifiableSet(newHashSet(collection));
    }
    
    /**
     * 集合转换为数组
     * @param collection 集合
     * @param type 数组类型
     * @return 数组
     */
    public static <T> T[] toArray(Collection<T> collection, Class<T[]> type) {
        return collection.toArray(newArray(type, collection.size()));
    }
    
    
    /**------------------Map------------------**/
    
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }
    
    public static <K, V> Map<K, V> newHashMap(int elementCount) {
        requireTrue(elementCount > 0, "elementCount must greater than 0");
        int c = (int) (elementCount / 0.75 + 1);
        return new HashMap<>(c);
    }
    
    public static <K, V> Map<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<>(map);
    }
    
    public static <V> Map<String, V> newCaseInsensitiveHashMap() {
        return new CaseInsensitiveHashMap<>();
    }
    
    public static <K, V> Map<K, V> newTreeMap() {
        return new TreeMap<>();
    }
    
    public static <K, V> Map<K, V> newTreeMap(Map<? extends K, ? extends V> map) {
        return new TreeMap<>(map);
    }
    
    public static <K, V> Map<K, V> newTreeMap(Comparator<? super K> comparator) {
        return new TreeMap<>(comparator);
    }
    
    public static <K, V> Map<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }
    
    public static <K, V> Map<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    } 
    
    public static <V> Map<String, V> newCaseInsensitiveLinkedHashMap() {
        return new CaseInsensitiveLinkedHashMap<V>();
    }
    
    public static <V> Map<String, V> newCaseInsensitiveConcurrentHashMap() {
        return new CaseInsensitiveConcurrentHashMap<>();
    }
    
    public static <K, V> Map<K, V> newUnmodifiedMap(Map<K, V> map) {
        return Collections.unmodifiableMap(map);
    }
    
    
    /**------------------Pair------------------**/
    
    /**
     * 判断键值对{@link Pair}中的键(key)是否为null
     * @param pair 键值对
     * @return boolean
     */
    public static <K, V> boolean isNullKey(Pair<K, V> pair) {
        return pair != null && null == pair.getKey();
    }
    
    /**
     * 判断键值对{@link Pair}中的键对应的值(value)是否为null
     * @param pair 键值对
     * @return boolean
     */
    public static <K, V> boolean isNullValue(Pair<K, V> pair) {
        return pair != null && null == pair.getValue();
    }
    
    /**
     * 创建一个键值对{@link Pair}实例
     * @see DefaultPair
     * @return {@code Pair<K, V>}
     */
    public static <K, V> Pair<K, V> newPair() {
        return new DefaultPair<>();
    }
    
    /**
     * 创建一个键值对{@link Pair}实例
     * @see DefaultPair
     * @param key 键的值
     * @param value 键对应的值
     * @return {@code Pair<K, V>}
     */
    public static <K, V> Pair<K, V> newPair(K key, V value) {
        return new DefaultPair<>(key, value);
    }
    
    /**
     * 创建一个键值对{@link Pair}实例
     * @see DefaultPair
     * @param pair 另一个键值对
     * @return {@code Pair<K, V>}
     */
    public static <K, V> Pair<K, V> newPair(Pair<K, V> pair) {
        return new DefaultPair<>(pair.getKey(), pair.getValue());
    }
    
    /**
     * 创建一个不可修改的键值对{@link Pair}实例
     * @see UnmodifiablePair
     * @param key 键的值
     * @param value 键对应的值
     * @return {@code Pair<K, V>}
     */
    public static <K, V> Pair<K, V> newUnmodifiablePair(K key, V value) {
        return new UnmodifiablePair<>(key, value);
    }
    
    /**
     * 创建一个不可修改的键值对{@link Pair}实例
     * @see UnmodifiablePair
     * @param pair 另一个键值对
     * @return {@code Pair<K, V>}
     */
    public static <K, V> Pair<K, V> newUnmodifiablePair(Pair<K, V> pair) {
        return new UnmodifiablePair<>(pair.getKey(), pair.getValue());
    }
}
