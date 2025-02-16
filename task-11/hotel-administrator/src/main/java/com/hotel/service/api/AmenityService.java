package com.hotel.service.api;

import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdateDTO;
import com.hotel.dto.response.AmenityDTO;

import java.util.List;

public interface AmenityService {
    AmenityDTO findById(Long id);

    List<AmenityDTO> findAllAmenities();

    List<AmenityDTO> findAllAmenitiesSortedByPrice();

    AmenityDTO createAmenity(AmenityCreateDTO amenityCreateDTO);

    AmenityDTO updateAmenityPrice(AmenityUpdateDTO amenityUpdateDTO);

    void exportToCsv();
}
