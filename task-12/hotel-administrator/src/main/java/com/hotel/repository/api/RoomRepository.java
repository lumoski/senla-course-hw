package com.hotel.repository.api;

import com.hotel.model.entity.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing hotel rooms.
 * Provides CRUD operations for Room entities and additional queries for business needs.
 */
public interface RoomRepository {

    /**
     * Finds a room by its ID.
     *
     * @param id the unique identifier of the room
     * @return an Optional containing the room if found, empty otherwise
     */
    Optional<Room> findById(Long id);

    /**
     * Retrieves all rooms.
     *
     * @return list of all rooms
     */
    List<Room> findAll();

    /**
     * Retrieves all rooms sorted by their price in ascending order.
     *
     * @return list of rooms sorted by price
     */
    List<Room> findAllRoomsSortedByPrice();

    /**
     * Retrieves all rooms sorted by their capacity in ascending order.
     *
     * @return list of rooms sorted by capacity
     */
    List<Room> findAllRoomsSortedByCapacity();

    /**
     * Retrieves all rooms sorted by their star rating in ascending order.
     *
     * @return list of rooms sorted by number of stars
     */
    List<Room> findAllRoomsSortedByStarRating();

    /**
     * Retrieves all available rooms sorted by their price in ascending order.
     *
     * @return list of available rooms sorted by price
     */
    List<Room> findAllAvailableRoomsSortedByPrice();

    /**
     * Retrieves all available rooms sorted by their capacity in ascending order.
     *
     * @return list of available rooms sorted by capacity
     */
    List<Room> findAllAvailableRoomsSortedByCapacity();

    /**
     * Retrieves all available rooms sorted by their star rating in ascending order.
     *
     * @return list of available rooms sorted by number of stars
     */
    List<Room> findAllAvailableRoomsSortedByStarRating();

    /**
     * Retrieves the total count of available rooms.
     *
     * @return the count of available rooms
     */
    Integer findAvailableRoomsCount();

    List<Room> findAvailableRoomsByDate(LocalDate date);

    /**
     * Saves a room.
     *
     * @param room the room to save
     * @return the saved room
     * @throws IllegalArgumentException if the room is null or id of room is null
     */
    Room save(Room room);

    /**
     * Update room price.
     *
     * @param room the room to update with new price
     * @return the saved room
     * @throws IllegalArgumentException if the room is null or id of room is null
     */
    Room updatePrice(Room room);

    /**
     * Update room status.
     *
     * @param room the room to update with new status
     * @return the saved room
     * @throws IllegalArgumentException if the room is null or id of room is null
     */
    Room updateStatus(Room room);

    /**
     * Deletes a room by its ID.
     *
     * @param id the unique identifier of the room to delete
     * @return true if the room was successfully deleted, false if the room was not found
     */
    boolean deleteById(int id);
}
