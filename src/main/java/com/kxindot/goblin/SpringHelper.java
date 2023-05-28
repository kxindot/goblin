package com.kxindot.goblin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author ZhaoQingJiang
 */
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringHelper.context = context;
    }
    
    @EventListener
    void onContextRefreshed(ContextRefreshedEvent event) {
        
    }
    
    @EventListener
    void onContextClosed(ContextClosedEvent event) {
        
    }

    
    @FunctionalInterface
    public interface SpringInitailizer {
        void initailize(ApplicationContext Context);
    }
    
    @FunctionalInterface
    public interface SpringDestroyer {
        void destroy();
    }
    
    /**
     * Get spring context
     * @return ApplicationContext
     */
    public static ApplicationContext getSpringContext() {
        return context;
    }
    
    /**
     * Find existing bean from spring context
     * @param name beanName
     * @return T
     */
    public static Object getBean(String name) {
        return context == null ? null : context.getBean(name);
    }
    
    /**
     * Find existing bean from spring context
     * @param name beanName
     * @param type beanType
     * @return T
     */
    public static <T> T getBean(String name, Class<T> type) {
        return context == null ? null : type.cast(context.getBean(name));
    }
    
    /**
     * find existing bean from spring context
     * @param type beanType
     * @return T
     */
    public static <T> T getBean(Class<T> type) {
        return context == null ? null : context.getBean(type);
    }
    
    /**
     * find existing bean list from spring context
     * @param type beanType
     * @return {@code List<T>}
     */
    public static <T> List<T> getBeans(Class<T> type) {
        if (isContextEnable()) {
            Map<String, T> result = context.getBeansOfType(type);
            if (result != null) {
                Collection<T> values = result.values();
                if (!values.isEmpty()) {
                    return Objects.newArrayList(values);
                }
            }
        }
        return Objects.unmodifiableEmptyList();
    }
    
    /**
     * Populate the given bean instance through applying after-instantiation 
     * callbacks and bean property post-processing (e.g. for annotation-driven injection).
     * @param bean existingBean
     */
    public static void autowire(Object bean) {
        if (isContextEnable()) {
            context.getAutowireCapableBeanFactory().autowireBean(bean);
        }
    }
    
    /**
     * Register the given existing object as singleton in the bean registry, using bean.getClass.getSimpleName as bean name.
     * @param bean existingBean
     * @param name existingBeanName
     */
    public static void register(Object bean) {
        register(bean, bean.getClass().getSimpleName());
    }
    
    /**
     * Register the given existing object as singleton in the bean registry, under the given bean name.
     * @param bean existingBean
     * @param name existingBeanName
     */
    public static void register(Object bean, String name) {
        if (isContextEnable()) {
            ((DefaultListableBeanFactory) context).registerSingleton(name, bean);
        }
    }
    
    /**
     * Notify all matching listeners registered with this application of an application event. 
     * Events may be framework events (such as RequestHandledEvent) or application-specific events.
     * @param event ApplicationEvent
     */
    public static void publishEvent(ApplicationEvent event) {
        if (isContextEnable()) {
            context.publishEvent(event);
        }
    }
    
    protected static boolean isContextEnable() {
        return context != null;
    }
    
    
    /******************* Transaction Help *******************/
    
    private static Map<Transactional, DataSourceTransactionManager> cache = Objects.newConcurrentHashMap();
    
    
    @Deprecated
    public static DataSourceTransactionManager getTransactionManager() {
        return isContextEnable() ? getBean(DataSourceTransactionManager.class) : null;
    }
    
    public static DataSourceTransactionManager getTransactionManager(String name) {
        return isContextEnable() ? getBean(name, DataSourceTransactionManager.class) : null;
    }
    
    public static DataSourceTransactionManager getTransactionManager(Transactional transactional) {
        Objects.requireNotNull(transactional);
        if (!isContextEnable()) return null;
        if (cache.containsKey(transactional)) {
            return cache.get(transactional);
        }
        String name = transactional.value();
        if (Objects.isBlank(name)) {
            name = transactional.transactionManager();
            if (Objects.isBlank(name)) {
                name = null;
            }
        }
        DataSourceTransactionManager manager = name == null ? getTransactionManager() : getTransactionManager(name);
        cache.putIfAbsent(transactional, manager);
        return manager;
    }
    
    @Deprecated
    public static TransactionStatus startTransaction() {
        return startTransaction(null, null);
    }
    
    public static TransactionStatus startTransaction(String managerName) {
        return startTransaction(managerName, null);
    }
    
    @Deprecated
    public static TransactionStatus startTransaction(int propagationBehavior) {
        return startTransaction(null, new DefaultTransactionDefinition(propagationBehavior));
    }
    
    @Deprecated
    public static TransactionStatus startTransaction(TransactionDefinition definition) {
        return startTransaction(null, definition);
    }
    
    public static TransactionStatus startTransaction(String managerName, int propagationBehavior) {
        DataSourceTransactionManager manager = managerName == null 
                ? getTransactionManager() : getTransactionManager(managerName);
        return startTransactionWithManager(manager, new DefaultTransactionDefinition(propagationBehavior));
    }
    
    public static TransactionStatus startTransaction(String managerName, TransactionDefinition definition) {
        DataSourceTransactionManager manager = managerName == null 
                ? getTransactionManager() : getTransactionManager(managerName);
        return startTransactionWithManager(manager, definition);
    }
    
    public static TransactionStatus startTransactionWithManager(DataSourceTransactionManager manager) {
        return startTransactionWithManager(manager, null);
    }
    
    public static TransactionStatus startTransactionWithManager(DataSourceTransactionManager manager, TransactionDefinition definition) {
        TransactionDefinition def = definition;
        if (def == null) {
            def = new DefaultTransactionDefinition();
        }
        return manager.getTransaction(def);
    }
    
    public static TransactionStatus startTransaction(Transactional transactional) {
        DataSourceTransactionManager manager = getTransactionManager(transactional);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(transactional.propagation().value());
        definition.setIsolationLevel(transactional.isolation().value());
        definition.setTimeout(transactional.timeout());
        definition.setReadOnly(transactional.readOnly());
        return manager.getTransaction(definition);
    }
    
    @Deprecated
    public static void commit(TransactionStatus status) {
        commit("", status);
    }
    
    public static void commit(String managerName, TransactionStatus status) {
        DataSourceTransactionManager manager =  Objects.isBlank(managerName) 
                ? getTransactionManager() : getTransactionManager(managerName);
        manager.commit(status);
    }
    
    public static void commit(Transactional transactional, TransactionStatus status) {
        getTransactionManager(transactional).commit(status);
    }
    
    @Deprecated
    public static void rollback(TransactionStatus status) {
        rollback("", status);
    }
    
    public static void rollback(String managerName, TransactionStatus status) {
        DataSourceTransactionManager manager =  Objects.isBlank(managerName) 
                ? getTransactionManager() : getTransactionManager(managerName);
        manager.rollback(status);
    }
    
    
    public static void rollback(Transactional transactional, 
            TransactionStatus status, Class<?>[] rollbacks, Class<?>[] noRollbacks, Throwable t) {
        Objects.requireNotNull(t, "t == null");
        if (Objects.isNotEmpty(noRollbacks)) {
            if (Stream.of(noRollbacks)
                    .filter(e -> e.isInstance(t))
                    .findFirst().isPresent()) {
                return;
            }
        }
        if (Objects.isNotEmpty(rollbacks)) {
            if (Stream.of(rollbacks)
                    .filter(e -> e.isInstance(t))
                    .findFirst().isPresent()) {
                getTransactionManager(transactional).rollback(status);
                return;
            }
        }
        if (RuntimeException.class.isInstance(t) || Error.class.isInstance(t)) {
            getTransactionManager(transactional).rollback(status);
        }
    }
    
}
