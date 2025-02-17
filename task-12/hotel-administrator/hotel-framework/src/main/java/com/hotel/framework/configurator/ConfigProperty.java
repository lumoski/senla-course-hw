package com.hotel.framework.configurator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty {
    String configFileName() default "config.properties";
    String propertyName() default "";
    String defaultValue() default "";
    Class<?> type() default String.class;
}
