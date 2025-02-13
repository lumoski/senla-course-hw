package com.hotel.dto.request;

import com.hotel.dto.response.AmenityDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.dto.response.RoomDTO;

import java.time.LocalDate;
import java.util.List;

public record BookingCreateDTO(
        RoomDTO roomDTO,
        List<GuestDTO> guestDTOList,
        List<AmenityDTO> amenityDTOList,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        String paymentMethod
) {}
