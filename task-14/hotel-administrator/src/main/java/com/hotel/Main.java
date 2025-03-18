package com.hotel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hotel.controller.console.AmenityConsoleControllerImpl;
import com.hotel.controller.console.BookingConsoleControllerImpl;
import com.hotel.controller.console.GuestConsoleControllerImpl;
import com.hotel.controller.console.RoomConsoleControllerImpl;
import com.hotel.controller.console.ui.Builder;
import com.hotel.controller.console.ui.Menu;
import com.hotel.controller.console.ui.MenuController;
import com.hotel.controller.console.ui.Navigator;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.hotel");

        AmenityConsoleControllerImpl amenityConsoleControllerImpl = context.getBean(AmenityConsoleControllerImpl.class);
        RoomConsoleControllerImpl roomConsoleControllerImpl = context.getBean(RoomConsoleControllerImpl.class);
        BookingConsoleControllerImpl bookingConsoleControllerImpl = context.getBean(BookingConsoleControllerImpl.class);
        GuestConsoleControllerImpl guestConsoleControllerImpl = context.getBean(GuestConsoleControllerImpl.class);

        Menu rootMenu = new Menu();
        MenuController menuController = new MenuController(
                new Builder(rootMenu, roomConsoleControllerImpl, guestConsoleControllerImpl, amenityConsoleControllerImpl, bookingConsoleControllerImpl),
                new Navigator(rootMenu)
        );

        menuController.run();
    }
}
