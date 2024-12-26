package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.RoomRepository;

import java.util.List;


public class RoomService {
    private final RoomRepository roomRepo;

    public RoomService(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public void checkIn(int roomId, String guestName) {
        Room room = roomRepo.findById(roomId);

        if (room != null && room.getStatus() == RoomStatus.AVAILABLE) {
            room.setGuestName(guestName);
            room.setStatus(RoomStatus.OCCUPIED);
            roomRepo.save(room);
            System.out.println("Guest " + guestName + " checked into room " + roomId);
        } else {
            System.out.println("Room " + roomId + " is not available");
        }
    }

    public void checkOut(int roomId) {
        Room room = roomRepo.findById(roomId);

        if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
            room.setGuestName(null);
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepo.save(room);
            System.out.println("Guest checked out from room " + roomId);
        } else {
            System.out.println("Room " + roomId + " is not occupied");
        }
    }

    public void changeRoomStatus(int roomId, RoomStatus roomStatus) {
        Room room = roomRepo.findById(roomId);

        if (room == null) {
            System.out.println("Room " + roomId + " not found");
            return;
        }

        if (room.getStatus() == RoomStatus.OCCUPIED && roomStatus == RoomStatus.REPAIR) {
            System.out.println("Cannot change room " + roomId + " status from OCCUPIED to REPAIR");
            return;
        }
        
        room.setStatus(roomStatus);
        roomRepo.save(room);
        System.out.println("Room " + roomId + " status changed to " + roomStatus);
    }

    public void changeRoomPrice(int roomId, double price) {
        Room room = roomRepo.findById(roomId);
        
        if (room == null) {
            System.out.println("Room " + roomId + " not found");
            return;
        }

        room.setPrice(price);
        roomRepo.save(room);
        System.out.println("Room " + roomId + " has a new price " + price);
    }

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    public void addRoom(int roomId, double price) {
        roomRepo.save(new Room(roomId, price, RoomStatus.AVAILABLE, null));
        System.out.println("Room " + roomId + " added");
    }
}
