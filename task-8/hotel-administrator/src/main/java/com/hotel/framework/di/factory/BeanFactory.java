package com.hotel.framework.di.factory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.di.config.Configuration;
import com.hotel.framework.di.config.JavaConfiguration;
import com.hotel.framework.di.configurator.BeanConfigurator;
import com.hotel.framework.di.configurator.JavaBeanConfigurator;

import lombok.extern.slf4j.Slf4j;

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
            beans.put(implementationClass, bean);

            injectFields(bean, implementationClass);

            log.info("Bean created: {}", bean);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean of type: " + clazz.getName(), e);
        }
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
                    log.info("Injecting bean of type: {}", field.getType());
                } catch (IllegalAccessException e) {
                    log.error("Failed to inject field: {}", field.getName(), e);
                }
            }
        }

        injectFields(bean, clazz.getSuperclass());
    }
}
