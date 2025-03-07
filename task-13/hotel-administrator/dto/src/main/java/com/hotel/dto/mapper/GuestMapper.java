package com.hotel.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hotel.core.model.domain.Guest;
import com.hotel.database.entity.GuestEntity;
import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;

@Mapper
public interface GuestMapper {

    @Mapping(target = "id", ignore = true)
    Guest toDomain(GuestCreateDTO guestDTO);

    Guest toDomain(GuestUpdateDTO guestDTO);

    Guest toDomain(GuestEntity guestEntity);

    GuestEntity toEntity(Guest guest);

    @Mapping(target = "id", ignore = true)
    GuestEntity toEntityFromCreateDTO(GuestCreateDTO guestCreateDTO);

    GuestEntity toEntityFromUpdateDTO(GuestUpdateDTO guestUpdateDTO);

    GuestDTO toDTO(Guest guest);

    GuestDTO toDTOFromEntity(GuestEntity guestEntity);

    List<GuestDTO> toDTOListFromDomain(List<Guest> guestList);

    List<GuestDTO> toDTOListFromEntity(List<GuestEntity> guestEntityList);

    List<Guest> toDomainListFromEntity(List<GuestEntity> guestEntityList);
}

