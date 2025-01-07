package com.hotel.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.hotel.model.Booking;
import com.hotel.model.Guest;

public class ConsoleBookingFacade extends BookingFacade {
    private final Scanner scanner;

    public ConsoleBookingFacade(RoomService roomService, GuestService guestService, BookingService bookingService, Scanner scanner) {
        super(roomService, guestService, bookingService);
        this.scanner = scanner;
    }

    public Booking consoleBookRoom() {
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

        return bookRoom(id, guestIds, checkInDate, checkOutDate);
    }

    public Double consoleCalculateTotalPaymentForBooking() {
        System.out.print("Enter Book ID (Long): ");
        Long id = scanner.nextLong();

        return calculateTotalPaymentForBooking(id);
    }

    public List<Guest> consoleGetLastThreeGuestsByRoom() {
        System.out.print("Enter Room ID (Long): ");
        Long id = scanner.nextLong();

        return getLastThreeGuestsByRoom(id);
    }
}    