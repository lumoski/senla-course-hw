package com.hotel.repository.api;

import java.util.List;
import java.util.Optional;

public interface BookingRepository<T, G, ID> extends BaseRepository<T, ID> {

    List<G> findAllGuestsSortedByName();

    List<T> findAllBookingsSortedByEndDate();

    Integer findAllGuestsCountInHotel();

    List<G> getLimitGuestsByRoomId(ID roomId);

    boolean isRoomBooked(T booking);

    Optional<T> findCurrentBookingForRoom(ID roomId);
}