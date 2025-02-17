package com.hotel.dto.request;

public record BookingUpdateBookingStatusDTO(
        Long id,
        String bookingStatus
) {}
