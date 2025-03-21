package com.hotel.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.hotel.service.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.exception.RoomNotFoundException;
import com.hotel.core.exception.RoomStatusChangeException;
import com.hotel.core.model.domain.Room;
import com.hotel.core.model.enums.RoomStatus;
import com.hotel.database.entity.RoomEntity;
import com.hotel.dto.mapper.RoomMapper;
import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.request.RoomUpdatePriceDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.util.CsvExporter;
import com.hotel.repository.api.RoomRepository;
import com.hotel.service.api.RoomService;


@Slf4j
@Service
@PropertySource("classpath:service.properties")
public class RoomServiceImpl implements RoomService, CsvExporter.CsvConverter<Room> {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private ServiceConfig serviceConfig;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper, ServiceConfig serviceConfig) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public RoomDTO findById(Long id) throws RoomNotFoundException {
        RoomEntity room = roomRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Room '{}' not found", id);
                    return new RoomNotFoundException(id);
                }
        );
        return roomMapper.toDTOFromEntity(room);
    }

    @Override
    public RoomDTO createRoom(RoomCreateDTO roomCreateDTO) {
        RoomEntity room = roomMapper.toEntityFromCreateDTO(roomCreateDTO);
        RoomEntity savedRoom = roomRepository.save(room);
        RoomDTO savedRoomDTO = roomMapper.toDTOFromEntity(savedRoom);

        log.debug("Room '{}' created successfully", savedRoom.getId());

        return savedRoomDTO;
    }

    @Override
    public RoomDTO updateRoomStatus(RoomUpdateStatusDTO roomUpdateStatusDTO) throws RoomStatusChangeException {
        Long id = roomUpdateStatusDTO.id();
        RoomStatus newStatus = RoomStatus.valueOf(roomUpdateStatusDTO.status());

        RoomEntity room = roomRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Room '{}' not found", id);
                    return new RoomNotFoundException(id);
                }
        );

        if (room.getStatus() == RoomStatus.OCCUPIED && newStatus == RoomStatus.REPAIR) {
            String errorMessage = String.format("Cannot change room %d status from OCCUPIED to REPAIR", id);
            log.error(errorMessage);
            throw new RoomStatusChangeException(errorMessage);
        }

        RoomStatus oldStatus = room.getStatus();
        room.setStatus(newStatus);

        RoomEntity updatedRoom = roomRepository.update(room);
        RoomDTO updatedRoomDTO = roomMapper.toDTOFromEntity(updatedRoom);

        log.debug("Room '{}' status changed from {} to {}", id, oldStatus, newStatus);

        return updatedRoomDTO;
    }

    @Override
    public RoomDTO updateRoomPrice(RoomUpdatePriceDTO roomUpdatePriceDTO) {
        Long id = roomUpdatePriceDTO.id();
        double newPrice = roomUpdatePriceDTO.price();

        RoomEntity room = roomRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Room '{}' not found", id);
                    return new RoomNotFoundException(id);
                }
        );

        double oldPrice = room.getPrice();
        room.setPrice(newPrice);

        RoomEntity updatedRoom = roomRepository.update(room);
        RoomDTO updatedRoomDTO = roomMapper.toDTOFromEntity(updatedRoom);

        log.debug("Room '{}' price updated from {} to {}", id, oldPrice, newPrice);

        return updatedRoomDTO;
    }

    @Override
    public List<RoomDTO> findAllRooms() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAll()
        );
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByPrice() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAllRoomsSortedByPrice()
        );
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByCapacity() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAllRoomsSortedByCapacity()
        );
    }

    @Override
    public List<RoomDTO> findAllRoomsSortedByStars() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAllRoomsSortedByStarRating()
        );
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByPrice() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAllAvailableRoomsSortedByPrice()
        );
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByCapacity() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAllAvailableRoomsSortedByCapacity()
        );
    }

    @Override
    public List<RoomDTO> findAllAvailableRoomsSortedByStars() {
        return roomMapper.toDTOListFromEntity(
                roomRepository.findAllAvailableRoomsSortedByStarRating()
        );
    }

    @Override
    public Integer findAvailableRoomsCount() {
        return roomRepository.findAvailableRoomsCount();
    }

    @Override
    public List<RoomDTO> findAvailableRoomsByDate(LocalDate date) {
        return roomMapper.toDTOListFromEntity(roomRepository.findAvailableRoomsByDate(date));
    }

    @Override
    public void exportToCsv() {
        CsvExporter<Room> exporter = new CsvExporter<>(serviceConfig.getRoomCsvFilePath());
        exporter.export(
                roomMapper.toDomainListFromEntity(roomRepository.findAll()),
                this
        );

        log.debug("Rooms '{}' exported successfully", serviceConfig.getRoomCsvFilePath());
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
