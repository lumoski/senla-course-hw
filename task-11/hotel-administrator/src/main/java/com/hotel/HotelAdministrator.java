package com.hotel;

import java.util.ArrayList;

import com.hotel.controller.console.BookingConsoleController;
import com.hotel.controller.console.GuestConsoleController;
import com.hotel.controller.console.InputManager;
import com.hotel.controller.console.RoomConsoleController;
import com.hotel.controller.console.ServiceConsoleController;

import com.hotel.controller.console.ui.Builder;
import com.hotel.controller.console.ui.Menu;
import com.hotel.controller.console.ui.MenuController;
import com.hotel.controller.console.ui.Navigator;
import com.hotel.database.DatabaseConnection;
import com.hotel.framework.di.factory.BeanFactory;
import com.hotel.utils.EntityManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HotelAdministrator {

    private static EntityManager entityManager;
    private static MenuController menuController;

    private static RoomConsoleController roomConsoleController;
    private static ServiceConsoleController serviceConsoleController;
    private static BookingConsoleController bookingConsoleController;
    private static GuestConsoleController guestConsoleController;

    static {
        entityManager = BeanFactory.getInstance().getBean(EntityManager.class);
        roomConsoleController = BeanFactory.getInstance().getBean(RoomConsoleController.class);
        guestConsoleController = BeanFactory.getInstance().getBean(GuestConsoleController.class);
        serviceConsoleController = BeanFactory.getInstance().getBean(ServiceConsoleController.class);
        bookingConsoleController = BeanFactory.getInstance().getBean(BookingConsoleController.class);

        Menu rootMenu = new Menu("Hotel administrator", new ArrayList<>());
        menuController = new MenuController(
            new Builder(rootMenu, roomConsoleController, guestConsoleController, serviceConsoleController, bookingConsoleController),
            new Navigator(rootMenu)
        );
    }

    public static void main(String[] args) {
        try {
            // entityManager.loadAll();
        } catch (Exception e) {
            log.error("Failed to load data", e);
        }

        menuController.run();
    }

    public static void closeApp() {
        try {
            // entityManager.saveAll();
        } catch (Exception e) {
            log.error("Failed to save data", e);
        }

        InputManager.getInstance().close();
        DatabaseConnection.getInstance().closeConnection();

        log.info("Application is closing...");
        System.exit(0);
    }
}
