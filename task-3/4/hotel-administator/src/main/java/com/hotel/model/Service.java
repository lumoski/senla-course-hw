package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Service {
    private String name;
    private double price;
}