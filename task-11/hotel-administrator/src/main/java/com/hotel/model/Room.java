package com.hotel.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {
    private Long id;
    private double price;
    private int capacity;
    private int starRating;
    private RoomStatus status;

    @JsonCreator
    public Room(@JsonProperty("id") Long id,
                @JsonProperty("price") double price,
                @JsonProperty("capacity") int capacity,
                @JsonProperty("starRating") int starRating,
                @JsonProperty("status") RoomStatus status) {
        this.id = id;
        this.price = price;
        this.capacity = capacity;
        this.starRating = starRating;
        this.status = status;
    }
}
