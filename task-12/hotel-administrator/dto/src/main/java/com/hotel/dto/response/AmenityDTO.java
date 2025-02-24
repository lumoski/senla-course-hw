package com.hotel.dto.response;

public record AmenityDTO(
        Long id,
        String name,
        double price,
        String category
) { }
