package com.hotel.framework.util;

import java.util.Scanner;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputManager {

    @Getter
    private static final InputManager instance = new InputManager();

    @Getter
    private final Scanner scanner;

    private InputManager() {
        this.scanner = new Scanner(System.in);
    }

    public void close() {
        scanner.close();
        log.info("Scanner closed");
    }
}