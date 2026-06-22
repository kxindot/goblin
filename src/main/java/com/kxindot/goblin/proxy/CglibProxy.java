package com.kxindot.goblin.proxy;

import static com.kxindot.goblin.Classes.isInterfaceOrAbstractClass;
import static com.kxindot.goblin.Objects.requireNotNull;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.proxy.Enhancer;

/**
 * 代理工具。
 * 
 * @author ZhaoQingJiang
 */
public final class CglibProxy {

    private static final NamingPolicy POLICY = new NamingPolicy();
    
    /**
     * 获取代理对象的实际类型。
     * 
     * @param proxy Object
     * @return {@code Class<?>}
     */
    public static Class<?> getActualType(Object proxy) {
    	Class<?> type = proxy.getClass();
        if (type.toString().contains(NamingPolicy.IDENTIFIER)) {
            type = type.getSuperclass();
        }
        return type;
    }
    
    /**
     * 创建指定类的代理对象
     * @param type {@code Class<?>} 待代理类
     * @param helper CallbackHelper 代理回调
     * @return Object 代理对象
     */
    public static Object enhance(Class<?> type, CallbackHelper helper) {
        return enhance(type, helper, new Class<?>[] {}, new Object[] {});
    }
    
    /**
     * 创建指定类的代理对象
     * @param type {@code Class<?>} 待代理类
     * @param helper CallbackHelper 代理回调
     * @param argTypes {@code Class<?>[]} 待代理类的构造器入参类型
     * @param args {@code Object[]} 待代理类的构造器入参
     * @return Object 代理对象
     */
    public static Object enhance(Class<?> type, CallbackHelper helper, Class<?>[] argTypes, Object[] args) {
        requireNotNull(type);
        if (isInterfaceOrAbstractClass(type)) {
            throw new IllegalArgumentException("can't enhance interface or abstract class : " + type.getName());
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallbackFilter(helper);
        enhancer.setCallbacks(helper.getCallbacks());
        enhancer.setNamingPolicy(POLICY);
        return enhancer.create(argTypes, args);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T proxy(SimpleCallback<T> callback) {
        return (T) enhance(callback.getSuperClass(), callback);
    }
    
    /**
     * @author ZhaoQingJiang
     */
    static class NamingPolicy extends DefaultNamingPolicy {
    	
    	static final String TAG = "ByGoblin";
    	static final String IDENTIFIER = "$$EnhancerByGoblin$$";
    	
        @Override
        protected String getTag() {return TAG;}
    }
    
}
