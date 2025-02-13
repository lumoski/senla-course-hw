package com.hotel.dto.mapper;

import com.hotel.dto.request.RoomCreateDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.model.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT)
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
