package com.hotel.controller.console;

import com.hotel.controller.RoomController;
import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.util.InputUtils;
import com.hotel.model.entity.Room;

import java.time.LocalDate;
import java.util.List;

public class RoomConsoleController extends RoomController {
    public RoomConsoleController() {
        super();
    }

    public RoomDTO createRoom() {
        System.out.println("Create a new Room");

        System.out.print("Enter Room Number: ");
        String roomNumber = InputUtils.readString();

        System.out.print("Enter Room Price (double): ");
        double price = InputUtils.readDouble();

        System.out.print("Enter Room Capacity (int): ");
        int capacity = InputUtils.readInt();

        System.out.print("Enter Room Stars (int): ");
        int stars = InputUtils.readInt();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        String status = InputUtils.readString();

        try {
            RoomCreateDTO room = new RoomCreateDTO(roomNumber, price, capacity, stars, status);
            return createRoom(room);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RoomDTO updateRoomStatus() {
        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        String status = InputUtils.readString();

        try {
            RoomUpdateStatusDTO room = new RoomUpdateStatusDTO(id, status);
            return updateRoomStatus(room);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RoomDTO updateRoomPrice() {
        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.print("Enter Room new price: ");
        double newPrice = InputUtils.readDouble();

        try {
            RoomUpdatePriceDTO room = new RoomUpdatePriceDTO(id, newPrice);
            return updateRoomPrice(room);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<RoomDTO> findAvailableRoomsByDate() {
        System.out.print("Enter a date to in (yyyy-MM-dd): ");
        LocalDate date = InputUtils.readDate();

        return findAvailableRoomsByDate(date);
    }
}
