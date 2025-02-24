package com.hotel.framework.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConsoleInputUtil {

    private static final Scanner scanner = new Scanner(System.in);

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

    public static void closeScanner() {
        scanner.close();
        log.info("Scanner closed.");
    }
}