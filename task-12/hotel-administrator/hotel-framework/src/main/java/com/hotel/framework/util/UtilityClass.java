package com.hotel.framework.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilityClass {
    
    public static String getFieldNames(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        return Arrays.stream(fields)
                     .map(Field::getName)
                     .collect(Collectors.joining(","));
    }
}
