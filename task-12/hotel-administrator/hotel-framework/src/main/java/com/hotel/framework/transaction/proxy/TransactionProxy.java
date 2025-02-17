package com.hotel.framework.transaction.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

@Slf4j
public class TransactionProxy {
    
    @SuppressWarnings("unchecked")
    public static <T> T create(T target, Class<T> interfaceClass) {
        log.debug("Creating transaction proxy for interface: {}", interfaceClass.getName());
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class<?>[] { interfaceClass },
            new TransactionHandler(target)
        );
    }
}