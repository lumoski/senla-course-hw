package com.hotel.dto.mapper;

import com.hotel.core.model.entity.Guest;
import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface GuestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Guest toEntity(GuestCreateDTO guestDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Guest toEntity(GuestUpdateDTO guestDTO);

    GuestDTO toDTO(Guest guest);

    List<GuestDTO> toDTOList(List<Guest> guests);
}

