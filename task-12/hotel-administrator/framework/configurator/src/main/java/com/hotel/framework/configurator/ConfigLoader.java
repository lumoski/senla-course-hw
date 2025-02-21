package com.hotel.framework.configurator;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

@Slf4j
public class ConfigLoader {

    private static String DEFAULT_APPLICATION_FILE;

    public static void initialize(Object object, String filePath) {
        DEFAULT_APPLICATION_FILE = filePath;
        initialize(object);
        DEFAULT_APPLICATION_FILE = "application.properties";
    }

    public static void initialize(Object object) {
        Class<?> clazz = object.getClass();

        Properties properties = new Properties();

        try (InputStream inputStream = clazz.getModule().getClassLoader().getResourceAsStream(DEFAULT_APPLICATION_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Config loaf error " + DEFAULT_APPLICATION_FILE, e);
        }

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

        log.info("Configuration for {} initialize successfully", clazz);
    }
}