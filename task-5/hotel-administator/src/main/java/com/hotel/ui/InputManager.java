package com.hotel.ui;

import java.util.Scanner;

public class InputManager {
    private static final InputManager instance = new InputManager();
    private final Scanner scanner;

    public Scanner getScanner() {
        return scanner;
    }

    private InputManager() {
        this.scanner = new Scanner(System.in);
    }

    public static InputManager getInstance() {
        return instance;
    }

    public void close() {
        scanner.close();
    }
}