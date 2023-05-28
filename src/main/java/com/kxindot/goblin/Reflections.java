package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.asList;
import static com.kxindot.goblin.Objects.convert;
import static com.kxindot.goblin.Objects.copyOf;
import static com.kxindot.goblin.Objects.isAllNotNull;
import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Objects.unmodifiableEmptyList;
import static com.kxindot.goblin.Throws.threx;
import static java.util.Arrays.stream;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kxindot.goblin.typeconvert.TypeConvertException;

/**
 * @author ZhaoQingJiang
 */
public final class Reflections {
    
    /**
     * 空的Object数组(长度为0)
     */
    static final Object[] EMP_OBJ_ARR = new Object[0];

    /**
     * 空的Class数组(长度为0)
     */
    static final Class<?>[] EMP_CLS_ARR = new Class<?>[0];
    
    /**
     * 默认域过滤器,不过滤任何域.
     */
    public static final Filter<Field> ACCEPT_ALL_FIELD = (m, t) -> {return true;};
    
    /**
     * 默认方法过滤器,不过滤任何方法.
     */
    public static final Filter<Method> ACCEPT_ALL_METHOD = (m, t) -> {return true;};
    
    
    
    
    
    /**
     * 获取指定类的构造器对象.若构造器参数类型数组为null或长度等于0,则返回无参构造器.
     * 
     * @param <T>
     * @param cls Class 类
     * @param args Class[] 构造器参数类型数组
     * @return Constructor 构造器对象
     * @throws NullPointerException cls == null
     * @throws ReflectionException 当构造器不存在或不可访问时抛出
     */
    public static <T> Constructor<T> findConstructor(Class<T> cls, Class<?>... args) {
        try {
            return cls.getConstructor(args);
        } catch (NoSuchMethodException | SecurityException e) {
            StringBuilder b = new StringBuilder("构造器不存在或其修饰符非公有(public)或不可访问: ");
            b.append(cls.getSimpleName());
            b.append("(");
            if (isNotEmpty(args)) {
                b.append(stream(args).map(c -> c == null 
                        ? "TypeUnknown" : c.getSimpleName()).collect(Collectors.joining(",")));
            }
            b.append("),起因异常: ");
            b.append(e.getClass().getSimpleName());
            throw new ReflectionException(b.toString(), e);
        }
    }
    
    /**
     * 调用类无参构造器创建类实例.
     * 若需通过反射创建数组实例,请使用{@link #newArrayInstance(Class, int)}方法.
     * 
     * @param <T>
     * @param cls Class 类
     * @return T 类实例
     * @throws NullPointerException cls == null
     * @throws ReflectionException 当无参构造器不存在或不可访问时抛出
     */
    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectionException("反射创建" + cls.getSimpleName() 
                + "实例失败,起因异常: " + e.getClass().getSimpleName() + ",信息: " + e.getMessage(), e);
        }
    }
    
    /**
     * 调用指定类构造器创建类实例.
     * 
     * @param <T>
     * @param constructor Constructor 构造器对象
     * @param args Object[] 构造器参数数组
     * @return T 类实例
     * @throws NullPointerException constructor == null
     * @throws ReflectionException 若构造实例过程中出现问题则抛出
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ReflectionException("调用类构造器创建实例失败,起因异常: " 
                + e.getClass().getSimpleName() + ",信息: " + e.getMessage(), e);
        }
    }

    /**
     * 调用类构造器创建类实例.若需通过反射创建数组实例,请使用{@link #newArrayInstance(Class, int)}方法.
     * <pre>
     * 若args==null或args.length==0, 则调用{@link #newInstance(Class)}方法创建实例;
     * 若args不为空且其每个元素皆不等于null,则根据{@link #findConstructor(Class, Class...)}方法找出构造器并调用{@link #newInstance(Constructor, Object...)}方法创建实例;
     * 若args不为空且其中有元素等于null,则获取参数长度等于args.length的所有构造器,逐个尝试构造实例,若成功则返回实例,若失败则返回最后一次失败异常;
     * </pre>
     * 
     * @param <T>
     * @param cls Class 类
     * @param args Object[] 构造器参数数组
     * @return T 类实例
     * @throws NullPointerException cls == null
     * @throws ReflectionException 获取构造器或构造实例过程中出现问题则抛出
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T newInstance(Class<T> cls, Object... args) {
        if (isEmpty(args)) {
            return newInstance(cls);
        } else if (isAllNotNull(args)) {
            Class<?>[] types = copyOf(args, Class[].class, Object::getClass);
            return newInstance(findConstructor(cls, types), args);
        }
        int len = args.length;
        List<Constructor<?>> cs = stream(cls.getConstructors()).filter(e -> len == e.getParameterCount()).collect(Collectors.toList());
        if (isEmpty(cs)) {
            threx(ReflectionException::new, "类%s无参数长度等于%d的构造器", cls.getSimpleName(), len);
        }
        RuntimeException ex = null;
        for (Constructor constructor : cs) {
            try {
                return (T) newInstance(constructor, args);
            } catch (ReflectionException e) {
                ex = e;
            }
        }
        throw ex;
    }

    /**
     * 反射创建一个指定长度的数组实例.
     * 
     * @param cls Class 数组类
     * @param length int 数组长度
     * @return T[] 数组实例
     * @throws NullPointerException cls == null
     * @throws NegativeArraySizeException 若长度小于0则抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArrayInstance(Class<? extends T[]> cls, int length) {
        return (T[]) Array.newInstance(cls.getComponentType(), length);
    }
    
    /**
     * <p>获取指定名称的类或接口域对象.</p>
     * 当入参类型是接口时,若存在符合指定名称的常量域则返回,否则返回null.<br>
     * 当入参类型是类时,若存在符合指定名称的域(包含成员及静态域,其修饰符可能是public,protected,default,private)则返回,否则返回null.
     * 
     * @see #findField(Class, String, boolean)
     * @param cls Class 类
     * @param name String 域名称
     * @return Field 域对象
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取域对象过程中触发安全权限问题则抛出
     */
    public static Field findField(Class<?> cls, String name) {
        return findField(cls, name, false);
    }

    /**
     * <p>获取指定名称的类或接口域对象.</p>
     * 当入参类型是接口时,若存在符合指定名称的常量域则返回,若不存在且inherit=true,则会查询其继承的所有接口的常量域,若都不存在则返回null.<br>
     * 当入参类型是类时,若存在符合指定名称的域(包含成员及静态域,其修饰符可能是public,protected,default,private)则返回,
     * 若不存在且inherit=true,则会向上查找其超类的域,直到向上溯源到直接继承于{@link Object}的超类,若都不存在则返回null.
     * 
     * @see Class#getDeclaredField(String)
     * @param cls Class 类
     * @param name String 域名称
     * @param inherit 是否包含超类或继承接口的域对象
     * @return Field 域对象
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取域对象过程中触发安全权限问题则抛出
     */
    public static Field findField(Class<?> cls, String name, boolean inherit) {
        Field f = null;
        if (cls.isInterface()) {
            f = findClassField(cls, name);
            if (f == null && inherit) {
                Class<?>[] interfaces = cls.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    if ((f = findClassField(interfaces[i], name)) != null) {
                        break;
                    }
                }
            }
        } else {
            do {
                f = findClassField(cls, name);
            } while (f == null && inherit 
                    && (cls = cls.getSuperclass()) != null 
                    && cls != Object.class);
        }
        return f;
    }
    
    /**
     * <p>获取指定名称的类成员(member)域对象.</p>
     * 当入参类型是接口时,直接返回null.<br>
     * 当入参类型是类时,若存在符合指定名称的成员(member)域(其修饰符可能是public,protected,default,private)则返回,否则返回null.
     * 
     * @see #findMemeberField(Class, String, boolean)
     * @param cls Class 类
     * @param name String 域名称
     * @return Field 成员(member)域对象
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取域对象过程中触发安全权限问题则抛出
     */
    public static Field findMemeberField(Class<?> cls, String name) {
        return findMemeberField(cls, name, false);
    }

    /**
     * <p>获取指定名称的类成员(member)域对象.</p>
     * 当入参类型是接口时,直接返回null.<br>
     * 当入参类型是类时,若存在符合指定名称的成员(member)域(其修饰符可能是public,protected,default,private)则返回,
     * 若不存在且inherit=true,则会向上查找其超类的域,直到向上溯源到直接继承于{@link Object}的超类,若都不存在则返回null.
     * 
     * @see #findMemeberField(Class, String, boolean)
     * @param cls Class 类
     * @param name String 域名称
     * @param inherit 是否包含超类或继承接口的域对象
     * @return Field 成员(member)域对象
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取域对象过程中触发安全权限问题则抛出
     */
    public static Field findMemeberField(Class<?> cls, String name, boolean inherit) {
        Field f = cls.isInterface() ? null : findField(cls, name, inherit);
        return f == null ? null : Modifier.isStatic(f.getModifiers()) ? null : f;
    }

    
    /**
     * <p>获取类或接口的域对象列表.</p>
     * 当入参类型是接口时,包含其所有常量域对象.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)的域对象.<br>
     * 若无可用域对象,则返回长度等于0的空列表.
     * 
     * @see #listFields(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @return {@code List<Field>} 域对象列表
     * @throws NullPointerException cls==null
     */
    public static List<Field> listFields(Class<?> cls) {
        return listFields(cls, ACCEPT_ALL_FIELD, false);
    }

    /**
     * <p>获取类或接口的域对象列表.</p>
     * 当入参类型是接口时,包含其所有常量域对象.若此时inherit=true,则会将其继承的所有接口的常量域对象也包含其中.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)的域对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的域对象也将包含其中.<br>
     * 若无可用域对象,则返回长度等于0的空列表.
     * 
     * @see #listFields(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @param inherit 是否包含超类或继承接口的域对象
     * @return {@code List<Field>} 域对象列表
     * @throws NullPointerException cls==null
     */
    public static List<Field> listFields(Class<?> cls, boolean inherit) {
        return listFields(cls, ACCEPT_ALL_FIELD, inherit);
    }
    
    /**
     * <p>获取类或接口的域对象列表.</p>
     * 当入参类型是接口时,包含其所有常量域对象.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)的域对象.<br><br>
     * 传入的{@link Filter}对象可以根据条件过滤返回的结果集.若无可用域对象,则返回长度等于0的空列表.
     * 
     * @see #listFields(Class, Filter, boolean)
     * @see #ACCEPT_ALL_FIELD
     * @see Filter
     * @param cls Class 类或接口
     * @return {@code List<Field>} 域对象列表
     * @throws NullPointerException cls==null或filter==null
     */
    public static List<Field> listFields(Class<?> cls, Filter<Field> filter) {
        return listFields(cls, filter, false);
    }

    /**
     * <p>获取类或接口的域对象列表.</p>
     * 当入参类型是接口时,包含其所有常量域对象.若此时inherit=true,则会将其继承的所有接口的常量域对象也包含其中.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)的域对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的域对象也将包含其中.<br><br>
     * 传入的{@link Filter}对象可以根据条件过滤返回的结果集.若无可用域对象,则返回长度等于0的空列表.
     * 
     * @see #ACCEPT_ALL_FIELD
     * @see Filter
     * @param cls Class 类或接口
     * @param filter {@code Filter<Field>} 域对象过滤器
     * @param inherit 是否包含超类或继承接口的域对象
     * @return {@code List<Field>} 域对象列表
     * @throws NullPointerException cls==null或filter==null
     */
    public static List<Field> listFields(Class<?> cls, Filter<Field> filter, boolean inherit) {
        List<Field> list = null;
        if (cls.isInterface()) {
            list = newArrayList(cls.getDeclaredFields());
            if (inherit) {
                Class<?>[] interfaces = cls.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    list.addAll(asList(interfaces[i].getDeclaredFields()));
                }
            }
        } else {
            list = newArrayList();
            do {
                list.addAll(asList(cls.getDeclaredFields()));
            } while (inherit && (cls = cls.getSuperclass()) != null && cls != Object.class);
        }
        return list.stream().filter(e -> filter.accept(e.getModifiers(), e)).collect(Collectors.toList());
    }
    
    /**
     * <p>获取类的成员(member)域对象列表.</p>
     * 当入参类型是接口时,返回长度等于0的空列表.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)的域对象.<br><br>
     * 若无可用成员域对象,则返回长度等于0的空列表.
     * 
     * @see #listMemberFields(Class, Filter, boolean)
     * @param cls Class 类
     * @param inherit 是否包含超类的成员域对象
     * @return {@code List<Field>} 成员域对象列表
     * @throws NullPointerException cls==null或filter==null
     */
    public static List<Field> listMemberFields(Class<?> cls) {
        return listMemberFields(cls, ACCEPT_ALL_FIELD, false);
    }
    
    /**
     * <p>获取类的成员(member)域对象列表.</p>
     * 当入参类型是接口时,返回长度等于0的空列表.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)的域对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的域对象也将包含其中.<br><br>
     * 若无可用成员域对象,则返回长度等于0的空列表.
     * 
     * @see #listMemberFields(Class, Filter, boolean)
     * @param cls Class 类
     * @param inherit 是否包含超类的成员域对象
     * @return {@code List<Field>} 成员域对象列表
     * @throws NullPointerException cls==null或filter==null
     */
    public static List<Field> listMemberFields(Class<?> cls, boolean inherit) {
        return listMemberFields(cls, ACCEPT_ALL_FIELD, inherit);
    }
    
    /**
     * <p>获取类的成员(member)域对象列表.</p>
     * 当入参类型是接口时,返回长度等于0的空列表.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)的域对象.<br><br>
     * 传入的{@link Filter}对象可以根据条件过滤返回的结果集.若无可用成员域对象,则返回长度等于0的空列表.
     * 
     * @see #listMemberFields(Class, Filter, boolean)
     * @see #ACCEPT_ALL_FIELD
     * @see Filter
     * @param cls Class 类
     * @param filter {@code Filter<Field>} 域对象过滤器
     * @return {@code List<Field>} 成员域对象列表
     * @throws NullPointerException cls==null或filter==null
     */
    public static List<Field> listMemberFields(Class<?> cls, Filter<Field> filter) {
        return listMemberFields(cls, filter, false);
    }
    
    /**
     * <p>获取类的成员(member)域对象列表.</p>
     * 当入参类型是接口时,返回长度等于0的空列表.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)的域对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的域对象也将包含其中.<br><br>
     * 传入的{@link Filter}对象可以根据条件过滤返回的结果集.若无可用成员域对象,则返回长度等于0的空列表.
     * 
     * @see #listFields(Class, Filter, boolean)
     * @see #ACCEPT_ALL_FIELD
     * @see Filter
     * @param cls Class 类
     * @param filter {@code Filter<Field>} 域对象过滤器
     * @param inherit 是否包含超类的成员域对象
     * @return {@code List<Field>} 成员域对象列表
     * @throws NullPointerException cls==null或filter==null
     */
    public static List<Field> listMemberFields(Class<?> cls, Filter<Field> filter, boolean inherit) {
        return cls.isInterface() ? unmodifiableEmptyList() 
                : listFields(cls, (m, t) -> !Modifier.isStatic(m) && filter.accept(m, t), inherit);
    }
    
    /**
     *<p>获取指定名称的类或接口方法对象.</p>
     * 当入参类型是接口时,若存在符合指定名称的抽象方法或默认(default)方法则返回,否则返回null.<br>
     * 当入参类型是类时,若存在符合指定名称的方法(包含成员及静态方法,其修饰符可能是public,protected,default,private)则返回,否则返回null.
     * 
     * @see #findMethod(Class, String, boolean, Class...)
     * @param cls Class 类或接口
     * @param name String 方法名称
     * @param Class[] 方法入参类型数组
     * @return Method 方法对象
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static Method findMethod(Class<?> cls, String name, Class<?>... params) {
        return findMethod(cls, name, false, params);
    }
    
    /**
     * <p>获取指定名称的类或接口方法对象.</p>
     * 当入参类型是接口时,若存在符合指定名称的抽象方法或默认(default)方法则返回,若不存在且inherit=true,则会查询其继承的所有接口的方法,若都不存在则返回null.<br>
     * 当入参类型是类时,若存在符合指定名称的方法(包含成员及静态方法,其修饰符可能是public,protected,default,private)则返回,
     * 若不存在且inherit=true,则会向上查找其超类的方法,直到向上溯源到直接继承于{@link Object}的超类,若都不存在则返回null.
     * 
     * @see Class#getDeclaredMethod(String, Class...)
     * @param cls Class 类或接口
     * @param name String 方法名称
     * @param inherit 是否包含超类或继承接口的方法对象
     * @param Class[] 方法入参类型数组
     * @return Method 方法对象
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static Method findMethod(Class<?> cls, String name, boolean inherit, Class<?>... params) {
        Method m = null;
        if (cls.isInterface()) {
            m = findClassMethod(cls, name, params);
            if (m == null && inherit) {
                Class<?>[] interfaces = cls.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    m = findClassMethod(interfaces[i], name, params);
                    if (m != null) {
                        break;
                    }
                }
            }
        } else {
            do {
                m = findClassMethod(cls, name, params);
            } while (m == null && inherit 
                    && (cls = cls.getSuperclass()) != null 
                    && cls != Object.class);
        }
        return m;
    }
    
    /**
     * <p>获取指定名称的类或接口方法对象列表(同一名称多个方法原因: 方法重写).</p>
     * 当入参类型是接口时,包含符合指定名称的抽象方法或默认(default)方法.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)方法对象.<br><br>
     * 若无对应方法对象,则返回长度等于0的空列表.
     * 
     * @see #listMethodsByName(Class, String, boolean)
     * @param cls Class 类或接口
     * @param name String 方法名称
     * @return {@code List<Method>} 方法列表
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static List<Method> listMethodsByName(Class<?> cls, String name) {
        return listMethodsByName(cls, name, false);
    }
    
    /**
     * <p>获取指定名称的类或接口方法对象列表(同一名称多个方法原因: 方法重写,方法重载).</p>
     * 当入参类型是接口时,包含符合指定名称的抽象方法或默认(default)方法.若此时inherit=true,则会将其继承的所有接口的方法对象也包含其中.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)方法对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的方法对象也将包含其中.<br><br>
     * 若无对应方法对象,则返回长度等于0的空列表.
     * 
     * @see #listMethods(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @param name String 方法名称
     * @param inherit 是否包含超类或继承接口的方法对象
     * @return {@code List<Method>} 方法列表
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static List<Method> listMethodsByName(Class<?> cls, String name, boolean inherit) {
        return listMethods(cls, (m, t) -> name.equals(t.getName()), inherit);
    }
    
    /**
     * <p>获取类或接口方法对象列表.</p>
     * 当入参类型是接口时,包含符合指定名称的抽象方法或默认(default)方法.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)方法对象.<br><br>
     * 若无方法对象,则返回长度等于0的空列表.
     * 
     * @see #listMethods(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @return {@code List<Method>} 方法列表
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static List<Method> listMethods(Class<?> cls) {
        return listMethods(cls, ACCEPT_ALL_METHOD, false);
    }
    
    /**
     * <p>获取类或接口方法对象列表.</p>
     * 当入参类型是接口时,包含符合指定名称的抽象方法或默认(default)方法.若此时inherit=true,则会将其继承的所有接口的方法对象也包含其中.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)方法对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的方法对象也将包含其中.<br><br>
     * 若无方法对象,则返回长度等于0的空列表.
     * 
     * @see #listMethods(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @param inherit 是否包含超类或继承接口的方法对象
     * @return {@code List<Method>} 方法列表
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static List<Method> listMethods(Class<?> cls, boolean inherit) {
        return listMethods(cls, ACCEPT_ALL_METHOD, inherit);
    }
    
    /**
     * <p>获取类或接口方法对象列表.</p>
     * 当入参类型是接口时,包含符合指定名称的抽象方法或默认(default)方法.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)方法对象.<br><br>
     * 传入的{@link Filter}对象可以根据条件过滤返回的结果集.若无方法对象,则返回长度等于0的空列表.
     * 
     * @see #listMethods(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @param filter {@code Filter<Method>} 方法对象过滤器
     * @return {@code List<Method>} 方法列表
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static List<Method> listMethods(Class<?> cls, Filter<Method> filter) {
        return listMethods(cls, filter, false);
    }
    
    /**
     * <p>获取类或接口方法对象列表.</p>
     * 当入参类型是接口时,包含符合指定名称的抽象方法或默认(default)方法.若此时inherit=true,则会将其继承的所有接口的方法对象也包含其中.<br>
     * 当入参类型是类时,包含所有被private,default,protected,public修饰的成员(member)及静态(static)方法对象.
     * 若此时inherit=true,并且其直接父类不是{@link Object},则继承链上的所有超类的方法对象也将包含其中.<br><br>
     * 若无方法对象,则返回长度等于0的空列表.
     * 
     * @see #listMethods(Class, Filter, boolean)
     * @param cls Class 类或接口
     * @param filter {@code Filter<Method>} 方法对象过滤器
     * @param inherit 是否包含超类或继承接口的方法对象
     * @return {@code List<Method>} 方法列表
     * @throws NullPointerException cls==null或name==null
     * @throws SecurityException 若在获取方法对象过程中触发安全权限问题则抛出
     */
    public static List<Method> listMethods(Class<?> cls, Filter<Method> filter, boolean inherit) {
        List<Method> list = null;
        if (cls.isInterface()) {
            list = newArrayList(cls.getDeclaredMethods());
            if (inherit) {
                Class<?>[] interfaces = cls.getInterfaces();
                for (Class<?> inter : interfaces) {
                    list.addAll(asList(inter.getDeclaredMethods()));
                }
            }
        } else {
            list = newArrayList();
            do {
                list.addAll(asList(cls.getDeclaredMethods()));
            } while (inherit && (cls = cls.getSuperclass()) != null && cls != Object.class);
        }
        return list.stream().filter(e -> filter.accept(e.getModifiers(), e)).collect(Collectors.toList());
    }

    
    public static Method findGetter(Class<?> cls, String name) {
        PropertyDescriptor pd = findPropertyDescriptor(cls, name);
        return pd == null ? null : pd.getReadMethod();
    }

    
    public static Method findSetter(Class<?> cls, String name) {
        PropertyDescriptor pd = findPropertyDescriptor(cls, name);
        return pd == null ? null : pd.getWriteMethod();
    }

    
    public static PropertyDescriptor findPropertyDescriptor(Class<?> cls, String name) {
        return listPropertyDescriptors(cls).stream()
                .filter(e -> name.equals(e.getName())).findFirst().orElse(null);
    }

    
    public static List<PropertyDescriptor> listPropertyDescriptors(Class<?> cls) {
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(cls);
        } catch (IntrospectionException e) {
            throw new ReflectionException("获取类" + cls.getSimpleName() + "信息失败", e);
        }
        return newArrayList(info.getPropertyDescriptors());
    }
    
    
    public static Class<?> findGenericParameterType(Class<?> cls, int index) {
        Class<?> scls = cls.getSuperclass();
        if (scls == Object.class)

            requireTrue(index >= 0, "不合法的下标值: %d", index);
        requireTrue(scls != Object.class, "%s没有继承的父类", cls.getSimpleName());
        int len = scls.getTypeParameters().length;
        requireTrue(len > 0, "%s的父类%s不是泛型类", cls.getSimpleName(), scls.getSimpleName());
        requireTrue(len >= index, null, EMP_OBJ_ARR);
        Type st = cls.getGenericSuperclass();
        requireTrue(st != null && st instanceof ParameterizedType, "类%s之父类不是泛型类!", cls.getSimpleName());

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <C extends P, P> Class<?> findGenericParameterType(Class<C> source, Class<P> target, int index) {
        requireTrue(index > -1, "Invalid index value : " + index);
        if (isNull(target))
            target = (Class<P>) source.getSuperclass();
        int len = target.getTypeParameters().length;
        if (len == 0)
            throw new IllegalArgumentException(target.getName() + " is not a generic class or interface");
        if (index >= len)
            throw new IndexOutOfBoundsException(
                    target.getName() + " generic parameter len: " + len + ", index: " + index);
        Type t = target.isInterface() ? findExactGenericInterfaceType(source, target)
                : findExactGenericClassType(source, target);
        return Class.class.cast(ParameterizedType.class.cast(t).getActualTypeArguments()[index]);
    }

    private static Type findExactGenericClassType(Class<?> source, Class<?> target) {
        Type t = source.getGenericSuperclass();
        return ParameterizedType.class.cast(t).getRawType() == target ? t
                : findExactGenericClassType(Class.class.cast(t), target);
    }

    /**
     * find generic type
     */
    private static Type findExactGenericInterfaceType(Class<?> source, Class<?> target) {
        Type[] ts = source.getGenericInterfaces();
        if (isEmpty(ts)) {
            return findExactGenericInterfaceType(source.getSuperclass(), target);
        }
        Type t = Stream.of(ts)
                .filter(e -> e instanceof ParameterizedType && ParameterizedType.class.cast(e).getRawType() == target)
                .findFirst().orElse(null);
        if (t == null) {
            t = Stream.of(ts)
                    .filter(e -> (!(e instanceof ParameterizedType)) && target.isAssignableFrom(Class.class.cast(e)))
                    .findFirst().get();
            return findExactGenericInterfaceType(Class.class.cast(t), target);
        }
        return t;
    }

    
    public static Object getValue(Object obj, Field field) {
        return null;
    }

    
    public static Object getValue(Object obj, Field field, Object deVal) {
        return null;
    }

    
    public static <T> T getValue(Object obj, Field field, Class<T> type) {
        return null;
    }

    
    public static <T> T getValue(Object obj, Field field, Class<T> type, T deVal) {
        return null;
    }
    
    
    public static void setValue(Object obj, Field field, Object val) {
        
    }

    
    public static Object invoke(Object obj, Method method, Object... args) {
        return null;
    }
    
    
    public static Object invokeGetter(Object obj, String name) {
        
        return null;
    }
    
    public static Object invokeSetter(Object obj, String name, Object val) {
        
        return null;
    }

    public static Object smartInvoke(Object obj, Method method, Object... args) {
        if (args == null)
            args = EMP_OBJ_ARR;
        int len = args.length;
        if (len != method.getParameterCount()) {
//            throw new ReflectionException("Method parameter count not match, require %d, input %d",
//                    method.getParameterCount(), len);
        }
        try {
            return invoke(obj, method, args);
        } catch (ReflectionException e) {
            if (!IllegalArgumentException.class.isInstance(e.getCause())) {
                // not argument type miss match
                throw e;
            }
            Object[] sargs = new Object[len];
            System.arraycopy(args, 0, sargs, 0, len);
            Class<?>[] types = method.getParameterTypes();
            try {
                for (int i = 0; i < len; i++) {
                    if (!types[i].isInstance(sargs[i])) {
                        sargs[i] = convert(sargs[i], types[i]);
                    }
                }
                return invoke(obj, method, sargs);
            } catch (TypeConvertException ex) {
                // type convert failed, throw original
                throw e;
            } catch (ReflectionException ex) {
                // InvocationTargetException would be the cause
                throw ex;
            }
        }
    }

    /**
     * 开放类域访问权限
     * 
     * @param field {@link Field}
     */
    public static void makeAccessiable(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 开放方法访问权限
     * 
     * @param method {@link Method}
     */
    public static void makeAccessiable(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
    }
    
    /**
     * 获取类或接口域对象
     */
    private static Field findClassField(Class<?> cls, String name) {
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    
    /**
     * 获取类或接口方法对象
     */
    private static Method findClassMethod(Class<?> cls, String name, Class<?>... params) {
        try {
            return cls.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    
    
    /**
     * <p>域或方法对象过滤器</p>
     * 
     * @author ZhaoQingJiang
     */
    @FunctionalInterface
    public interface Filter<T> {
        /**
         * 返回false则会过滤掉此对象
         * 
         * @param mod int 修饰符值
         * @param target T 待过滤对象
         * @return boolean 
         */
        boolean accept(int mod, T target);
    }

    
    /**
     * <p>反射异常类</p>
     * 
     * @author ZhaoQingJiang
     */
    static class ReflectionException extends RuntimeException {

        private static final long serialVersionUID = 212321960269231201L;

        public ReflectionException(String message, Throwable cause) {
            super(message, cause);
        }

        public ReflectionException(String message) {
            super(message);
        }
    }

}
