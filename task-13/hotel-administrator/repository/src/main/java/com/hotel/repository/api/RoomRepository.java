package com.hotel.repository.api;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository<T, ID> extends BaseRepository<T, ID> {

    List<T> findAllRoomsSortedByPrice();

    List<T> findAllRoomsSortedByCapacity();

    List<T> findAllRoomsSortedByStarRating();

    List<T> findAllAvailableRoomsSortedByPrice();

    List<T> findAllAvailableRoomsSortedByCapacity();

    List<T> findAllAvailableRoomsSortedByStarRating();

    Integer findAvailableRoomsCount();

    List<T> findAvailableRoomsByDate(LocalDate date);
}
