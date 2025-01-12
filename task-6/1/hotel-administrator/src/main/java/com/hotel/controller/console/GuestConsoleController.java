package com.hotel.controller.console;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.hotel.controller.GuestController;
import com.hotel.model.Guest;
import com.hotel.service.GuestService;

public class GuestConsoleController extends GuestController {
    private static final String FILE_PATH = "guests.csv";
    private final Scanner scanner = InputManager.getInstance().getScanner();

    public GuestConsoleController(GuestService guestService) {
        super(guestService);
    }

    public Guest addGuest() {
        System.out.println("Create a new Guest");
    
        System.out.print("Enter Guest ID (Long): ");
        Long id = scanner.nextLong();
        scanner.nextLine();
    
        System.out.print("Enter Guest first name: ");
        String firstName = scanner.nextLine();
    
        System.out.print("Enter Guest last name: ");
        String lastName = scanner.nextLine();
    
        System.out.print("Enter Guest email: ");
        String email = scanner.nextLine();
    
        System.out.print("Enter Guest phone: ");
        String phoneNumber = scanner.nextLine();
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), formatter);
    
        Guest guest = new Guest(id, firstName, lastName, email, phoneNumber, date);
    
        System.out.println("\nGuest created successfully:");
        System.out.println(guest);

        return addGuest(guest);
    }

    public Guest updateGuest() {
        System.out.println("Update a Guest");
    
        System.out.print("Enter Guest ID (Long): ");
        Long id = scanner.nextLong();
        scanner.nextLine();
    
        System.out.print("Enter Guest first name: ");
        String firstName = scanner.nextLine();
    
        System.out.print("Enter Guest last name: ");
        String lastName = scanner.nextLine();
    
        System.out.print("Enter Guest email: ");
        String email = scanner.nextLine();
    
        System.out.print("Enter Guest phone: ");
        String phoneNumber = scanner.nextLine();
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), formatter);
    
        Guest guest = new Guest(id, firstName, lastName, email, phoneNumber, date);
    
        System.out.println("\nGuest updated successfully:");
        System.out.println(guest);

        return updateGuest(id, guest);
    }

    public void importFromCsv() {
        importFromCsv(FILE_PATH);
    }

    public void exportToCsv() {
        exportToCsv(FILE_PATH);
    }
}
