package com.hotel.repository.api;

import com.hotel.model.entity.Booking;
import com.hotel.model.entity.Guest;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing hotel bookings.
 * Provides CRUD operations for Booking entities and additional queries for business needs.
 */
public interface BookingRepository {

    /**
     * Finds a booking by its ID.
     *
     * @param id the unique identifier of the booking
     * @return an Optional containing the booking if found, empty otherwise
     */
    Optional<Booking> findById(Long id);

    /**
     * Retrieves all bookings.
     *
     * @return list of all bookings
     */
    List<Booking> findAll();

    /**
     * Retrieves all guests sorted by their names.
     *
     * @return list of bookings with guests sorted by name
     */
    List<Guest> findAllGuestsSortedByName();

    /**
     * Retrieves all bookings sorted by their end dates.
     *
     * @return list of bookings sorted by end date
     */
    List<Booking> findAllBookingsSortedByEndDate();

    /**
     * Retrieves the total number of guests currently staying in the hotel.
     *
     * @return the count of guests in the hotel
     */
    Integer findAllGuestsCountInHotel();

    /**
     * Retrieves the last bookings for a specific room, limited count from properties,
     * sorted by check-out date in descending order.
     *
     * @param roomId the ID of the room
     * @return list of the last guests for the room
     */
    List<Guest> getLimitGuestsByRoomId(Long roomId);

    /**
     * Saves or updates a booking.
     *
     * @param booking the booking to save or update
     * @return the saved booking
     * @throws IllegalArgumentException if the booking is null
     */
    Booking save(Booking booking);

    /**
     * Deletes a booking by its ID.
     *
     * @param id the unique identifier of the booking to delete
     * @return true if the booking was deleted, false if the booking was not found
     * @throws IllegalArgumentException if id is null
     */
    boolean deleteById(Long id);

    boolean isRoomBooked(Booking booking);

    Optional<Booking> findCurrentBookingForRoom(Long roomId);
}