package com.hotel.framework.transaction.proxy;

import com.hotel.database.EntityManagerProvider;
import com.hotel.framework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class TransactionHandler implements InvocationHandler {
    
    private final Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = target.getClass().getMethod(
            method.getName(), 
            method.getParameterTypes()
        );

        if (targetMethod.isAnnotationPresent(Transactional.class)) {
            try {
                EntityManagerProvider.beginTransaction();
                EntityManagerProvider.setIsTransactionActive(true);
                log.debug("Transaction started for method: {}", method.getName());
                
                Object result = method.invoke(target, args);
                
                EntityManagerProvider.setIsTransactionActive(false);
                EntityManagerProvider.commitTransaction();
                log.debug("Transaction committed for method: {}", method.getName());
                
                return result;
            } catch (Exception e) {
                EntityManagerProvider.setIsTransactionActive(false);
                EntityManagerProvider.rollbackTransaction();
                log.error("Transaction rolled back for method: {}", method.getName(), e);
                if (e instanceof java.lang.reflect.InvocationTargetException) {
                    throw e.getCause();
                }
                throw e;
            } finally {
                EntityManagerProvider.setIsTransactionActive(false);
                EntityManagerProvider.closeEntityManager();
            }
        }
        
        return method.invoke(target, args);
    }
}