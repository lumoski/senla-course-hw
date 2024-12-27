package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Room {
    private int id;
    private double price;
    private RoomStatus status;

    // TODO: выделить сущность посетителя
    private String guestName;
}
