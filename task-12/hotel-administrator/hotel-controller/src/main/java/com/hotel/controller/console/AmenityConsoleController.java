package com.hotel.controller.console;

import com.hotel.controller.AmenityController;
import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdateDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.framework.util.InputUtils;

public class AmenityConsoleController extends AmenityController {
    public AmenityConsoleController() {
        super();
    }

    public AmenityDTO createAmenity() {
        System.out.println("Create a new Amenity");
    
        try {
            System.out.print("Enter Amenity first name: ");
            String name = InputUtils.readString();

            System.out.print("Enter Amenity price (Long): ");
            double price = InputUtils.readDouble();

            System.out.print("Enter Amenity category: ");
            String category = InputUtils.readString();

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
        Long id = InputUtils.readLong();

        System.out.print("Enter new Amenity price (Long): ");
        double newPrice = InputUtils.readDouble();

        try {
            AmenityUpdateDTO amenityUpdateDTO = new AmenityUpdateDTO(id, newPrice);
            AmenityDTO amenityDTO = updateAmenityPrice(amenityUpdateDTO);

            System.out.println("\nAmenity updated successfully:");
            System.out.println(amenityDTO);

            return amenityDTO;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
