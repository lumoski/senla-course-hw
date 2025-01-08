package com.hotel.ui;

import java.util.Scanner;

public class MenuController {
    private final Builder builder;
    private final Navigator navigator;

    public MenuController(Builder builder, Navigator navigator) {
        this.builder = builder;
        this.navigator = navigator;
    }

    public void run() {
        Menu rootMenu = builder.buildMenu();
        navigator.setCurrentMenu(rootMenu);

        Scanner scanner = InputManager.getInstance().getScanner();

        while (true) {
            navigator.printMenu();
            System.out.println("Enter your choice:");

            try {
                int choice = scanner.nextInt();
                System.out.println();

                navigator.navigate(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

            System.out.println();
        }
    }
}
