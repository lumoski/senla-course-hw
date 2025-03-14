package com.hotel.core.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long roomId) {
        super(String.format("Room with ID %d not found", roomId));
    }
}
