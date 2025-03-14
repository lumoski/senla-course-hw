package com.hotel.controller.api;

import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoomController {

    RoomDTO createRoom(RoomCreateDTO roomDTO);

    RoomDTO updateRoomStatus(RoomUpdateStatusDTO roomUpdateStatusDTO);

    RoomDTO updateRoomPrice(RoomUpdatePriceDTO roomUpdatePriceDTO);

    List<RoomDTO> findAllRooms();

    List<RoomDTO> findAllRoomsSortedByPrice();

    List<RoomDTO> findAllRoomsSortedByCapacity();

    List<RoomDTO> findAllRoomsSortedByStars();

    List<RoomDTO> findAllAvailableRoomsSortedByPrice();

    List<RoomDTO> findAllAvailableRoomsSortedByCapacity();

    List<RoomDTO> findAllAvailableRoomsSortedByStars();

    List<RoomDTO> findAvailableRoomsByDate(LocalDate date);

    Integer findAvailableRoomsCount();

    void exportToCsv();

    void importFromCsv();
}
