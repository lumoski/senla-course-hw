package com.hotel.controller.console;

import java.util.Scanner;

import com.hotel.controller.RoomController;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.service.RoomService;

public class RoomConsoleController extends RoomController {
    private static final String FILE_PATH = "rooms.csv";
    private final Scanner scanner = InputManager.getInstance().getScanner();

    public RoomConsoleController(RoomService roomService) {
        super(roomService);
    }

    public Room addRoom() {
        System.out.println("Create a new Room");

        System.out.print("Enter Room ID (Long): ");
        Long id = scanner.nextLong();

        System.out.print("Enter Room Price (double): ");
        double price = scanner.nextDouble();

        System.out.print("Enter Room Capacity (int): ");
        int capacity = scanner.nextInt();

        System.out.print("Enter Room Stars (int): ");
        int stars = scanner.nextInt();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        String statusInput = scanner.next().toUpperCase();
        RoomStatus status = RoomStatus.valueOf(statusInput);

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
        Long id = scanner.nextLong();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        String statusInput = scanner.next().toUpperCase();
        RoomStatus status = RoomStatus.valueOf(statusInput);

        try {
            return changeRoomStatus(id, status);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Room updateRoomPrice() {
        System.out.print("Enter Room ID (Long): ");
        Long id = scanner.nextLong();

        System.out.print("Enter Room new price: ");
        double newPrice = scanner.nextDouble();

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
