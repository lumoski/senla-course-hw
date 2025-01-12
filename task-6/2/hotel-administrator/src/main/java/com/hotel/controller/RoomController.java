package com.hotel.controller;

import java.time.LocalDate;
import java.util.List;

import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.service.RoomService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoomController {
    private final RoomService roomService;

    public Room addRoom(Room room) throws IllegalArgumentException {
        validate(room);

        return roomService.addRoom(room);
    }

    public Room changeRoomStatus(Long id, RoomStatus roomStatus) throws IllegalArgumentException {
        validateId(id);
        
        return roomService.changeRoomStatus(id, roomStatus);
    }

    public Room updateRoomPrice(Long id, double newPrice) throws IllegalArgumentException {
        validateId(id);
        validatePrice(newPrice);

        return roomService.updateRoomPrice(id, newPrice);
    }

    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    public List<Room> getAllRoomsSortedByPrice() {
        return roomService.getAllRoomsSortedByPrice();
    }

    public List<Room> getAllRoomsSortedByCapacity() {
        return roomService.getAllRoomsSortedByCapacity();
    }

    public List<Room> getAllRoomsSortedByStars() {
        return roomService.getAllRoomsSortedByStars();
    }

    public List<Room> getAllAvailableRoomsSortedByPrice() {
        return roomService.getAllAvailableRoomsSortedByPrice();
    }

    public List<Room> getAllAvailableRoomsSortedByCapacity() {
        return roomService.getAllAvailableRoomsSortedByCapacity();
    }

    public List<Room> getAllAvailableRoomsSortedByStars() {
        return roomService.getAllAvailableRoomsSortedByStars();
    }

    public Integer getAvailableRoomsCount() {
        return roomService.getAvailableRoomsCount();
    }

    public List<Room> getAvailableRoomsByDate(LocalDate localDate) {
        return roomService.getAvailableRoomsByDate(localDate);
    }

    public void importFromCsv(String filePath) {
        roomService.importFromCsv(filePath);
    }

    public void exportToCsv(String filePath) {
        roomService.exportToCsv(filePath);
    }

    private void validate(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        
        validateId(room.getId());
        validatePrice(room.getPrice());
        validateCapacity(room.getCapacity());
        validateStarRating(room.getStarRating());
        validateStatus(room.getStatus());
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Room price cannot be negative");
        }
    }

    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be greater than 0");
        }
    }

    private void validateStarRating(int starRating) {
        if (starRating < 1 || starRating > 5) {
            throw new IllegalArgumentException("Room star rating must be between 1 and 5");
        }
    }

    private void validateStatus(RoomStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Room status cannot be null");
        }
    }
}
