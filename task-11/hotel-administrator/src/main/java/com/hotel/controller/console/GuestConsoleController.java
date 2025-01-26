package com.hotel.controller.console;

import java.time.LocalDate;

import com.hotel.controller.GuestController;
import com.hotel.model.Guest;
import com.hotel.utils.InputUtils;

public class GuestConsoleController extends GuestController {
    private static final String FILE_PATH = "guests.csv";

    public GuestConsoleController() {
        super();
    }

    public Guest addGuest() {
        System.out.println("Create a new Guest");
    
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
    
        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = InputUtils.readDate();
    
        Guest guest = new Guest(id, firstName, lastName, email, phoneNumber, date);
    
        try {
            addGuest(guest);
            
            System.out.println("\nGuest created successfully:");
            System.out.println(guest);

            return guest;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Guest updateGuest() {
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
    
        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = InputUtils.readDate();
    
        Guest guest = new Guest(id, firstName, lastName, email, phoneNumber, date);
    
        try {
            updateGuest(id, guest);
            
            System.out.println("\nGuest created successfully:");
            System.out.println(guest);

            return guest;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void importFromCsv() {
        importFromCsv(FILE_PATH);
    }

    public void exportToCsv() {
        exportToCsv(FILE_PATH);
    }
}
