package com.hotel.service.api;

import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    RoomDTO findById(Long id);

    RoomDTO createRoom(RoomCreateDTO roomCreateDTO);

    RoomDTO updateRoomStatus(RoomUpdateStatusDTO roomUpdateStatusDTO);

    RoomDTO updateRoomPrice(RoomUpdatePriceDTO roomUpdatePriceDTO);

    List<RoomDTO> findAllRooms();

    List<RoomDTO> findAllRoomsSortedByPrice();

    List<RoomDTO> findAllRoomsSortedByCapacity();

    List<RoomDTO> findAllRoomsSortedByStars();

    List<RoomDTO> findAllAvailableRoomsSortedByPrice();

    List<RoomDTO> findAllAvailableRoomsSortedByCapacity();

    List<RoomDTO> findAllAvailableRoomsSortedByStars();

    Integer findAvailableRoomsCount();

    List<RoomDTO> findAvailableRoomsByDate(LocalDate date);

    void exportToCsv();
}
