package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service layer for managing hotel bookings.
 * Handles business logic for creating, updating, and managing hotel bookings.
 */
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final RoomService roomService;

    private final BookingRepository bookingRepo;
    private final GuestRepository guestRepo;

    public Booking bookRoom(Long roomId, List<Long> guestIds, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = roomService.findById(roomId);

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room is not available for booking");
        }

        List<Guest> guests = guestIds.stream()
                .map(guestRepo::findById)
                .map(optionalGuest -> optionalGuest.orElseThrow(() ->
                        new IllegalArgumentException("Guest not found for provided ID")))
                .toList();

        if (guests.size() > room.getCapacity()) {
            throw new IllegalArgumentException("Room cannot accommodate more than " + room.getCapacity() + " guests");
        }

        if (checkInDate.isAfter(checkOutDate)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        roomService.changeRoomStatus(roomId, RoomStatus.OCCUPIED);

        Booking booking = new Booking(checkInDate, checkOutDate, room, room.getPrice() * guests.size(), guests);
        Booking savedBooking = bookingRepo.save(booking);

        log.info("Room {} successfully booked from {} to {} for {} guests", roomId, checkInDate, checkOutDate, guests.size());
        return savedBooking;
    }

    public void checkOutExpiredBookings() {
        List<Booking> expiredBookings = bookingRepo.findAll().stream()
                .filter(booking -> booking.getCheckOutDate().isBefore(LocalDate.now()) || booking.getCheckOutDate().isEqual(LocalDate.now()))
                .toList();

        for (Booking booking : expiredBookings) {
            Room room = booking.getRoom();

            roomService.changeRoomStatus(room.getId(), RoomStatus.AVAILABLE);

            log.info("Room {} has been checked out and set to AVAILABLE", room.getId());
        }
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
