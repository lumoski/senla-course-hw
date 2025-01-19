package com.hotel.controller;

import java.time.LocalDate;
import java.util.List;

import com.hotel.framework.di.annotation.Inject;
import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.service.BookingFacade;

public class BookingController {

    @Inject
    private BookingFacade bookingFacade;

    public Booking bookRoom(Long roomId, List<Long> guestIds, 
        LocalDate checkInDate, LocalDate checkOutDate) throws IllegalArgumentException {
            validateRoomId(roomId);

            return bookingFacade.bookRoom(roomId, guestIds, checkInDate, checkOutDate);
    }

    public Booking addBooking(Booking booking) {
            return bookingFacade.addBooking(booking);
    }

    public void checkOutExpiredBookings() {
        bookingFacade.checkOutExpiredBookings();
    }

    public void evictGuestsFromRoom(Long roomId) throws IllegalArgumentException {
        validateRoomId(roomId);

        bookingFacade.evictGuestsFromRoom(roomId);
    }

    public double calculateTotalPaymentForBooking(Long bookingId) throws IllegalArgumentException {
        validateBookingId(bookingId);

        return bookingFacade.calculateTotalPaymentForBooking(bookingId);
    }

    public List<Guest> getLastThreeGuestsByRoom(Long roomId) throws IllegalArgumentException {
        validateRoomId(roomId);

        return bookingFacade.getLastThreeGuestsByRoom(roomId);
    }

    public List<Guest> getLimitGuestsByRoom(Long roomId) throws IllegalArgumentException {
        validateRoomId(roomId);

        return bookingFacade.getLimitGuestsByRoom(roomId);
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

    public void importFromCsv(String filePath) {
        bookingFacade.importFromCsv(filePath);
    }

    public void exportToCsv(String filePath) {
        bookingFacade.exportToCsv(filePath);
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
