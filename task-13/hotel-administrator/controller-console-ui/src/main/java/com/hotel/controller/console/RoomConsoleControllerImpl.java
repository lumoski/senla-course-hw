package com.hotel.controller.console;

import java.time.LocalDate;
import java.util.List;

import com.hotel.controller.api.RoomController;
import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.ConsoleInputUtil;
import com.hotel.service.api.RoomService;

public class RoomConsoleControllerImpl implements RoomController {

    @Inject
    private RoomService roomService;

    @Override
    public RoomDTO createRoom(RoomCreateDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @Override
    public RoomDTO updateRoomStatus(RoomUpdateStatusDTO roomUpdateStatusDTO) {
        return roomService.updateRoomStatus(roomUpdateStatusDTO);
    }

    @Override
    public RoomDTO updateRoomPrice(RoomUpdatePriceDTO roomUpdatePriceDTO) {
        return roomService.updateRoomPrice(roomUpdatePriceDTO);
    }

    @Override
    public List<RoomDTO> findAllRooms() {
        return roomService.findAllRooms();
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByPrice() {
        return roomService.findAllRoomsSortedByPrice();
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByCapacity() {
        return roomService.findAllRoomsSortedByCapacity();
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByStars() {
        return roomService.findAllRoomsSortedByStars();
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByPrice() {
        return roomService.findAllAvailableRoomsSortedByPrice();
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByCapacity() {
        return roomService.findAllAvailableRoomsSortedByCapacity();
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByStars() {
        return roomService.findAllAvailableRoomsSortedByStars();
    }

    @Override
    public List<RoomDTO> findAvailableRoomsByDate(LocalDate date) {
        return roomService.findAvailableRoomsByDate(date);
    }

    @Override
    public Integer findAvailableRoomsCount() {
        return roomService.findAvailableRoomsCount();
    }

    @Override
    public void exportToCsv() {
        roomService.exportToCsv();
    }

    @Override
    public void importFromCsv() {
        // TODO:
    }

    public RoomDTO createRoom() {
        System.out.println("Create a new Room");

        System.out.print("Enter Room Number: ");
        String roomNumber = ConsoleInputUtil.readString();

        System.out.print("Enter Room Price (double): ");
        double price = ConsoleInputUtil.readDouble();

        System.out.print("Enter Room Capacity (int): ");
        int capacity = ConsoleInputUtil.readInt();

        System.out.print("Enter Room Stars (int): ");
        int stars = ConsoleInputUtil.readInt();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        String status = ConsoleInputUtil.readString();

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
        Long id = ConsoleInputUtil.readLong();

        System.out.println("Enter Room Status (AVAILABLE, OCCUPIED, REPAIR): ");
        String status = ConsoleInputUtil.readString();

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
        Long id = ConsoleInputUtil.readLong();

        System.out.print("Enter Room new price: ");
        double newPrice = ConsoleInputUtil.readDouble();

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
        LocalDate date = ConsoleInputUtil.readDate();

        return findAvailableRoomsByDate(date);
    }
}
