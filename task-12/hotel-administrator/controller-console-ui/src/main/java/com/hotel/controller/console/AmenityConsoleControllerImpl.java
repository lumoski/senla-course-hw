package com.hotel.controller.console;

import java.util.List;

import com.hotel.controller.api.AmenityController;
import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdatePriceDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.ConsoleInputUtil;
import com.hotel.service.api.AmenityService;

public class AmenityConsoleControllerImpl implements AmenityController {

    @Inject
    private AmenityService amenityService;

    @Override
    public AmenityDTO createAmenity(AmenityCreateDTO amenityDTO) {
        return amenityService.createAmenity(amenityDTO);
    }

    @Override
    public AmenityDTO updateAmenityPrice(AmenityUpdatePriceDTO amenityUpdatePriceDTO) {
        return amenityService.updateAmenityPrice(amenityUpdatePriceDTO);
    }

    @Override
    public List<AmenityDTO> findAllAmenities() {
        return amenityService.findAllAmenities();
    }

    @Override
    public List<AmenityDTO> findAllAmenitiesSortedByPrice() {
        return amenityService.findAllAmenitiesSortedByPrice();
    }

    @Override
    public void exportToCsv() {
        amenityService.exportToCsv();
    }

    @Override
    public void importFromCsv() {
        // TODO:
    }

    public AmenityDTO createAmenity() {
        System.out.println("Create a new Amenity");

        try {
            System.out.print("Enter Amenity first name: ");
            String name = ConsoleInputUtil.readString();

            System.out.print("Enter Amenity price (Long): ");
            double price = ConsoleInputUtil.readDouble();

            System.out.print("Enter Amenity category: ");
            String category = ConsoleInputUtil.readString();

            AmenityCreateDTO amenityCreateDTO = new AmenityCreateDTO(name, price, category);
            AmenityDTO amenityDTO = createAmenity(amenityCreateDTO);

            System.out.println("\nAmenity created successfully:");
            System.out.println(amenityDTO);

            return amenityDTO;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public AmenityDTO updateAmenityPrice() {
        System.out.print("Enter Amenity ID (Long): ");
        Long id = ConsoleInputUtil.readLong();

        System.out.print("Enter new Amenity price (Long): ");
        double newPrice = ConsoleInputUtil.readDouble();

        try {
            AmenityUpdatePriceDTO amenityUpdateDTO = new AmenityUpdatePriceDTO(id, newPrice);
            AmenityDTO amenityDTO = updateAmenityPrice(amenityUpdateDTO);

            System.out.println("\nAmenity updated successfully:");
            System.out.println(amenityDTO);

            return amenityDTO;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
