package com.hotel.framework.di.config;

import java.util.Map;

public interface Configuration {

    String getPackageToScan();
    
    Map<Class<?>, Class<?>> getImplementationClasses();
}
