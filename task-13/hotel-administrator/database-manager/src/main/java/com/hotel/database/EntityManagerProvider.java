package com.hotel.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityManagerProvider {
    private static final String PERSISTENCE_UNIT_NAME = "hibernate-persistence-unit";
    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> threadLocalIsTransactionActive = new ThreadLocal<>();
    private static EntityManagerFactory emFactory;

    static {
        threadLocalIsTransactionActive.set(false);
        threadLocalEntityManager.remove();
    }

    public static void createEntityManagerFactory() {
        try {
            emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            log.info("EntityManagerFactory created successfully");
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
