package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.RoomRepository;
import com.hotel.utils.UtilityClass;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for managing hotel rooms.
 * Handles business logic for creating and updating hotel rooms.
 */
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room findById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> {
            log.warn("Failed to find Room: Room with ID '{}' not found", id);
            return new IllegalArgumentException("Room with ID '" + id + "' not found");
        });

        return room;
    }

    public String getRoomDetails(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> {
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
                room.getStatus().toString());
    }

    public Room addRoom(Room room) {
        roomRepository.findById(room.getId()).ifPresent(existingRoom -> {
            String errorMessage = String.format("Room with ID '%d' already exists", room.getId());
            log.warn("Failed to add room: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        });

        Room savedRoom = roomRepository.save(room);

        log.info("Room '{}' added successfully", room.getId());
        return savedRoom;
    }

    public Room changeRoomStatus(Long id, RoomStatus newStatus) {
        Room room = roomRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format("Room with ID '%d' not found", id);
            log.warn("Failed to find Room: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        });

        if (room.getStatus() == RoomStatus.OCCUPIED && newStatus == RoomStatus.REPAIR) {
            String errorMessage = String.format("Cannot change room %d status from OCCUPIED to REPAIR", id);
            log.warn(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        RoomStatus oldStatus = room.getStatus();
        room.setStatus(newStatus);

        Room updatedRoom = roomRepository.save(room);
        log.info("Room {} status changed from {} to {}", id, oldStatus, newStatus);

        return updatedRoom;
    }

    public Room updateRoomPrice(Long id, double newPrice) {
        Room room = roomRepository.findById(id).orElseThrow(() -> {
            log.warn("Failed to update price: Room '{}' not found", id);
            return new IllegalArgumentException("Room '" + id + "' not found");
        });

        double oldPrice = room.getPrice();
        room.setPrice(newPrice);

        Room updatedRoom = roomRepository.save(room);
        log.info("Room '{}' price updated from {} to {}", id, oldPrice, newPrice);

        return updatedRoom;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAllRoomsSortedByPrice() {
        return roomRepository.findAllRoomsSortedByPrice();
    }

    public List<Room> getAllRoomsSortedByCapacity() {
        return roomRepository.findAllRoomsSortedByCapacity();
    }

    public List<Room> getAllRoomsSortedByStars() {
        return roomRepository.findAllRoomsSortedByStarRating();
    }

    public List<Room> getAllAvailableRoomsSortedByPrice() {
        return roomRepository.findAllAvailableRoomsSortedByPrice();
    }

    public List<Room> getAllAvailableRoomsSortedByCapacity() {
        return roomRepository.findAllAvailableRoomsSortedByCapacity();
    }

    public List<Room> getAllAvailableRoomsSortedByStars() {
        return roomRepository.findAllAvailableRoomsSortedByStarRating();
    }

    public Integer getAvailableRoomsCount() {
        return roomRepository.getAvailableRoomsCount();
    }

    public List<Room> getAvailableRoomsByDate(LocalDate localDate) {
        return roomRepository.getAvailableRoomsByDate(localDate);
    }

    public void importFromCsv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 5) {
                    throw new IllegalArgumentException("Invalid CSV format");
                }

                Long id = Long.parseLong(fields[0]);
                double price = Double.parseDouble(fields[1]);
                int capacity = Integer.parseInt(fields[2]);
                int starRating = Integer.parseInt(fields[3]);
                RoomStatus status = RoomStatus.valueOf(fields[4]);

                Room room = new Room(id, price, capacity, starRating, status);
                addRoom(room);
            }
            System.out.println("Rooms exported successfully from " + filePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error importing rooms from CSV: " + e.getMessage());
        }
    }

    public void exportToCsv(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write(UtilityClass.getFieldNames(Room.class));
            writer.newLine();

            for (Room room : roomRepository.findAll()) {
                String line = String.format("%d,%.2f,%d,%d,%s",
                        room.getId(),
                        room.getPrice(),
                        room.getCapacity(),
                        room.getStarRating(),
                        room.getStatus().name());
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Rooms exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting rooms to CSV: " + e.getMessage());
        }
    }
}
