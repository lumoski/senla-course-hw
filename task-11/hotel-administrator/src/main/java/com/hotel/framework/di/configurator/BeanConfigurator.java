package com.hotel.framework.di.configurator;

public interface BeanConfigurator {

    <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass);
}
