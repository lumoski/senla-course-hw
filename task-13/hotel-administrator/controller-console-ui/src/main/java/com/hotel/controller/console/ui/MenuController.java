package com.hotel.controller.console.ui;

import com.hotel.database.ConnectionManager;
import com.hotel.framework.util.ConsoleInputUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MenuController {

    private Builder builder;
    
    private Navigator navigator;

    public MenuController(Builder builder, Navigator navigator) {
        this.builder = builder;
        this.navigator = navigator;
    }

    public void run() {
        Menu rootMenu = builder.buildMenu();
        navigator.setCurrentMenu(rootMenu);

        log.info("Application is starting...");

        while (true) {
            navigator.printMenu();
            System.out.println("Enter your choice:");

            int choice = ConsoleInputUtil.readInt();
            System.out.println();

            if (navigator.navigate(choice)) {
                close();
                break;
            }
    
            System.out.println();
        }
    }

    public void close() {
        ConsoleInputUtil.closeScanner();
        ConnectionManager.closeConnection();

        log.info("Application is closing...");
    }
}
