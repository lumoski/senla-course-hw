package com.hotel.framework.di.configurator;

import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class JavaBeanConfigurator implements BeanConfigurator {

    private final Reflections scanner;
    private final Map<Class<?>, Class<?>> implementationClasses;

    public JavaBeanConfigurator(String packageToScan, Map<Class<?>, Class<?>> implementationClasses) {
        this.scanner = new Reflections(packageToScan);
        this.implementationClasses = implementationClasses;
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {

        if (implementationClasses.containsKey(interfaceClass)) {
            return (Class<? extends T>) implementationClasses.get(interfaceClass);
        }

        Set<Class<? extends T>> classes = scanner.getSubTypesOf(interfaceClass);

        if (classes.isEmpty()) {
            throw new RuntimeException("No implementation found for interface: " + interfaceClass.getName());
        }

        if (classes.size() != 1) {
            throw new RuntimeException("Ambiguous implementation found for interface: " + interfaceClass.getName());
        }

        return classes.stream().findFirst().get();
    }
}
