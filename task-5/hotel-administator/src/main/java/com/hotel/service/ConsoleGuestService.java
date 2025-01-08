package com.hotel.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;

public class ConsoleGuestService extends GuestService {
    private final Scanner scanner;

    public ConsoleGuestService(GuestRepository guestRepo, Scanner scanner) {
        super(guestRepo);
        this.scanner = scanner;
    }

    public Guest consoleAddGuest() {
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

        addGuest(guest);
    
        return guest;
    }

    public Guest consoleUpdateGuest() {
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
    
        System.out.println("\nGuest created successfully:");
        System.out.println(guest);

        updateGuest(id, guest);
    
        return guest;
    }
}    