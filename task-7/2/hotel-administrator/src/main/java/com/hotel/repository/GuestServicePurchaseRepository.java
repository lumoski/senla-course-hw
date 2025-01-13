package com.hotel.repository;

import com.hotel.model.GuestServicePurchase;
import java.util.List;

/**
 * Repository interface for managing guest service purchases.
 * Provides methods for retrieving and sorting service purchases.
 */
public interface GuestServicePurchaseRepository {

    /**
     * Retrieves all services purchased by a specific guest.
     *
     * @param guestId the unique identifier of the guest
     * @return list of services purchased by the guest
     */
    List<GuestServicePurchase> findServicesByGuestId(Long guestId);

    /**
     * Retrieves all service purchases sorted by their price in ascending order.
     *
     * @return list of service purchases sorted by price
     */
    List<GuestServicePurchase> findAllSortedByPrice();

    /**
     * Retrieves all service purchases sorted by their purchase date in ascending order.
     *
     * @return list of service purchases sorted by date
     */
    List<GuestServicePurchase> findAllSortedByDate();

    /**
     * Adds a new service purchase to the repository.
     *
     * @param purchase the purchase to add
     */
    GuestServicePurchase addPurchase(GuestServicePurchase purchase);

    void importFromCsv(String filePath);
    
    void exportToCsv(String filePath);
}
