package com.hotel.service.api;

import java.util.List;

import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdatePriceDTO;
import com.hotel.dto.response.AmenityDTO;

public interface AmenityService {
    AmenityDTO findById(Long id);

    List<AmenityDTO> findAllAmenities();

    List<AmenityDTO> findAllAmenitiesSortedByPrice();

    AmenityDTO createAmenity(AmenityCreateDTO amenityCreateDTO);

    AmenityDTO updateAmenityPrice(AmenityUpdatePriceDTO amenityUpdateDTO);

    void exportToCsv();
}
