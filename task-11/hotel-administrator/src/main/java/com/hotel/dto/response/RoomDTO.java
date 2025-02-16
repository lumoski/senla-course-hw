package com.hotel.dto.response;

public record RoomDTO(
        Long id,
        String roomNumber,
        double price,
        int capacity,
        int starRating,
        String status
) {}
