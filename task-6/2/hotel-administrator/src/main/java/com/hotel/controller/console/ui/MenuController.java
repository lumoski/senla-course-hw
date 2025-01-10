package com.hotel.controller.console.ui;

import java.util.Scanner;

import com.hotel.controller.console.InputManager;

public class MenuController {
    private final Builder builder;
    private final Navigator navigator;
    private final Scanner scanner = InputManager.getInstance().getScanner();

    public MenuController(Builder builder, Navigator navigator) {
        this.builder = builder;
        this.navigator = navigator;
    }

    public void run() {
        Menu rootMenu = builder.buildMenu();
        navigator.setCurrentMenu(rootMenu);

        while (true) {
            navigator.printMenu();
            System.out.println("Enter your choice:");

            try {
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    System.out.println();
                    navigator.navigate(choice);
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }
    
            System.out.println();
        }
    }
}
