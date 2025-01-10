package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Booking {
    private final Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room room;
    private double totalPrice;
    private List<Guest> guests = new ArrayList<>();
}
