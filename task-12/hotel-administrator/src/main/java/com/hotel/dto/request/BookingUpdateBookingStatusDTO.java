package com.hotel.dto.request;

import com.hotel.dto.response.AmenityDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.dto.response.RoomDTO;

import java.time.LocalDate;
import java.util.List;

public record BookingUpdateBookingStatusDTO(
        Long id,
        String bookingStatus
) {}
