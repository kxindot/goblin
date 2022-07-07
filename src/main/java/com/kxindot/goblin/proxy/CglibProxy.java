package com.kxindot.goblin.proxy;

import static com.kxindot.goblin.Classes.isInterfaceOrAbstractClass;
import static com.kxindot.goblin.Objects.requireNotNull;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author zhaoqingjiang
 */
public final class CglibProxy {

    private static final NamingPolicy POLICY = new NamingPolicy();
    
    /**
     * 创建指定类的代理对象
     * @param type Class<?> 待代理类
     * @param helper CallbackHelper 代理回调
     * @return Object 代理对象
     */
    public static Object enhance(Class<?> type, CallbackHelper helper) {
        return enhance(type, helper, new Class<?>[] {}, new Object[] {});
    }
    
    /**
     * 创建指定类的代理对象
     * @param type Class<?> 待代理类
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
    
    /**
     * @author ZhaoQingJiang
     */
    static class NamingPolicy extends DefaultNamingPolicy {
        @Override
        protected String getTag() {return "ByGoblin";}
    }
    
}
