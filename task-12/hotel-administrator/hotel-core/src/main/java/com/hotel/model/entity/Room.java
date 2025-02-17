package com.hotel.model.entity;

import com.hotel.model.BaseEntity;
import com.hotel.model.enums.RoomStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Room extends BaseEntity {
    private String roomNumber;
    private double price;
    private int capacity;
    private int starRating;
    private RoomStatus status;

    @Builder
    public Room(Long id,
                String roomNumber,
                double price,
                int capacity,
                int starRating,
                RoomStatus status,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.starRating = starRating;
        this.status = status;
    }
}
