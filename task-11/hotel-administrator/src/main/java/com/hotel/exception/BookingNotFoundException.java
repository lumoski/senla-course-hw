package com.hotel.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long bookingId) {
        super(String.format("Booking with ID %d not found", bookingId));
    }
}
