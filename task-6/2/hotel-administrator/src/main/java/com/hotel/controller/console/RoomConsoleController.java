package com.hotel.controller.console;

import com.hotel.controller.RoomController;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.service.RoomService;
import com.hotel.utils.InputUtils;

public class RoomConsoleController extends RoomController {
    private static final String FILE_PATH = "rooms.csv";

    public RoomConsoleController(RoomService roomService) {
        super(roomService);
    }

    public Room addRoom() {
        System.out.println("Create a new Room");

        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.print("Enter Room Price (double): ");
        double price = InputUtils.readDouble();

        System.out.print("Enter Room Capacity (int): ");
        int capacity = InputUtils.readInt();

        System.out.print("Enter Room Stars (int): ");
        int stars = InputUtils.readInt();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        RoomStatus status = InputUtils.readRoomStatus();

        Room room = new Room(id, price, capacity, stars, status);

        try {
            addRoom(room);

            System.out.println("\nRoom created successfully:");
            System.out.println(room);

            return room;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Room changeRoomStatus() {
        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        RoomStatus status = InputUtils.readRoomStatus();

        try {
            return changeRoomStatus(id, status);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Room updateRoomPrice() {
        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.print("Enter Room new price: ");
        double newPrice = InputUtils.readDouble();

        try {
            return updateRoomPrice(id, newPrice);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void importFromCsv() {
        importFromCsv(FILE_PATH);
    }

    public void exportToCsv() {
        exportToCsv(FILE_PATH);
    }
}
