package com.hotel.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hotel.core.model.domain.Amenity;
import com.hotel.database.entity.AmenityEntity;
import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.response.AmenityDTO;

@Mapper(componentModel = "spring")
public interface AmenityMapper {

    @Mapping(target = "id", ignore = true)
    Amenity toDomain(AmenityCreateDTO amenityDTO);

    Amenity toDomain(AmenityDTO amenityDTO);

    Amenity toDomain(AmenityEntity amenityEntity);

    AmenityEntity toEntity(Amenity amenity);

    @Mapping(target = "id", ignore = true)
    AmenityEntity toEntity(AmenityCreateDTO amenityDTO);

    AmenityDTO toDTO (Amenity amenity);

    AmenityDTO toDTO (AmenityEntity amenityEntity);

    List<AmenityDTO> toDTOListFromDomain (List<Amenity> amenityList);

    List<AmenityDTO> toDTOListFromEntity (List<AmenityEntity> amenityEntityList);

    List<Amenity> toDomainListFromDTO (List<AmenityDTO> amenityDTOList);

    List<Amenity> toDomainListFromEntity (List<AmenityEntity> amenityEntityList);

    List<AmenityEntity> toEntityListFromDomain (List<Amenity> amenityList);

    List<AmenityEntity> toEntityListFromDTO (List<AmenityDTO> amenityDTOList);
}
