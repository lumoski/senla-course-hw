package com.hotel.dto.mapper;

import com.hotel.core.model.entity.Room;
import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.response.RoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Room toEntity(RoomCreateDTO roomDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Room toEntity(RoomDTO roomDTO);

    RoomDTO toDTO(Room room);

    List<RoomDTO> toDTOList(List<Room> rooms);
}
