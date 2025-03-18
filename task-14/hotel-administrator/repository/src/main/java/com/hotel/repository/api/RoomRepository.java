package com.hotel.repository.api;

import java.time.LocalDate;
import java.util.List;

import com.hotel.database.entity.RoomEntity;

public interface RoomRepository extends BaseRepository<RoomEntity, Long> {

    List<RoomEntity> findAllRoomsSortedByPrice();

    List<RoomEntity> findAllRoomsSortedByCapacity();

    List<RoomEntity> findAllRoomsSortedByStarRating();

    List<RoomEntity> findAllAvailableRoomsSortedByPrice();

    List<RoomEntity> findAllAvailableRoomsSortedByCapacity();

    List<RoomEntity> findAllAvailableRoomsSortedByStarRating();

    Integer findAvailableRoomsCount();

    List<RoomEntity> findAvailableRoomsByDate(LocalDate date);
}
