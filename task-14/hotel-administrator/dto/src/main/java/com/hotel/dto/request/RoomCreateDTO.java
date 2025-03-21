package com.hotel.dto.request;

public record RoomCreateDTO(
        String roomNumber,
        double price,
        int capacity,
        int starRating,
        String status
) { }
