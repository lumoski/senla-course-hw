package com.hotel.repository.api;

import com.hotel.core.model.entity.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(Long id);

    List<Room> findAll();

    List<Room> findAllRoomsSortedByPrice();

    List<Room> findAllRoomsSortedByCapacity();

    List<Room> findAllRoomsSortedByStarRating();

    List<Room> findAllAvailableRoomsSortedByPrice();

    List<Room> findAllAvailableRoomsSortedByCapacity();

    List<Room> findAllAvailableRoomsSortedByStarRating();

    Integer findAvailableRoomsCount();

    List<Room> findAvailableRoomsByDate(LocalDate date);

    Room save(Room room);

    Room updatePrice(Room room);

    Room updateStatus(Room room);

    boolean deleteById(int id);
}
