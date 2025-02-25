package com.hotel.core.model.domain;

import lombok.Getter;
import lombok.Setter;

import com.hotel.core.model.enums.RoomStatus;

@Getter
@Setter
public class Room {
    private Long id;
    private String roomNumber;
    private double price;
    private int capacity;
    private int starRating;
    private RoomStatus status;

    public Room() {
    }

    public Room(Long id,
                String roomNumber,
                double price,
                int capacity,
                int starRating,
                RoomStatus status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.starRating = starRating;
        this.status = status;
    }
}
