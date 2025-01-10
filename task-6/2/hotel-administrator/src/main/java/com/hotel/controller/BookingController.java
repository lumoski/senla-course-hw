package com.hotel.controller;

import java.time.LocalDate;
import java.util.List;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.service.BookingFacade;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookingController {
    private final BookingFacade bookingFacade;

    public Booking bookRoom(Long roomId, List<Long> guestIds, 
        LocalDate checkInDate, LocalDate checkOutDate) {
            validateRoomId(roomId);

            return bookingFacade.bookRoom(roomId, guestIds, checkInDate, checkOutDate);
    }

    public void checkOutExpiredBookings() {
        bookingFacade.checkOutExpiredBookings();
    }

    public void evictGuestsFromRoom(Long roomId) {
        validateRoomId(roomId);

        bookingFacade.evictGuestsFromRoom(roomId);
    }

    public double calculateTotalPaymentForBooking(Long bookingId) {
        validateBookingId(bookingId);

        return bookingFacade.calculateTotalPaymentForBooking(bookingId);
    }

    public List<Guest> getLastThreeGuestsByRoom(Long roomId) {
        validateRoomId(roomId);

        return bookingFacade.getLastThreeGuestsByRoom(roomId);
    }

    public List<Booking> getAllBookings() {
        return bookingFacade.getAllBookings();
    }

    public List<Booking> getAllBookingsSortedByGuestName() {
        return bookingFacade.getBookingsSortedByName();
    }

    public List<Booking> getAllBookingsSortedByEndDate() {
        return bookingFacade.getBookingsSortedByEndDate();
    }

    public Integer getAllGuestsCountInHotel() {
        return bookingFacade.getAllGuestsInHotel();
    }

    private void validateRoomId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
    }

    private void validateBookingId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
    }
}
