package com.hotel.core.model.domain;

import com.hotel.core.model.enums.AmenityCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Amenity {
    private Long id;
    private String name;
    private double price;
    private AmenityCategory category;

    public Amenity() {
    }

    public Amenity(Long id, String name, double price, AmenityCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
