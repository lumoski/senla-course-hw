package com.hotel.dto.request;

import java.time.LocalDate;

public record GuestCreateDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String passportNumber,
        String gender,
        String address,
        LocalDate birthDate,
        String status
) { }
