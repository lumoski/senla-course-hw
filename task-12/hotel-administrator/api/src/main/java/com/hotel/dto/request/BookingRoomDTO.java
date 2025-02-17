package com.hotel.dto.request;

import java.time.LocalDate;
import java.util.List;

public record BookingRoomDTO(
        Long roomId,
        List<Long> guestIds,
        List<Long> amenityIds,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        String paymentMethod
) {}
