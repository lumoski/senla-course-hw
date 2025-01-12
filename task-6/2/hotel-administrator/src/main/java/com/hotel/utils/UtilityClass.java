package com.hotel.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilityClass {
    public static String getFieldNames(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        return Arrays.stream(fields)
                     .map(Field::getName)
                     .collect(Collectors.joining(","));
    }
}
