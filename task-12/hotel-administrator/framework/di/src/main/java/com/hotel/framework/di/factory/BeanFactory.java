package com.hotel.framework.di.factory;

import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.di.config.Configuration;
import com.hotel.framework.di.config.JavaConfiguration;
import com.hotel.framework.di.configurator.BeanConfigurator;
import com.hotel.framework.di.configurator.JavaBeanConfigurator;
import com.hotel.framework.transaction.annotation.Transactional;
import com.hotel.framework.transaction.proxy.TransactionProxy;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BeanFactory {

    private static final BeanFactory INSTANCE = new BeanFactory();

    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();
    
    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;

    private BeanFactory() {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(
                configuration.getPackageToScan(),
                configuration.getImplementationClasses()
            );
    }

    public static BeanFactory getInstance() {
        return INSTANCE;
    }

    public <T> T getBean(Class<T> clazz) {
        Class<? extends T> implementationClass = clazz;

        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }

        if (beans.containsKey(implementationClass)) {
            return (T) beans.get(implementationClass);
        }

        try {
            T bean = implementationClass.getDeclaredConstructor().newInstance();
            
            if (needsTransactionProxy(implementationClass)) {
                Class<?>[] interfaces = implementationClass.getInterfaces();
                if (interfaces.length == 0) {
                    throw new RuntimeException("Class " + implementationClass.getName() 
                        + " needs @Transactional but doesn't implement any interface");
                }

                @SuppressWarnings("unchecked")
                T proxyBean = (T) TransactionProxy.create(bean, (Class<T>) interfaces[0]);
                beans.put(implementationClass, proxyBean);
                injectFields(bean, implementationClass);
                return proxyBean;
            }

            beans.put(implementationClass, bean);
            injectFields(bean, implementationClass);

            log.debug("Bean created: {}", bean);
            return bean;
        } catch (Exception e) {
            log.error(String.valueOf(e.getCause()));
            throw new RuntimeException("Failed to create bean of type: " + clazz.getName(), e);
        }
    }

    private boolean needsTransactionProxy(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Transactional.class)) {
                return true;
            }
        }
        
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return needsTransactionProxy(superClass);
        }
        
        return false;
    }

    private <T> void injectFields(T bean, Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, getBean(field.getType()));
                    log.debug("Injecting {} in {}", field.getType(), clazz.getName());
                } catch (IllegalAccessException e) {
                    log.error("Failed to inject field: {}", field.getName(), e);
                }
            }
        }

        injectFields(bean, clazz.getSuperclass());
    }
}
