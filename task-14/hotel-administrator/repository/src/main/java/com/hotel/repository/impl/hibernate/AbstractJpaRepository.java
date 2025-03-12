package com.hotel.repository.impl.hibernate;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import com.hotel.database.EntityManagerProvider;
import com.hotel.repository.api.BaseRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractJpaRepository<T, ID> implements BaseRepository<T, ID> {
    
    private final Class<T> entityClass;
    
    protected AbstractJpaRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public T save(T entity) {
        try {
            log.debug("Attempting to save {} entity", entityClass.getSimpleName());
            
            EntityManagerProvider.beginTransaction();
            EntityManagerProvider.getEntityManager().persist(entity);
            EntityManagerProvider.commitTransaction();
            
            log.debug("Successfully saved {} entity", entityClass.getSimpleName());
            return entity;
            
        } catch (EntityExistsException e) {
            log.error("Entity already exists in database: {}", e.getMessage());
            EntityManagerProvider.rollbackTransaction();
            throw new RuntimeException("Entity already exists in database", e); 
        } catch (Exception e) {
            log.error("Error saving entity: {}", e.getMessage(), e);
            EntityManagerProvider.rollbackTransaction();
            throw new RuntimeException("Error saving entity", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    // Не учитывает многопоточность, нет обработчика OptimisticLockException
    @Override
    public T update(T entity) {
        try {
            log.debug("Attempting to update {} entity", entityClass.getSimpleName());
            
            EntityManagerProvider.beginTransaction();
            T merged = EntityManagerProvider.getEntityManager().merge(entity);
            EntityManagerProvider.commitTransaction();
            
            log.debug("Successfully updated {} entity", entityClass.getSimpleName());
            return merged;
        } catch (Exception e) {
            log.error("Error updating entity: {}", e.getMessage(), e);
            EntityManagerProvider.rollbackTransaction();
            throw new RuntimeException("Error updating entity", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }
    
    @Override
    public void delete(T entity) {
        try {
            log.debug("Attempting to delete {} entity", entityClass.getSimpleName());
            
            EntityManagerProvider.beginTransaction();
            if (!EntityManagerProvider.getEntityManager().contains(entity)) {
                entity = EntityManagerProvider.getEntityManager().merge(entity);
            }
            EntityManagerProvider.getEntityManager().remove(entity);
            EntityManagerProvider.commitTransaction();
            
            log.debug("Successfully deleted {} entity", entityClass.getSimpleName());
            
        } catch (Exception e) {
            log.error("Error deleting entity: {}", e.getMessage(), e);
            EntityManagerProvider.rollbackTransaction();
            throw new RuntimeException("Error deleting entity", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }
    
    @Override
    public void deleteById(ID id) {
        try {
            log.debug("Attempting to delete {} entity with ID: {}", entityClass.getSimpleName(), id);
            
            EntityManagerProvider.beginTransaction();
            T entity = EntityManagerProvider.getEntityManager().find(entityClass, id);
            
            if (entity != null) {
                EntityManagerProvider.getEntityManager().remove(entity);
                log.debug("Successfully deleted {} entity with ID: {}", entityClass.getSimpleName(), id);
            } else {
                log.warn("Entity {} with ID: {} not found", entityClass.getSimpleName(), id);
            }
            
            EntityManagerProvider.commitTransaction();
            
        } catch (Exception e) {
            log.error("Error deleting entity by ID: {}", e.getMessage(), e);
            EntityManagerProvider.rollbackTransaction();
            throw new RuntimeException("Error deleting entity by ID", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }
    
    @Override
    public Optional<T> findById(ID id) {
        try {
            log.debug("Searching for {} entity with ID: {}", entityClass.getSimpleName(), id);
            
            T entity = EntityManagerProvider.getEntityManager().find(entityClass, id);
            
            if (entity == null) {
                log.debug("Entity {} with ID: {} not found", entityClass.getSimpleName(), id);
            }
            
            return Optional.ofNullable(entity);
            
        } catch (Exception e) {
            log.error("Error finding entity by ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error finding entity by ID", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public List<T> findAll() {
        try {
            log.debug("Retrieving all entities of type: {}", entityClass.getSimpleName());
            
            EntityManager manager = EntityManagerProvider.getEntityManager();

            List<T> results = manager.createQuery(
                "FROM " + entityClass.getSimpleName(), 
                entityClass
            ).getResultList();
            
            log.debug("Found {} entities of type: {}", results.size(), entityClass.getSimpleName());
            return results;
            
        } catch (IllegalArgumentException e) {
            log.error("Query error: {}", e.getMessage(), e);
            throw new RuntimeException("Query error", e);
            
        } catch (Exception e) {
            log.error("Error retrieving entities: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving entities", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }
}