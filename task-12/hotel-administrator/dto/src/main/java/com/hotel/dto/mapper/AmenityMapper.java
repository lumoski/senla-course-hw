package com.hotel.dto.mapper;

import com.hotel.core.model.entity.Amenity;
import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.response.AmenityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AmenityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Amenity toEntity(AmenityCreateDTO amenityDTO);

    Amenity toEntity(AmenityDTO amenityDTO);

    AmenityDTO toDTO (Amenity amenity);

    List<AmenityDTO> toDTOList (List<Amenity> amenityList);
}
