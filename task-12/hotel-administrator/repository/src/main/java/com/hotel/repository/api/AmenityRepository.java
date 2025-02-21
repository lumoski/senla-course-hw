package com.hotel.repository.api;

import com.hotel.core.model.entity.Amenity;

import java.util.List;
import java.util.Optional;

public interface AmenityRepository {

    Optional<Amenity> findById(Long id);

    List<Amenity> findAll();

    List<Amenity> findAllSortedByPrice();

    Amenity save(Amenity amenity);


    Amenity updatePrice(Amenity amenity);

    boolean deleteById(int id);
}