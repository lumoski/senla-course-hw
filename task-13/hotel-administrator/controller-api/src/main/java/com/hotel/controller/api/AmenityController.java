package com.hotel.controller.api;

import java.util.List;

import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdatePriceDTO;
import com.hotel.dto.response.AmenityDTO;

public interface AmenityController {

    AmenityDTO createAmenity(AmenityCreateDTO amenityDTO);

    AmenityDTO updateAmenityPrice(AmenityUpdatePriceDTO amenityUpdatePriceDTO);

    List<AmenityDTO> findAllAmenities();

    List<AmenityDTO> findAllAmenitiesSortedByPrice();

    void exportToCsv();

    void importFromCsv();
}
