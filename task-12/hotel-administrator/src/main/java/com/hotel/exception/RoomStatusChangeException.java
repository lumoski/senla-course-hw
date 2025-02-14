package com.hotel.exception;

public class RoomStatusChangeException extends RuntimeException {
    public RoomStatusChangeException(String message) {
        super(message);
    }
}
