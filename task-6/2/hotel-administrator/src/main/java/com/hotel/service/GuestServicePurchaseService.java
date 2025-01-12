package com.hotel.service;

import com.hotel.model.Guest;
import com.hotel.model.GuestServicePurchase;
import com.hotel.model.Service;
import com.hotel.repository.GuestServicePurchaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for managing guest service purchases.
 * Handles business logic for recording and retrieving service purchases.
 */
@Slf4j
@RequiredArgsConstructor
public class GuestServicePurchaseService {
    private final GuestServicePurchaseRepository purchaseRepository;

    public GuestServicePurchase recordPurchase(Guest guest, Service service, LocalDate purchaseDate, double totalPrice) {
        if (guest == null || service == null || purchaseDate == null) {
            throw new IllegalArgumentException("Guest, service, and purchase date cannot be null");
        }

        GuestServicePurchase purchase = new GuestServicePurchase(null, guest, service, purchaseDate, totalPrice);
        purchaseRepository.addPurchase(purchase);
        log.info("Recorded purchase: Guest '{}' purchased service '{}' on '{}' for {}",
                guest.getId(), service.getName(), purchaseDate, totalPrice);
        return purchase;
    }

    public List<GuestServicePurchase> getServicesByGuest(Long guestId) {
        if (guestId == null) {
            throw new IllegalArgumentException("Guest ID cannot be null");
        }

        List<GuestServicePurchase> purchases = purchaseRepository.findServicesByGuestId(guestId);
        log.info("Retrieved {} services for guest with ID '{}'", purchases.size(), guestId);
        return purchases;
    }

    public List<GuestServicePurchase> getAllPurchasesSortedByPrice() {
        List<GuestServicePurchase> purchases = purchaseRepository.findAllSortedByPrice();
        log.info("Retrieved {} purchases sorted by price", purchases.size());
        return purchases;
    }

    public List<GuestServicePurchase> getAllPurchasesSortedByDate() {
        List<GuestServicePurchase> purchases = purchaseRepository.findAllSortedByDate();
        log.info("Retrieved {} purchases sorted by date", purchases.size());
        return purchases;
    }
}
