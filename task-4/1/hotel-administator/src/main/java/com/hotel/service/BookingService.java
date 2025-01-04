package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service layer for managing hotel bookings.
 * Handles business logic for creating, updating, and managing hotel bookings.
 */
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepo;

    public Booking addBooking(Booking booking) {
        bookingRepo.save(booking);
        log.info("Booking '{}' added successfully", booking.getId());
        return booking;
    }

    public double calculateTotalPaymentForBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Booking with ID " + bookingId + " not found"));

        long daysStayed = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        if (daysStayed <= 0) {
            throw new IllegalArgumentException("Invalid booking duration");
        }

        double totalPayment = daysStayed * booking.getRoom().getPrice();
        log.info("Total payment for booking {} in room {} : {}", bookingId, booking.getRoom().getId(), totalPayment);
        return totalPayment;
    }

    public Booking findBookingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

        return bookingRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to find booking: Booking with ID '{}' not found", id);
            return new IllegalArgumentException("Booking with ID '" + id + "' not found");
        });
    }

    public List<Booking> getLastThreeGuestsByRoom(Long roomId) {
        return bookingRepo.getLastThreeGuestsByRoom(roomId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public List<Booking> getBookingsSortedByName() {
        return bookingRepo.findAllGuestsSortedByName();
    }

    public List<Booking> getBookingsSortedByEndDate() {
        return bookingRepo.findAllGuestsSortedByEndDate();
    }

    public Integer getAllGuestsInHotel() {
        return bookingRepo.getAllGuestsInHotel();
    }
}
