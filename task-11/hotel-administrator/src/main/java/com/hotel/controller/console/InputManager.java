package com.hotel.controller.console;

import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("Scanner closed");
    }
}