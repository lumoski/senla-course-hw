package com.hotel.exception;

public class AmenityNotFoundException extends RuntimeException {
    public AmenityNotFoundException(Long amenityId) {
        super(String.format("Amenity with ID %d not found", amenityId));
    }
}
