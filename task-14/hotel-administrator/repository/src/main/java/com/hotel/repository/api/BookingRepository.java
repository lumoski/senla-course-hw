package com.hotel.repository.api;

import java.util.List;
import java.util.Optional;

import com.hotel.database.entity.BookingEntity;
import com.hotel.database.entity.GuestEntity;

public interface BookingRepository extends BaseRepository<BookingEntity, Long> {

    List<GuestEntity> findAllGuestsSortedByName();

    List<BookingEntity> findAllBookingsSortedByEndDate();

    Integer findAllGuestsCountInHotel();

    List<GuestEntity> getLimitGuestsByRoomId(Long roomId);

    boolean isRoomBooked(BookingEntity booking);

    Optional<BookingEntity> findCurrentBookingForRoom(Long roomId);
}