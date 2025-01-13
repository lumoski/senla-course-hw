package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room {
    private final Long id;
    private double price;
    private int capacity;
    private int starRating;
    private RoomStatus status;
}
