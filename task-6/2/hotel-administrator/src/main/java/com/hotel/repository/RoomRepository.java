package com.hotel.repository;

import com.hotel.model.Room;

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
    Integer getAvailableRoomsCount();

    /**
     * Retrieves a list of rooms available for booking on a specific date.
     *
     * @param localDate the date for which room availability is checked
     * @return list of available rooms for the specified date
     */
    List<Room> getAvailableRoomsByDate(LocalDate localDate);
    
    /**
     * Saves or updates a room.
     *
     * @param room the room to save or update
     * @return the saved room
     * @throws IllegalArgumentException if the room is null
     */
    Room save(Room room);
    
    /**
     * Deletes a room by its ID.
     *
     * @param id the unique identifier of the room to delete
     * @return true if the room was successfully deleted, false if the room was not found
     */
    boolean deleteById(int id);

    void importFromCsv(String filePath);
    
    void exportToCsv(String filePath);
}
