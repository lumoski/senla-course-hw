package com.hotel.repository.impl;

import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

public class InMemoryGuestRepository implements GuestRepository {
    
    private final List<Guest> guests = new ArrayList<>();

    @Override
    public Optional<Guest> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return guests.stream()
                .filter(guest -> guest.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest save(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }

        if (guest.getId() == null) {
            throw new IllegalArgumentException("Guest ID cannot be null");
        }

        guests.stream()
                .filter(g -> g.getId().equals(guest.getId()))
                .findFirst()
                .ifPresent(guests::remove);

        guests.add(guest);
        return guest;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        int initialSize = guests.size();
        guests.removeIf(guest -> Objects.equals(guest.getId(), id));
        return guests.size() < initialSize;
    }
}