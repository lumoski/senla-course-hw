package com.hotel.dto.request;

import java.time.LocalDate;

public record GuestUpdateDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String passportNumber,
        String gender,
        String address,
        LocalDate birthDate,
        String status
) {}
