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

import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.ServiceRepository;

import com.hotel.repository.impl.InMemoryBookingRepository;
import com.hotel.repository.impl.InMemoryGuestRepository;
import com.hotel.repository.impl.InMemoryRoomRepository;
import com.hotel.repository.impl.InMemoryServiceRepository;

import com.hotel.service.BookingFacade;
import com.hotel.service.BookingService;
import com.hotel.service.GuestService;
import com.hotel.service.HotelServiceService;
import com.hotel.service.RoomService;
import com.hotel.utils.EntityManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HotelAdministrator {
    private static final RoomConsoleController roomConsoleController;
    private static final ServiceConsoleController serviceConsoleController;
    private static final GuestConsoleController guestConsoleController;
    private static final BookingConsoleController bookingConsoleController;

    private static final Menu mainMenu;
    private static final Builder builder;
    private static final Navigator navigator;
    private static final MenuController menuController;

    private static final EntityManager entityManager;

    static {
        ServiceRepository serviceRepository = new InMemoryServiceRepository();
        GuestRepository guestRepository = new InMemoryGuestRepository();
        BookingRepository bookingRepository = new InMemoryBookingRepository();  
        RoomRepository roomRepository = new InMemoryRoomRepository(bookingRepository);

        RoomService roomService = new RoomService(roomRepository);
        HotelServiceService hotelServiceService = new HotelServiceService(serviceRepository);
        GuestService guestService = new GuestService(guestRepository);
        BookingService bookingService = new BookingService(bookingRepository);
        BookingFacade bookingFacade = new BookingFacade(roomService, guestService, bookingService);

        roomConsoleController = new RoomConsoleController(roomService);
        serviceConsoleController = new ServiceConsoleController(hotelServiceService);
        guestConsoleController = new GuestConsoleController(guestService);
        bookingConsoleController = new BookingConsoleController(bookingFacade);
        entityManager = new EntityManager(bookingConsoleController, guestConsoleController, serviceConsoleController, roomConsoleController);

        mainMenu = new Menu("Hotel administrator", new ArrayList<>());
        builder = new Builder(mainMenu, roomConsoleController, guestConsoleController, serviceConsoleController, bookingConsoleController);
        navigator = new Navigator(mainMenu);
        menuController = new MenuController(builder, navigator);
    }

    public static void main(String[] args) {
        try {
            entityManager.loadAll();
        } catch (Exception e) {
            log.error("Failed to load data", e);
        }

        menuController.run();
    }

    public static void closeApp() {
        try {
            entityManager.saveAll();
        } catch (Exception e) {
            log.error("Failed to save data", e);
        }

        InputManager.getInstance().close();
        log.info("Scanner closed");
        log.info("Application is closing...");
        System.exit(0);
    }
}
