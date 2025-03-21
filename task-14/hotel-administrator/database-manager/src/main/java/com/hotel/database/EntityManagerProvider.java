package com.hotel.database;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityManagerProvider {
    private static final String PERSISTENCE_UNIT_NAME = "hibernate-persistence-unit";
    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> threadLocalIsTransactionActive = new ThreadLocal<>();
    private static EntityManagerFactory emFactory;
    private static DatabaseConfig databaseConfig;
    
    public static void setDatabaseConfig(DatabaseConfig config) {
        databaseConfig = config;
        log.info("DatabaseConfig установлен в EntityManagerProvider");
    }

    static {
        threadLocalIsTransactionActive.set(false);
        threadLocalEntityManager.remove();
    }

    public static void createEntityManagerFactory() {
        try {
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.url", databaseConfig.getUrl());
            properties.put("javax.persistence.jdbc.user", databaseConfig.getUsername());
            properties.put("javax.persistence.jdbc.password", databaseConfig.getPassword());
            properties.put("javax.persistence.jdbc.driver", databaseConfig.getDriverClassName());
            
            emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
            log.info("EntityManagerFactory created successfully with properties from DatabaseConfig");
        } catch (Exception e) {
            log.error("Failed to create EntityManagerFactory", e);
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }

    public static void setIsTransactionActive(boolean isTransactionActive) {
        threadLocalIsTransactionActive.set(isTransactionActive);
    }

    public static EntityManager getEntityManager() {
        EntityManager em = threadLocalEntityManager.get();
        if (em == null || !em.isOpen()) {
            em = emFactory.createEntityManager();
            threadLocalIsTransactionActive.set(false);
            threadLocalEntityManager.set(em);
            log.debug("New EntityManager created");
        }
        return em;
    }

    public static void beginTransaction() {
        if (threadLocalIsTransactionActive.get()) {
            return;
        }

        EntityManager em = getEntityManager();
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
            log.debug("Transaction began");
        }
    }

    public static void commitTransaction() {
        if (threadLocalIsTransactionActive.get()) {
            return;
        }

        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
            log.debug("Transaction committed");
        }
    }

    public static void rollbackTransaction() {
        if (threadLocalIsTransactionActive.get()) {
            return;
        }
        
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
            log.debug("Transaction rolled back");
        }
    }

    public static void closeEntityManager() {
        if (threadLocalIsTransactionActive.get()) {
            return;
        }

        EntityManager em = threadLocalEntityManager.get();
        if (em != null && em.isOpen()) {
            em.close();
            threadLocalEntityManager.remove();
            log.debug("EntityManager closed");
        }
    }

    public static void closeEntityManagerFactory() {
        if (emFactory != null && emFactory.isOpen()) {
            emFactory.close();
            log.info("EntityManagerFactory closed");
        }
    }
}
