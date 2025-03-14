package com.hotel;

import com.hotel.controller.console.ui.Builder;
import com.hotel.controller.console.ui.Menu;
import com.hotel.controller.console.ui.MenuController;
import com.hotel.controller.console.ui.Navigator;

public class Main {

    private static MenuController menuController;

    static {
        Menu rootMenu = new Menu();
        menuController = new MenuController(
                new Builder(rootMenu),
                new Navigator(rootMenu)
        );
    }

    public static void main(String[] args) {
        menuController.run();
    }
}