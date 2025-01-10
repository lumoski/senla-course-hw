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
import com.hotel.repository.GuestServicePurchaseRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.ServiceRepository;

import com.hotel.repository.impl.InMemoryBookingRepository;
import com.hotel.repository.impl.InMemoryGuestRepository;
import com.hotel.repository.impl.InMemoryGuestServicePurchaseRepository;
import com.hotel.repository.impl.InMemoryRoomRepository;
import com.hotel.repository.impl.InMemoryServiceRepository;

import com.hotel.service.BookingFacade;
import com.hotel.service.BookingService;
import com.hotel.service.GuestService;
import com.hotel.service.HotelServiceService;
import com.hotel.service.RoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HotelAdministrator {
    private final RoomConsoleController roomConsoleController;
    private final ServiceConsoleController serviceConsoleController;
    private final GuestConsoleController guestConsoleController;
    private final BookingConsoleController bookingConsoleController;

    private final Menu mainMenu;
    private final Builder builder;
    private final Navigator navigator;
    private final MenuController menuController;

    public HotelAdministrator() {
        ServiceRepository serviceRepository = new InMemoryServiceRepository();
        GuestRepository guestRepository = new InMemoryGuestRepository();
        BookingRepository bookingRepository = new InMemoryBookingRepository();
        RoomRepository roomRepository = new InMemoryRoomRepository(bookingRepository);
        GuestServicePurchaseRepository guestServicePurchaseRepository = new InMemoryGuestServicePurchaseRepository();

        RoomService roomService = new RoomService(roomRepository);
        HotelServiceService hotelServiceService = new HotelServiceService(serviceRepository);
        GuestService guestService = new GuestService(guestRepository);
        BookingService bookingService = new BookingService(bookingRepository);
        BookingFacade bookingFacade = new BookingFacade(roomService, guestService, bookingService);

        this.roomConsoleController = new RoomConsoleController(roomService);
        this.serviceConsoleController = new ServiceConsoleController(hotelServiceService);
        this.guestConsoleController = new GuestConsoleController(guestService);
        this.bookingConsoleController = new BookingConsoleController(bookingFacade);

        this.mainMenu = new Menu("Hotel administrator", new ArrayList<>());
        this.builder = new Builder(mainMenu, roomConsoleController, guestConsoleController, serviceConsoleController, bookingConsoleController);
        this.navigator = new Navigator(mainMenu);
        this.menuController = new MenuController(builder, navigator);
    }

    public static void main(String[] args) {
        HotelAdministrator admin = new HotelAdministrator();
        admin.menuController.run();
    }

    public static void closeApp() {
        InputManager.getInstance().close();
        log.info("Scanner closed");
        log.info("Application is closing...");
        System.exit(0);
    }
}
