package com.hotel.controller.console;

import java.time.LocalDate;

import com.hotel.controller.GuestController;
import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.framework.util.InputUtils;

public class GuestConsoleController extends GuestController {
    public GuestConsoleController() {
        super();
    }

    public GuestDTO createGuest() {
        System.out.println("Create a new Guest");
    
        System.out.print("Enter Guest first name: ");
        String firstName = InputUtils.readString();
    
        System.out.print("Enter Guest last name: ");
        String lastName = InputUtils.readString();
    
        System.out.print("Enter Guest email: ");
        String email = InputUtils.readString();

        System.out.print("Enter Guest phone: ");
        String phoneNumber = InputUtils.readString();

        System.out.print("Enter Guest passport number: ");
        String passportNumber = InputUtils.readString();

        System.out.print("Enter Guest gender: ");
        String gender = InputUtils.readString();

        System.out.print("Enter Guest address: ");
        String address = InputUtils.readString();
    
        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = InputUtils.readDate();

        GuestCreateDTO createDTO = new GuestCreateDTO(
                firstName,
                lastName,
                email,
                phoneNumber,
                passportNumber,
                gender,
                address,
                date,
                "NEW"
        );
    
        try {
            GuestDTO savedCreatedGuest = createGuest(createDTO);
            
            System.out.println("\nGuest created successfully:");
            System.out.println(createDTO);

            return savedCreatedGuest;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public GuestDTO updateGuest() {
        System.out.println("Update a Guest");
    
        System.out.print("Enter Guest ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.print("Enter Guest first name: ");
        String firstName = InputUtils.readString();

        System.out.print("Enter Guest last name: ");
        String lastName = InputUtils.readString();

        System.out.print("Enter Guest email: ");
        String email = InputUtils.readString();

        System.out.print("Enter Guest phone: ");
        String phoneNumber = InputUtils.readString();

        System.out.print("Enter Guest passport number: ");
        String passportNumber = InputUtils.readString();

        System.out.print("Enter Guest gender: ");
        String gender = InputUtils.readString();

        System.out.print("Enter Guest address: ");
        String address = InputUtils.readString();

        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = InputUtils.readDate();

        GuestUpdateDTO updateDTO = new GuestUpdateDTO(
                id,
                firstName,
                lastName,
                email,
                phoneNumber,
                passportNumber,
                gender,
                address,
                date,
                "NEW"
        );
    
        try {
            GuestDTO savedUpdatedDTO = updateGuest(updateDTO);
            
            System.out.println("\nGuest updated successfully:");

            return savedUpdatedDTO;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
