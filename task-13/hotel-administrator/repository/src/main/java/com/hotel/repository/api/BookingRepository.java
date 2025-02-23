package com.hotel.repository.api;

import com.hotel.core.model.entity.Booking;
import com.hotel.core.model.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    Optional<Booking> findById(Long id);

    List<Booking> findAll();

    List<Guest> findAllGuestsSortedByName();

    List<Booking> findAllBookingsSortedByEndDate();

    Integer findAllGuestsCountInHotel();

    List<Guest> getLimitGuestsByRoomId(Long roomId);

    Booking save(Booking booking);

    boolean deleteById(Long id);

    boolean isRoomBooked(Booking booking);

    Optional<Booking> findCurrentBookingForRoom(Long roomId);
}