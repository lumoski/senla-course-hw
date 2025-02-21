package com.hotel.repository.api;

import com.hotel.core.model.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestRepository {

    Optional<Guest> findById(Long id);

    List<Guest> findAll();

    Guest save(Guest guest);

    boolean deleteById(Long id);
}