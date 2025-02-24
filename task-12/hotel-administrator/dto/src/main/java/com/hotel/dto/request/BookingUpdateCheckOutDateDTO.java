package com.hotel.dto.request;

import java.time.LocalDate;

public record BookingUpdateCheckOutDateDTO(
        Long id,
        LocalDate checkOutDate
) { }
