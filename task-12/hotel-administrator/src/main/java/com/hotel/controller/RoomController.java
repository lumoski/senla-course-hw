package com.hotel.controller;

import java.time.LocalDate;
import java.util.List;

import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.service.api.RoomService;

public class RoomController {

    @Inject
    protected RoomService roomService;

    public RoomDTO createRoom(RoomCreateDTO room) throws IllegalArgumentException {
        return roomService.createRoom(room);
    }

    public RoomDTO updateRoomStatus(RoomUpdateStatusDTO room) throws IllegalArgumentException {
        return roomService.updateRoomStatus(room);
    }

    public RoomDTO updateRoomPrice(RoomUpdatePriceDTO room) throws IllegalArgumentException {
        return roomService.updateRoomPrice(room);
    }

    public List<RoomDTO> findAllRooms() {
        return roomService.findAllRooms();
    }

    public List<RoomDTO> findAllRoomsSortedByPrice() {
        return roomService.findAllRoomsSortedByPrice();
    }

    public List<RoomDTO> findAllRoomsSortedByCapacity() {
        return roomService.findAllRoomsSortedByCapacity();
    }

    public List<RoomDTO> findAllRoomsSortedByStars() {
        return roomService.findAllRoomsSortedByStars();
    }

    public List<RoomDTO> findAllAvailableRoomsSortedByPrice() {
        return roomService.findAllAvailableRoomsSortedByPrice();
    }

    public List<RoomDTO> findAllAvailableRoomsSortedByCapacity() {
        return roomService.findAllAvailableRoomsSortedByCapacity();
    }

    public List<RoomDTO> findAllAvailableRoomsSortedByStars() {
        return roomService.findAllAvailableRoomsSortedByStars();
    }

    public Integer findAvailableRoomsCount() {
        return roomService.findAvailableRoomsCount();
    }

    public List<RoomDTO> findAvailableRoomsByDate(LocalDate date) {
        return roomService.findAvailableRoomsByDate(date);
    }

    public void exportToCsv() {
        roomService.exportToCsv();
    }
}