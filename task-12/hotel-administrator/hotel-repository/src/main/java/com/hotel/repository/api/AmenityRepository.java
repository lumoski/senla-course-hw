package com.hotel.repository.api;

import com.hotel.model.entity.Amenity;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing hotel amenities.
 * Provides CRUD operations for Amenity entities and additional queries for business needs.
 */
public interface AmenityRepository {

    /**
     * Finds an Amenity by its ID.
     *
     * @param id the unique identifier of the amenity
     * @return an Optional containing the amenity if found, empty otherwise
     */
    Optional<Amenity> findById(Long id);

    /**
     * Retrieves all available amenities.
     *
     * @return list of all amenities
     */
    List<Amenity> findAll();

    /**
     * Retrieves all amenities sorted by their price in ascending order.
     *
     * @return list of amenities sorted by price
     */
    List<Amenity> findAllSortedByPrice();

    /**
     * Saves or updates an amenity.
     *
     * @param amenity the amenity to save or update
     * @return the saved amenity
     */
    Amenity save(Amenity amenity);

    /**
     * Updates an amenity price.
     *
     * @param amenity the amenity to update
     * @return the saved amenity
     */
    Amenity updatePrice(Amenity amenity);

    /**
     * Deletes an amenity by its id.
     *
     * @param id the id of the amenity to delete
     * @return true if the amenity was successfully deleted, false if the amenity was not found
     */
    boolean deleteById(int id);
}