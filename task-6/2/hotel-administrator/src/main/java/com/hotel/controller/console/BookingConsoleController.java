package com.hotel.controller.console;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.hotel.controller.BookingController;
import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.service.BookingFacade;

public class BookingConsoleController extends BookingController {
    private static final String FILE_PATH = "bookings.csv";
    private final Scanner scanner = InputManager.getInstance().getScanner();

    public BookingConsoleController(BookingFacade bookingFacade) {
        super(bookingFacade);
    }

    public Booking bookRoom() {
        checkOutExpiredBookings();

        System.out.println("Create a new book room");

        System.out.print("Enter Room ID (Long): ");
        Long id = scanner.nextLong();

        System.out.print("Enter number of guests: ");
        int count = scanner.nextInt();

        List<Long> guestIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            System.out.print("Enter ID of guest " + (i + 1) + ": ");
            Long guestId = scanner.nextLong();
            guestIds.add(guestId);
        }

        scanner.nextLine().trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Enter a date to in (yyyy-MM-dd): ");
        LocalDate checkInDate = LocalDate.parse(scanner.nextLine(), formatter);

        System.out.print("Enter a date to out (yyyy-MM-dd): ");
        LocalDate checkOutDate = LocalDate.parse(scanner.nextLine(), formatter);

        try {
            Booking booking = bookRoom(id, guestIds, checkInDate, checkOutDate);
            
            System.out.println("\nBooking created successfully:");
            System.out.println(booking);

            return booking;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Double calculateTotalPaymentForBooking() {
        System.out.print("Enter Book ID (Long): ");
        Long id = scanner.nextLong();

        try {
            return calculateTotalPaymentForBooking(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Guest> getLastThreeGuestsByRoom() {
        System.out.print("Enter Room ID (Long): ");
        Long id = scanner.nextLong();

        try {
            return getLastThreeGuestsByRoom(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void evictGuestsFromRoom() {
        System.out.print("Enter Room ID (Long): ");
        Long id = scanner.nextLong();

        try {
            evictGuestsFromRoom(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void importFromCsv() {
        importFromCsv(FILE_PATH);
    }

    public void exportToCsv() {
        exportToCsv(FILE_PATH);
    }
}
