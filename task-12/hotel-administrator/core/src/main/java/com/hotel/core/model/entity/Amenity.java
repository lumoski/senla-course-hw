package com.hotel.core.model.entity;

import com.hotel.core.model.BaseEntity;
import com.hotel.core.model.enums.AmenityCategory;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Amenity extends BaseEntity {
    private String name;
    private double price;
    private AmenityCategory category;

    @Builder
    public Amenity(Long id,
                   String name,
                   double price,
                   AmenityCategory category,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
