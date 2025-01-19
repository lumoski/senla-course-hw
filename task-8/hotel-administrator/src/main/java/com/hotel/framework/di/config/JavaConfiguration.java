package com.hotel.framework.di.config;

import java.util.Map;
import java.util.HashMap;

public class JavaConfiguration implements Configuration {

    @Override
    public String getPackageToScan() {
        return "com.hotel";
    }

    @Override
    public Map<Class<?>, Class<?>> getImplementationClasses() {
        return new HashMap<>();
    }
}
