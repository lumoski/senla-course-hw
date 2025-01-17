package com.hotel.repository.impl;

import com.hotel.model.GuestServicePurchase;
import com.hotel.repository.GuestServicePurchaseRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryGuestServicePurchaseRepository implements GuestServicePurchaseRepository {
    
    private final List<GuestServicePurchase> purchases = new ArrayList<>();

    @Override
    public List<GuestServicePurchase> findServicesByGuestId(Long guestId) {
        if (guestId == null) {
            throw new IllegalArgumentException("Guest ID cannot be null");
        }
        return purchases.stream()
                .filter(purchase -> purchase.getGuest().getId().equals(guestId))
                .collect(Collectors.toList());
    }

    @Override
    public List<GuestServicePurchase> findAllSortedByPrice() {
        return purchases.stream()
                .sorted(Comparator.comparingDouble(GuestServicePurchase::getTotalPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<GuestServicePurchase> findAllSortedByDate() {
        return purchases.stream()
                .sorted(Comparator.comparing(GuestServicePurchase::getPurchaseDate))
                .collect(Collectors.toList());
    }

    @Override
    public GuestServicePurchase addPurchase(GuestServicePurchase purchase) {
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase cannot be null");
        }
        purchases.add(purchase);
        return purchase;
    }

    @Override
    public void importFromCsv(String filePath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'importFromCsv'");
    }

    @Override
    public void exportToCsv(String filePath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportToCsv'");
    }
}
