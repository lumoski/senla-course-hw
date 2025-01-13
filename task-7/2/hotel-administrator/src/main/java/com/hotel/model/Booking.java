package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {
    private final Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    private Room room;

    private double totalPrice;
    
    private List<Guest> guests = new ArrayList<>();

    @JsonCreator
    public Booking(@JsonProperty("id") Long id,
                   @JsonProperty("guest") List<Guest> guests,
                   @JsonProperty("room") Room room,
                   @JsonProperty("checkInDate") LocalDate checkInDate,
                   @JsonProperty("checkOutDate") LocalDate checkOutDate) {
        this.id = id;
        this.guests = guests;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
