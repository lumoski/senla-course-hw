package com.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private String bookingReference;
    private RoomDTO roomDTO;
    private List<GuestDTO> guestDTOList;
    private List<AmenityDTO> amenityDTOList;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private String bookingStatus;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime cancelledAt;
}
