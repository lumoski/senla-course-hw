package com.hotel.exception;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(Long guestId) {
        super(String.format("Guest with ID %d not found", guestId));
    }
}
