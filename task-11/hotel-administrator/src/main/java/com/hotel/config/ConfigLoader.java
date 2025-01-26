package com.hotel.config;

import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigLoader {

    public static void initialize(Object object, Properties properties) {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);

                String key = configProperty.propertyName();
                if (key.isEmpty()) {
                    key = clazz.getSimpleName().toUpperCase() + "." + field.getName().toUpperCase();
                }

                String defaultValue = configProperty.defaultValue();
                String value = properties.getProperty(key, defaultValue);
                field.setAccessible(true);

                try {
                    if (field.getType() == int.class) {
                        field.setInt(object, Integer.parseInt(value));
                    } else if (field.getType() == boolean.class) {
                        field.setBoolean(object, Boolean.parseBoolean(value));
                    } else {
                        field.set(object, value);
                    }
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    throw new RuntimeException("Failed to set config property for field: " + field.getName(), e);
                }
            }
        }
    }
}