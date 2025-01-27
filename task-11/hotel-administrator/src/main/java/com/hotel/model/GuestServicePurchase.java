package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class GuestServicePurchase {
    private Long id;
    private Guest guest;
    private Service service;
    private LocalDate purchaseDate;
    private double totalPrice;
}