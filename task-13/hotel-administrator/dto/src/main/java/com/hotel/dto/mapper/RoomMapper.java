package com.hotel.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hotel.core.model.domain.Room;
import com.hotel.database.entity.RoomEntity;
import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.response.RoomDTO;

@Mapper
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    Room toDomain(RoomCreateDTO roomDTO);

    Room toDomain(RoomDTO roomDTO);

    Room toDomain(RoomEntity roomEntity);
    
    RoomDTO toDTOFromEntity(RoomEntity roomEntity);

    @Mapping(target = "id", ignore = true)
    RoomEntity toEntityFromCreateDTO(RoomCreateDTO roomCreateDTO);

    List<RoomDTO> toDTOListFromEntity(List<RoomEntity> roomEntityList);

    List<Room> toDomainListFromEntity(List<RoomEntity> roomEntityList);

    RoomEntity toEntity(Room room);

    RoomEntity toEntity(RoomDTO roomDTO);

    RoomDTO toDTO(Room room);

    List<RoomDTO> toDTOList(List<Room> rooms);
}
