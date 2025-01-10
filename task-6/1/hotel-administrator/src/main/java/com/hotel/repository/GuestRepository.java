package com.hotel.repository;

import com.hotel.model.Guest;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing hotel guests.
 * Provides CRUD operations for Guest entities and additional queries for business needs.
 */
public interface GuestRepository {

    /**
     * Finds a guest by their ID.
     *
     * @param id the unique identifier of the guest
     * @return an Optional containing the guest if found, empty otherwise
     */
    Optional<Guest> findById(Long id);
    
    /**
     * Retrieves all registered guests.
     *
     * @return list of all registered guests
     */
    List<Guest> findAll();
    
    /**
     * Saves or updates a guest.
     *
     * @param guest the guest to save or update
     * @return the saved guest
     * @throws IllegalArgumentException if the guest is null
     */
    Guest save(Guest guest);
    
    /**
     * Deletes a guest by their ID.
     *
     * @param id the unique identifier of the guest to delete
     * @return true if the guest was successfully deleted, false if the guest was not found
     * @throws IllegalArgumentException if id is null
     */
    boolean deleteById(Long id);

    void importFromCsv(String filePath);
    
    void exportToCsv(String filePath);
}
