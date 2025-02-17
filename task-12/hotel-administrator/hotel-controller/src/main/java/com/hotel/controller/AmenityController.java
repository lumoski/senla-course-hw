package com.hotel.controller;

import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdateDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.service.api.AmenityService;

import java.util.List;

public class AmenityController {

    @Inject
    private AmenityService amenityService;

    public AmenityDTO createAmenity(AmenityCreateDTO amenityCreateDTO) {
        return amenityService.createAmenity(amenityCreateDTO);
    }

    public AmenityDTO updateAmenityPrice(AmenityUpdateDTO amenityUpdateDTO) {
        return amenityService.updateAmenityPrice(amenityUpdateDTO);
    }

    public List<AmenityDTO> findAllAmenities() {
        return amenityService.findAllAmenities();
    }

    public List<AmenityDTO> findAllAmenitiesSortedByPrice() {
        return amenityService.findAllAmenitiesSortedByPrice();
    }

    public void exportToCsv() {
        amenityService.exportToCsv();
    }
}