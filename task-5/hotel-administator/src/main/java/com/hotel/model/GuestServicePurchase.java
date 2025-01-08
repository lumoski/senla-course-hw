package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class GuestServicePurchase {
    private static transient long count = 0;

    private final Long id;

    private Guest guest;
    private Service service;
    private LocalDate purchaseDate;
    private double totalPrice;

    public GuestServicePurchase(Guest guest, Service service, LocalDate purchaseDate) {
        this.id = ++count;

        this.guest = guest;
        this.service = service;
        this.purchaseDate = purchaseDate;
        this.totalPrice = service.getPrice();
    }
}