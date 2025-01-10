package com.hotel.repository;

import com.hotel.model.Service;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing hotel services.
 * Provides CRUD operations for Service entities and additional queries for business needs.
 */
public interface ServiceRepository {

    /**
     * Finds a Service by its ID.
     *
     * @param id the unique identifier of the service
     * @return an Optional containing the service if found, empty otherwise
     */
    Optional<Service> findById(Long id);
    
    /**
     * Retrieves all available services.
     *
     * @return list of all services
     */
    List<Service> findAll();

    /**
     * Retrieves all services sorted by their price in ascending order.
     *
     * @return list of services sorted by price
     */
    List<Service> findAllSortedByPrice();
    
    /**
     * Saves or updates a service.
     *
     * @param service the service to save or update
     * @return the saved service
     * @throws IllegalArgumentException if the service is null
     */
    Service save(Service service);
    
    /**
     * Deletes a service by its name.
     *
     * @param name the name of the service to delete
     * @return true if the service was successfully deleted, false if the service was not found
     * @throws IllegalArgumentException if name is null or empty
     */
    boolean deleteByName(String name);

    void importFromCsv(String filePath);
    
    void exportToCsv(String filePath);
}
