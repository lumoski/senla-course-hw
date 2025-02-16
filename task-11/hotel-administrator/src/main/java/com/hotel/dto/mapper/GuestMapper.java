package com.hotel.dto.mapper;

import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.model.entity.Guest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT)
public interface GuestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Guest toEntity(GuestCreateDTO guestDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Guest toEntity(GuestUpdateDTO guestDTO);

    Guest toEntity(GuestDTO guestDTO);

    GuestDTO toDTO(Guest guest);
    List<GuestDTO> toDTOList(List<Guest> guests);
}

