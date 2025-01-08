package com.hotel.repository.impl;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.repository.BookingRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryBookingRepository implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public Optional<Booking> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }

    @Override
    public List<Booking> findAllGuestsSortedByName() {
        return bookings.stream()
                .sorted(Comparator.comparing(booking -> booking.getGuests()
                        .stream()
                        .findFirst()
                        .map(Guest::getFirstName)
                        .orElse("")))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findAllGuestsSortedByEndDate() {
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getCheckOutDate))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getAllGuestsInHotel() {
        return bookings.stream()
                .filter(booking -> !booking.getCheckInDate().isAfter(LocalDate.now()) &&
                        !booking.getCheckOutDate().isBefore(LocalDate.now()))
                .mapToInt(booking -> booking.getGuests().size())
                .sum();
    }

    @Override
    public Double getTotalPaymentForGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }

        return bookings.stream()
                .filter(booking -> booking.getGuests().contains(guest))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    @Override
    public List<Guest> getLastThreeGuestsByRoom(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
    
        List<Guest> guests = new ArrayList<>();
    
        for (Booking booking : bookings) {
            if (booking.getRoom() != null && roomId.equals(booking.getRoom().getId())) {
                guests.addAll(booking.getGuests());
            }
        }
    
        return guests.stream()
                .skip(Math.max(0, guests.size() - 3))
                .collect(Collectors.toList());
    }

    @Override
    public Booking save(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        findById(booking.getId()).ifPresent(existingBooking -> bookings.remove(existingBooking));
        bookings.add(booking);
        return booking;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return bookings.removeIf(booking -> booking.getId().equals(id));
    }
}
