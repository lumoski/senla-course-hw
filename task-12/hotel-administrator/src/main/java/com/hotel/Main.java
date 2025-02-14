package com.hotel;

import com.hotel.controller.console.AmenityConsoleController;
import com.hotel.controller.console.BookingConsoleController;
import com.hotel.controller.console.GuestConsoleController;
import com.hotel.controller.console.RoomConsoleController;
import com.hotel.controller.console.ui.Builder;
import com.hotel.controller.console.ui.Menu;
import com.hotel.controller.console.ui.MenuController;
import com.hotel.controller.console.ui.Navigator;
import com.hotel.database.DatabaseConnection;
import com.hotel.framework.di.factory.BeanFactory;
import com.hotel.framework.util.InputManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class Main {

    private static MenuController menuController;
    private static RoomConsoleController roomConsoleController;
    private static AmenityConsoleController serviceConsoleController;
    private static BookingConsoleController bookingConsoleController;
    private static GuestConsoleController guestConsoleController;

    static {
        roomConsoleController = BeanFactory.getInstance().getBean(RoomConsoleController.class);
        guestConsoleController = BeanFactory.getInstance().getBean(GuestConsoleController.class);
        serviceConsoleController = BeanFactory.getInstance().getBean(AmenityConsoleController.class);
        bookingConsoleController = BeanFactory.getInstance().getBean(BookingConsoleController.class);

        Menu rootMenu = new Menu("Hotel administrator", new ArrayList<>());
        menuController = new MenuController(
                new Builder(rootMenu, roomConsoleController, guestConsoleController, serviceConsoleController, bookingConsoleController),
                new Navigator(rootMenu)
        );
    }

    public static void main(String[] args) {
        menuController.run();
    }

    public static void closeApp() {
        InputManager.getInstance().close();
        DatabaseConnection.getInstance().closeConnection();

        log.info("Application is closing...");
        System.exit(0);
    }
}