package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.contains;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.newHashMap;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.substringBeforeLast;
import static com.kxindot.goblin.Resources.isJarFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.JarURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class工具类
 * @author ZhaoQingJiang
 */
public final class Classes {
	
	/** 空的Class数组(长度为0) */
    public static final Class<?>[] EMPTY_CLS_ARRAY = new Class<?>[0];
    /** 包分隔符: . */
    public static final String Package_Separator = ".";
    /** 路径分隔符: / */
    public static final String Path_Separator = "/";
    /** 内部类分隔符: $ */
    public static final String Inner_Class_Separator = "$";
    /** Cglib代理类分隔符: $$ */
    public static final String Cglib_Class_Separator = "$$";
    /** Java字节码文件后缀: .class */
    public static final String Java_Class_Extension = ".class";
    /** Java源代码文件后缀: .java */
    public static final String Java_Source_Extension = ".java";
    /** 数组后缀: [] */
    public static final String Array_Suffix = "[]";
    /** 内部数组前缀: [ */
    public static final String Internal_Array_Prefix = "[";
    /** 非基础数组前缀: [L */
    public static final String Non_Primitive_Array_Prefix = "[L";
    /** 正则.符号转义: \\. */
    public static final String ESC_Regex_Dot = "\\.";
    /** java.lang包 */
    public static final String java_lang = "java.lang";
    /**
     * 数组类名前缀字符
     */
    private static final String Array_Name_Prefix = "[";
    /**
     * Element Type        Encoding
     * class or interface  Lclassname;
     */
    private static final char Object_Array_Encoding = 'L';
    /**
     * <pre>
     * Element Type        Encoding
     * boolean                Z
     * byte                   B
     * char                   C
     * double                 D
     * float                  F
     * int                    I
     * long                   J
     * short                  S
     * </pre>
     */
    private static final char[] Primitive_Array_Encodings = new char[] {'Z', 'B', 'C', 'D', 'F', 'I', 'J', 'S'}; 
    
    /** 映射: 基础数据类型-基础数据包装类型 */
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
    
    /** 映射: 基础数据包装类型-基础数据类型 */
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = newHashMap();

    /** 映射: 基础类型名称-基础类型 */
    private static final Map<String, Class<?>> namePrimitiveMap = newHashMap();
    
    /** 映射: 常用类型名称-常用类型  */
    private static final Map<String, Class<?>> nameCommonClassMap = newHashMap();
    
    static {
        primitiveWrapperMap.forEach((k, v) -> {
            if (!k.equals(v)) {
                wrapperPrimitiveMap.put(v, k);
                nameCommonClassMap.put(v.getName(), v);
                nameCommonClassMap.put(v.getSimpleName(), v);
            }
        });
        
        Set<Class<?>> set = newHashSet(boolean[].class, byte[].class, 
                char[].class, short[].class, int[].class, long[].class, 
                double[].class, float[].class);
        set.addAll(primitiveWrapperMap.keySet());
        set.forEach(e -> namePrimitiveMap.put(e.getName(), e));
        
        set = newHashSet(Boolean[].class, Byte[].class, Character[].class, Short[].class, 
                Integer[].class, Long[].class, Double[].class, Float[].class, 
                Number.class, Number[].class, String.class, String[].class,
                Object.class, Object[].class, Class.class, Class[].class, 
                Throwable.class, Exception.class, RuntimeException.class,
                Error.class, StackTraceElement.class, StackTraceElement[].class,
                BigDecimal.class, BigDecimal[].class, Date.class, 
                LocalDate.class, LocalTime.class, LocalDateTime.class);
        set.forEach(e -> nameCommonClassMap.put(e.getName(), e));
    }
    
    
    /**
     * 判断当前进程是否在JAR包中运行
     * @return boolean
     */
    public static boolean isPackaged() {
        return isPackaged(getAvailableClassLoader());
    }
    
    /**
     * 判断当前进程是否在JAR包中运行
     * @param classLoader ClassLoader
     * @return boolean
     */
    public static boolean isPackaged(ClassLoader classLoader) {
        return isJarFile(classLoader.getResource(Path_Separator));
    }
    
    /**
     * 判断是否基础数据类型
     * @param cls {@code Class<?>}
     * @return boolean
     */
    public static boolean isPrimitive(Class<?> cls) {
        return cls != null && cls.isPrimitive();
    }
    
    /**
     * 判断是否基础数据类型或包装类型
     * @param cls {@code Class<?>}
     * @return boolean
     */
    public static boolean isPrimitiveOrWrapper(Class<?> cls) {
        return cls != null && (cls.isPrimitive() || isPrimitiveWrapper(cls));
    }
    
    /**
     * 判断是否基础数据包装类型
     * @param cls {@code Class<?>}
     * @return boolean
     */
    public static boolean isPrimitiveWrapper(Class<?> cls) {
        return wrapperPrimitiveMap.containsKey(cls);
    }
    
    /**
     * 判断是否基础数据类型数组
     * @param cls 类型
     * @return boolean
     */
    public static boolean isPrimitiveArray(Class<?> cls) {
        return cls == null ? false : cls.isArray() 
                ? isPrimitiveArray(cls.getComponentType()) : cls.isPrimitive();
    }
    
    /**
     * 判断类名是否基础数据类型数组
     * @param className 全类名
     * @return boolean
     */
    public static boolean isPrimitiveArrayName(String className) {
        if (isBlank(className)) return false;
        if (className.startsWith(Array_Name_Prefix)) {
            int i = className.lastIndexOf(Array_Name_Prefix);
            return i + 2 == className.length() && contains(Primitive_Array_Encodings, className.charAt(++i));
        }
        return false;
    }
    
    public static boolean isGeneric(Class<?> cls) {
        return cls.getTypeParameters().length > 0;
    }
    
    /**
     * 判断类是否归属于java.lang包
     * @param cls Class
     * @return boolean
     */
    public static boolean isJavaLang(Class<?> cls) {
        return cls == null ? false : cls.isArray() 
                ? isJavaLang(cls.getComponentType()) : isJavaLang(cls.getName(), false);
    }
    
    /**
     * 判断类是否归属于java.lang包(不含其子包)
     * @param className 类名
     * @return boolean
     */
    public static boolean isJavaLang(String className) {
        return isJavaLang(className, true);
    }
    
    /**
     * 判断类是否归属于java.lang包(不含其子包)
     */
    private static boolean isJavaLang(String className, boolean check) {
        if (isBlank(className)) return false;
        if (className.startsWith(Array_Name_Prefix)) {
            int index = className.lastIndexOf(Array_Name_Prefix);
            if (index < className.length()) {
                char c = className.charAt(++index);
                if (c == Object_Array_Encoding && index < className.length()) {
                    return isJavaLang(className.substring(++index));
                }
            }
        } else if (className.startsWith(java_lang)) {
            return check ? exists(className) : true;
        } else if (!className.contains(Package_Separator)) {
            return exists(String.join(Package_Separator, java_lang, className));
        }
        return false;
    }
    
    
    /**
     * 判断目标对象是否是来源类型或其子类型.
     * 
     * @param source 来源类型
     * @param target 目标对象
     * @return boolean 
     */
    public static boolean isAssignableFrom(Class<?> source, Object target) {
    	return isAssignableFrom(source, target.getClass());
    }
    
    
    /**
     * 判断目标类型是否是来源类型或其子类型.
     * 
     * @param source 来源类型
     * @param target 目标类型
     * @return boolean 
     */
    public static boolean isAssignableFrom(Class<?> source, Class<?> target) {
    	return source.isAssignableFrom(target);
    }
    
    
    /**
     * 判断目标对象是否来源类型的实例.
     * 
     * @param source 来源类型
     * @param target 目标对象
     * @return boolean
     */
    public static boolean isInstance(Class<?> source, Object target) {
    	return source.isInstance(target);
    }
    
    
    /**
     * 判断类是否存在(是否存在于类加载器中)
     * @param className 全类名
     * @return boolean
     */
    public static boolean exists(String className) {
        if (isBlank(className)) return false;
        try {
            loadClass(className);
            return true;
        } catch (IllegalArgumentException e) {
            // ignore
        }
        return false;
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
    
    
    /**
     * 获取线程上下文类加载器.若无则返回null
     * @return ClassLoader
     */
    public static ClassLoader getThreadContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    /**
     * 获取可用的类加载器.优先级: 
     * <pre>
     * 1. 线程上下文类加载器;
     * 2. 本类{@link Classes}的类加载器;
     * 3. 系统类加载器;
     * 4. 以上皆无,返回null;
     * </pre>
     * @return ClassLoader
     */
    public static ClassLoader getAvailableClassLoader() {
        ClassLoader loader = null;
        try {
            loader = getThreadContextClassLoader();
        } catch (Throwable e) {
            // ignore this
        }
        if (loader == null) {
            loader = Classes.class.getClassLoader();
            if (loader == null) {
                try {
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Throwable e) {
                    // ignore this
                }
            }
        }
        return loader;
    }
    
    
    /**
     * 判断类Class是否内部类
     * @param cls 类Class
     * @return boolean
     */
    public static boolean isInnerClass(Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
    }
    
    /**
     * 判断类Class是否接口(interface)
     * @param cls 类Class
     * @return boolean
     */
    public static boolean isInterface(Class<?> cls) {
        return cls != null && Modifier.isInterface(cls.getModifiers());
    }
    
    /**
     * 判断类Class是否抽象类
     * @param cls 类Class
     * @return boolean
     */
    public static boolean isAbstractClass(Class<?> cls) {
        return cls != null && Modifier.isAbstract(cls.getModifiers());
    }
    
    /**
     * 判断类Class是否接口(interface)或是抽象类
     * @param cls 类Class
     * @return boolean
     */
    public static boolean isInterfaceOrAbstractClass(Class<?> cls) {
        return isInterface(cls) || isAbstractClass(cls);
    }
    
    /**
     * 将类名转化为路径模式,如com.kxindot.goblin.Test转化为com/kxindot/goblin/Test
     * <pre>
     * toPathPattern("com.kxindot.goblin.TestClass")    =>  com/kxindot/goblin/TestClass
     * toPathPattern("com.kxindot.goblin.TestClass.java")    =>  com/kxindot/goblin/TestClass.java
     * toPathPattern("com.kxindot.goblin.TestClass.class")    =>  com/kxindot/goblin/TestClass.class
     * toPathPattern("com.kxindot.goblin.TestClass.html")    =>  com/kxindot/goblin/TestClass/html
     * toPathPattern("com.kxindot.goblin.TestClass.txt")    =>  com/kxindot/goblin/TestClass/txt
     * toPathPattern("TestClass.java")    =>  TestClass.java
     * toPathPattern("TestClass.class")    =>  TestClass.class
     * toPathPattern("TestClass.html")    =>  TestClass/html
     * toPathPattern("TestClass.txt")    =>  TestClass/txt
     * toPathPattern("TestClass")    =>  TestClass
     * </pre>
     * @param className 类名或类文件名
     * @return String
     */
    public static String toPathPattern(String className) {
        String ext = EMP;
        String name = className;
        if (name.endsWith(Java_Class_Extension)) {
            ext = Java_Class_Extension;
            name = name.substring(0, name.lastIndexOf(ext));
        } else if (name.endsWith(Java_Source_Extension)) {
            ext = Java_Source_Extension;
            name = name.substring(0, name.lastIndexOf(ext));
        }
        return name.replaceAll(ESC_Regex_Dot, Path_Separator) + ext;
    }
    
    /**
     * 将路径名转化为包名模式,如com/kxindot/goblin/Test转化为com.kxindot.goblin.Test
     * @param path String
     * @return String
     */
    public static String toPackagePattern(String path) {
        return path.replaceAll(Path_Separator, Package_Separator);
    }
    
    /**
     * 获取类所在包名
     * @param cls {@code Class<?>}
     * @return String
     */
    public static String getPackageName(Class<?> cls) {
        return getPackageName(cls.getName());
    }
    
    /**
     * 根据全类名获取类所在包名
     * @param className 全类名
     * @return String
     */
    public static String getPackageName(String className) {
        requireNotBlank(className);
        String clsName = className;
        if (clsName.contains(Path_Separator)) {
            clsName = toPackagePattern(clsName);
        }
        return substringBeforeLast(clsName, Package_Separator);
    }
    
    /**
     * 加载已被初始化的类Class对象
     * @param className 类名称
     * @return 类Class对象
     * @throws IllegalArgumentException 当类无法检索到时,抛出此异常
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true, null);
    }
    
    /**
     * 加载类Class对象
     * @param className 类名称
     * @param initialize 是否初始化
     * @return 类Class对象
     * @throws IllegalArgumentException 当类无法检索到时,抛出此异常
     */
    public static Class<?> loadClass(String className, boolean initialize) {
        return loadClass(className, initialize, null);
    }
    
    /**
     * 加载已被初始化的类Class对象
     * @param className 类名称
     * @param loader 类加载器
     * @return 类Class对象
     * @throws IllegalArgumentException 当类无法检索到时,抛出此异常
     */
    public static Class<?> loadClass(String className, ClassLoader loader) {
        return loadClass(className, true, loader);
    }
    
    /**
     * 加载类Class对象
     * @param className 类名称
     * @param initialize 是否初始化
     * @param loader 类加载器
     * @return 类Class对象
     * @throws IllegalArgumentException 当类无法检索到时,抛出此异常
     */
    public static Class<?> loadClass(String className, boolean initialize, ClassLoader loader) {
        try {
            return forName(className, initialize, loader);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't find class : " + className);
        }
    }
    
    /**
     * 加载类Class对象.与loadClass方法的区别是: 
     * loadClass方法捕获了{@link ClassNotFoundException}异常,取而代之抛出了一个未检异常
     * {@link IllegalArgumentException}
     * @see #loadClass(String)
     * @see #loadClass(String, boolean)
     * @see #loadClass(String, ClassLoader)
     * @see #loadClass(String, boolean, ClassLoader)
     * @param className 类名称
     * @param initialize 是否初始化
     * @param loader 类加载器
     * @return 类Class对象
     * @throws ClassNotFoundException 当类无法检索到时,抛出此异常
     */
    public static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        requireNotNull(className, "className == null");
        Class<?> cls = null;
        if (className.length() <= 8) cls = namePrimitiveMap.get(className);
        if (cls == null) cls = nameCommonClassMap.get(className);
        if (cls != null) return cls;
        
        // 例如: "java.lang.String[]"
        if (className.endsWith(Array_Suffix)) {
            className = className.substring(0, className.length() - Array_Suffix.length());
            cls = forName(className, initialize, loader);
            return Array.newInstance(cls, 0).getClass();
        }
        
        // 例如: "[L.lang.String;"
        if (className.startsWith(Non_Primitive_Array_Prefix) && className.endsWith(";")) {
            className = className.substring(Non_Primitive_Array_Prefix.length(), className.length() - 1);
            cls = forName(className, initialize, loader);
            return Array.newInstance(cls, 0).getClass();
        }
        
        // 例如: "[[I" 或 "[[Ljava.lang.String;"
        if (className.startsWith(Internal_Array_Prefix)) {
            className = className.substring(Internal_Array_Prefix.length());
            cls = forName(className, initialize, loader);
            return Array.newInstance(cls, 0).getClass();
        }
        
        if (loader == null) loader = getAvailableClassLoader();
        try {
            return !initialize && loader != null ? loader.loadClass(className) 
                    : Class.forName(className, initialize, loader);
        } catch (ClassNotFoundException e1) {
            int index = className.lastIndexOf(Package_Separator);
            if (index != -1) {
                className = String.join(Inner_Class_Separator, 
                        className.substring(0, index), className.substring(index + 1));
                try {
                    return !initialize && loader != null ? loader.loadClass(className) 
                            : Class.forName(className, initialize, loader); 
                } catch (ClassNotFoundException e2) {
                    // 忽略
                }
            }
            throw e1;
        }
    }
    
    
    /**
     * 类Class过滤器
     * @author ZhaoQingJiang
     */
    @FunctionalInterface
    public interface ClassFilter {
        /**
         * @param pkg 类Class所在包
         * @param cls 类Class
         * @return 若为false,对应的类Class则被丢弃
         */
        boolean accept(String pkg, Class<?> cls);
        
        /**
         * {@link ClassFilter#accept(String, Class)}方法默认返回true.内部使用.
         */
        ClassFilter defaultClassFilter = new ClassFilter() {
            @Override
            public boolean accept(String pkg, Class<?> cls) {
                return true;
            }
        };
    }
    
    /**
     * 加载包下所有类Class对象(默认初始化)
     * @param pkgName 包名
     * @return 类Class对象集合 - 若无类Class对象则返回length==0的空集合
     * @throws IOException 加载类资源时出现错误,则抛出此异常
     */
    public static Set<Class<?>> loadClasses(String pkgName) throws IOException {
        return loadClasses(pkgName, true, ClassFilter.defaultClassFilter);
    }
    
    /**
     * 加载包下类Class对象(默认初始化)
     * @param pkgName 包名
     * @param filter 类Class过滤器 - {@link ClassFilter}
     * @return 类Class对象集合 - 若无类Class对象则返回length==0的空集合
     * @throws IOException 加载类资源时出现错误,则抛出此异常
     */
    public static Set<Class<?>> loadClasses(String pkgName, ClassFilter filter) throws IOException {
        return loadClasses(pkgName, true, filter);
    }
    
    /**
     * 加载包下所有类Class对象
     * @param pkgName 包名
     * @param initialize 是否初始化
     * @return 类Class对象集合 - 若无类Class对象则返回length==0的空集合
     * @throws IOException 加载类资源时出现错误,则抛出此异常
     */
    public static Set<Class<?>> loadClasses(String pkgName, boolean initialize) throws IOException {
        return loadClasses(pkgName, initialize, ClassFilter.defaultClassFilter);
    }
    
    /**
     * 加载包下类Class对象
     * @param pkgName 包名
     * @param initialize 是否初始化
     * @param filter 类Class过滤器 - {@link ClassFilter}
     * @return 类Class对象集合 - 若无类Class对象则返回length==0的空集合
     * @throws IOException 加载类资源时出现错误,则抛出此异常
     */
    public static Set<Class<?>> loadClasses(String pkgName, boolean initialize, ClassFilter filter) throws IOException {
        requireNotNull(filter, "filter == null");
        Set<Class<?>> set = new HashSet<>();
        String path = pkgName.replaceAll("\\.", "/");
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(path);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                loadClass(set, url.getPath().replaceAll("%20", ""), initialize, filter);
            } else if ("jar".equals(protocol)) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                if (connection == null) continue;
                JarFile jarFile = connection.getJarFile();
                if (jarFile == null) continue;
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    String name = jarEntry.getName().replaceAll("/", Package_Separator);
                    if (name.endsWith(Java_Class_Extension)) {
                        set.add(loadClass(name.substring(0, name.lastIndexOf(Package_Separator)), initialize));
                    }
                }
            }
        }
        return set;
    }
    
    /**
     * 加载类Class并放入Set集合
     */
    private static void loadClass(Set<Class<?>> set, String path, boolean initialize, ClassFilter filter) {
        File[] files = new File(path).listFiles(f -> 
            (f.isFile() && f.getName().endsWith(Java_Class_Extension)) || f.isDirectory());
        if (files.length < 1) return;
        String pkgName = path.replaceAll("/", Package_Separator);
        for (File f : files) {
            String name = f.getName();
            if (f.isFile()) {
                loadClass(set, String.join(Package_Separator, pkgName, name.substring(0, name.lastIndexOf(Package_Separator))), pkgName, initialize, filter);
            } else {
                loadClass(set, String.join("/", path, name), initialize, filter);
            }
        }
    }
    
    /**
     * 加载类Class并放入Set集合
     */
    private static void loadClass(Set<Class<?>> set, String className, String pkgName, boolean initialize, ClassFilter filter) {
        Class<?> cls = loadClass(className, initialize);
        if (filter.accept(pkgName, cls)) set.add(cls);
    }
    
}
