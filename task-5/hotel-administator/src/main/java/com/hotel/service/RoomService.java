package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for managing hotel rooms.
 * Handles business logic for creating and updating hotel rooms.
 */
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepo;

    public Room findById(Long id) {
        Room room = roomRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to find Room: Room with ID '{}' not found", id);
            return new IllegalArgumentException("Room with ID '" + id + "' not found");
        });

        return room;
    }

    public String getRoomDetails(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }

        Room room = roomRepo.findById(roomId).orElseThrow(() -> {
            log.warn("Room with ID '{}' not found", roomId);
            return new IllegalArgumentException("Room with ID '" + roomId + "' not found");
        });

        return String.format(
            "---------------" +
            "Room Details:%n" +
            "ID: %d%n" +
            "Price: %.2f%n" +
            "Capacity: %d%n" +
            "Stars: %d%n" +
            "Status: %s", 
            room.getId(), 
            room.getPrice(), 
            room.getCapacity(), 
            room.getStarRating(), 
            room.getStatus().toString()
        );
    }

    public Room addRoom(Room room) {
        validateRoomParameters(room);

        if (roomRepo.findById(room.getId()).isPresent()) {
            log.warn("Failed to add room: Room with id '{}' already exists", room.getId());
            throw new IllegalArgumentException("Guest with id '" + room.getId() + "' already exists");
        }

        Room savedGuest = roomRepo.save(room);
        log.info("Room '{}' added successfully", room.getId());
        
        return savedGuest;
    }

    public void changeRoomStatus(Long id, RoomStatus roomStatus) {
        if (id == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }

        Room room = roomRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to find Room: Room with ID '{}' not found", id);
            return new IllegalArgumentException("Room with ID '" + id + "' not found");
        });

        if (room.getStatus() == RoomStatus.OCCUPIED && roomStatus == RoomStatus.REPAIR) {
            log.warn("Cannot change room " + id + " status from OCCUPIED to REPAIR");
            throw new IllegalArgumentException("Room with ID '" + id + "' not found");
        }
        
        RoomStatus oldStatus = room.getStatus();
        room.setStatus(roomStatus);
        roomRepo.save(room);
        log.info("Room {} status changed from {} to {}", id, oldStatus, roomStatus);
    }

    public Room updateRoomPrice(Long id, double newPrice) {
        if (id == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }

        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        Room room = roomRepo.findById(id)
            .orElseThrow(() -> {
                log.warn("Failed to update price: Room '{}' not found", id);
                return new IllegalArgumentException("Room '" + id + "' not found");
            });

        double oldPrice = room.getPrice();
        room.setPrice(newPrice);

        Room updatedRoom = roomRepo.save(room);
        log.info("Room '{}' price updated from {} to {}", id, oldPrice, newPrice);
        
        return updatedRoom;
    }

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    public List<Room> getAllRoomsSortedByPrice() {
        return roomRepo.findAllRoomsSortedByPrice();
    }

    public List<Room> getAllRoomsSortedByCapacity() {
        return roomRepo.findAllRoomsSortedByCapacity();
    }

    public List<Room> getAllRoomsSortedByStars() {
        return roomRepo.findAllRoomsSortedByStarRating();
    }

    public List<Room> getAllAvailableRoomsSortedByPrice() {
        return roomRepo.findAllAvailableRoomsSortedByPrice();
    }

    public List<Room> getAllAvailableRoomsSortedByCapacity() {
        return roomRepo.findAllAvailableRoomsSortedByCapacity();
    }

    public List<Room> getAllAvailableRoomsSortedByStars() {
        return roomRepo.findAllAvailableRoomsSortedByStarRating();
    }

    public Integer getAvailableRoomsCount() {
        return roomRepo.getAvailableRoomsCount();
    }

    public List<Room> getAvailableRoomsByDate(LocalDate localDate) {
        return roomRepo.getAvailableRoomsByDate(localDate);
    }

    private void validateRoomParameters(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        if (room.getId() == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
    }
}
