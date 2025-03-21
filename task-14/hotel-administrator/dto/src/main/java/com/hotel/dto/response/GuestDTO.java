package com.hotel.dto.response;

public record GuestDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String gender,
        String status
) { }
