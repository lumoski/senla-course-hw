package com.hotel.dto.request;

public record BookingUpdatePaymentStatusDTO(
        Long id,
        String paymentStatus
) {}
