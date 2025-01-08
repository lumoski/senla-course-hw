package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Booking {
    private static transient long count = 0;

    private final Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room room;
    private double totalPrice;
    private List<Guest> guests = new ArrayList<>();

    public Booking(LocalDate checkInDate, LocalDate checkOutDate, Room room, double totalPrice, List<Guest> guests) {
        this.id = ++count;

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.totalPrice = totalPrice;
        this.guests = guests;
    }
}
