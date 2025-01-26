package com.hotel.controller.console.ui;

import com.hotel.utils.InputUtils;

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

        while (true) {
            navigator.printMenu();
            System.out.println("Enter your choice:");

            int choice = InputUtils.readInt();
            System.out.println();

            navigator.navigate(choice);
    
            System.out.println();
        }
    }
}
