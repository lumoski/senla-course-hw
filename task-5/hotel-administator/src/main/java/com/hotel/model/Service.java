package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Service {
    private final Long id;
    private String name;
    private double price;
    private String category;
}