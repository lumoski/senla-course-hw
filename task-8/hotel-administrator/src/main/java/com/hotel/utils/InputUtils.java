package com.hotel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.hotel.controller.console.InputManager;
import com.hotel.model.RoomStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InputUtils {
    
    private static final Scanner scanner = InputManager.getInstance().getScanner();

    public static String readString() {
        return scanner.nextLine();
    }

    public static int readInt() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
            }
        }
    }

    public static double readDouble() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid decimal number.");
            }
        }
    }

    public static long readLong() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid long integer.");
            }
        }
    }

    public static LocalDate readDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            try {
                String input = scanner.nextLine();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Please enter a date in the format yyyy-MM-dd.");
            }
        }
    }

    public static RoomStatus readRoomStatus() {
        while (true) {
            try {
                String statusInput = InputUtils.readString().toUpperCase();
                return RoomStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid Room Status. Please enter one of the following: AVAILABLE, OCCUPIED, REPAIR.");
            }
        }
    }
}
