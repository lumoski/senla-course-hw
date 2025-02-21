package com.hotel.service.impl;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.exception.RoomNotFoundException;
import com.hotel.core.exception.RoomStatusChangeException;
import com.hotel.core.model.entity.Room;
import com.hotel.core.model.enums.RoomStatus;
import com.hotel.dto.mapper.RoomMapper;
import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.configurator.ConfigLoader;
import com.hotel.framework.configurator.ConfigProperty;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.CsvExporter;
import com.hotel.repository.api.RoomRepository;
import com.hotel.service.api.RoomService;

@Slf4j
public class RoomServiceImpl implements RoomService, CsvExporter.CsvConverter<Room> {

    @Inject
    private RoomRepository roomRepository;

    @Inject
    RoomMapper roomMapper;

    @ConfigProperty
    private String csvFilePath;

    public RoomServiceImpl() {
        ConfigLoader.initialize(this, "service.properties");
    }

    @Override
    public RoomDTO findById(Long id) throws RoomNotFoundException {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Room '{}' not found", id);
                    return new RoomNotFoundException(id);
                }
        );
        return roomMapper.toDTO(room);
    }

    @Override
    public RoomDTO createRoom(RoomCreateDTO roomCreateDTO) {
        Room room = roomMapper.toEntity(roomCreateDTO);
        Room savedRoom = roomRepository.save(room);
        RoomDTO savedRoomDTO = roomMapper.toDTO(savedRoom);

        log.debug("Room '{}' created successfully", savedRoom.getId());

        return savedRoomDTO;
    }

    @Override
    public RoomDTO updateRoomStatus(RoomUpdateStatusDTO roomUpdateStatusDTO) throws RoomStatusChangeException {
        Long id = roomUpdateStatusDTO.id();
        RoomStatus newStatus = RoomStatus.valueOf(roomUpdateStatusDTO.status());

        Room room = roomMapper.toEntity(findById(id));

        if (room.getStatus() == RoomStatus.OCCUPIED && newStatus == RoomStatus.REPAIR) {
            String errorMessage = String.format("Cannot change room %d status from OCCUPIED to REPAIR", id);
            log.error(errorMessage);
            throw new RoomStatusChangeException(errorMessage);
        }

        RoomStatus oldStatus = room.getStatus();
        room.setStatus(newStatus);

        Room updatedRoom = roomRepository.updateStatus(room);
        RoomDTO updatedRoomDTO = roomMapper.toDTO(updatedRoom);

        log.debug("Room '{}' status changed from {} to {}", id, oldStatus, newStatus);

        return updatedRoomDTO;
    }

    @Override
    public RoomDTO updateRoomPrice(RoomUpdatePriceDTO roomUpdatePriceDTO) {
        Long id = roomUpdatePriceDTO.id();
        double newPrice = roomUpdatePriceDTO.price();

        Room room = roomMapper.toEntity(findById(id));

        double oldPrice = room.getPrice();
        room.setPrice(newPrice);

        Room updatedRoom = roomRepository.updatePrice(room);
        RoomDTO updatedRoomDTO = roomMapper.toDTO(updatedRoom);

        log.debug("Room '{}' price updated from {} to {}", id, oldPrice, newPrice);

        return updatedRoomDTO;
    }

    @Override
    public List<RoomDTO> findAllRooms() {
        return roomMapper.toDTOList(
                roomRepository.findAll()
        );
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByPrice() {
        return roomMapper.toDTOList(
                roomRepository.findAllRoomsSortedByPrice()
        );
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByCapacity() {
        return roomMapper.toDTOList(
                roomRepository.findAllRoomsSortedByCapacity()
        );
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByStars() {
        return roomMapper.toDTOList(
                roomRepository.findAllRoomsSortedByStarRating()
        );
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByPrice() {
        return roomMapper.toDTOList(
                roomRepository.findAllAvailableRoomsSortedByPrice()
        );
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByCapacity() {
        return roomMapper.toDTOList(
                roomRepository.findAllAvailableRoomsSortedByCapacity()
        );
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByStars() {
        return roomMapper.toDTOList(
                roomRepository.findAllAvailableRoomsSortedByStarRating()
        );
    }

    @Override
    public Integer findAvailableRoomsCount() {
        return roomRepository.findAvailableRoomsCount();
    }

    @Override
    public List<RoomDTO> findAvailableRoomsByDate(LocalDate date) {
        return roomMapper.toDTOList(roomRepository.findAvailableRoomsByDate(date));
    }

    @Override
    public void exportToCsv() {
        CsvExporter<Room> exporter = new CsvExporter<>(csvFilePath);
        exporter.export(
                roomRepository.findAll(),
                new RoomServiceImpl()
        );

        log.debug("Rooms '{}' exported successfully", csvFilePath);
    }

    @Override
    public String getCsvHeaderLine() {
        String[] header = {"id", "room_number", "price", "capacity", "star_rating", "status"};
        return String.join(",", header);
    }

    @Override
    public String getCsvDataLine(Room item) {
        return String.format("%s,%s,%s,%s,%s,%s",
                item.getId(),
                item.getRoomNumber(),
                item.getPrice(),
                item.getCapacity(),
                item.getStarRating(),
                item.getStatus().name()
        );
    }
}
