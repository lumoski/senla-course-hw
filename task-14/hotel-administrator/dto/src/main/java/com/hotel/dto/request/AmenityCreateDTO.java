package com.hotel.dto.request;

public record AmenityCreateDTO(
        String name,
        double price,
        String category
) { }
