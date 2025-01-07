package com.hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.model.Service;

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
import com.hotel.service.ConsoleBookingFacade;
import com.hotel.service.ConsoleGuestService;
import com.hotel.service.ConsoleHotelServiceService;
import com.hotel.service.ConsoleRoomService;
import com.hotel.service.GuestService;
import com.hotel.service.HotelServiceService;
import com.hotel.service.RoomService;

import com.hotel.ui.Builder;
import com.hotel.ui.InputManager;
import com.hotel.ui.Menu;
import com.hotel.ui.MenuController;
import com.hotel.ui.Navigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HotelAdministrator {
    private final ConsoleRoomService roomService;
    private final ConsoleGuestService guestService;
    private final ConsoleHotelServiceService hotelServiceService;
    private final ConsoleBookingFacade bookingFacade;

    public HotelAdministrator() {
        InputManager inputManager = InputManager.getInstance();

        ServiceRepository serviceRepository = new InMemoryServiceRepository();
        GuestRepository guestRepository = new InMemoryGuestRepository();
        BookingRepository bookingRepository = new InMemoryBookingRepository();
        RoomRepository roomRepository = new InMemoryRoomRepository(bookingRepository);
        GuestServicePurchaseRepository guestServicePurchaseRepository = new InMemoryGuestServicePurchaseRepository();

        BookingService bookingService = new BookingService(bookingRepository);

        ConsoleRoomService consoleRoomService = new ConsoleRoomService(roomRepository, inputManager.getScanner());
        ConsoleGuestService consoleGuestService = new ConsoleGuestService(guestRepository, inputManager.getScanner());
        ConsoleHotelServiceService consoleHotelServiceService = new ConsoleHotelServiceService(serviceRepository, inputManager.getScanner());
        ConsoleBookingFacade bookingFacade = new ConsoleBookingFacade(consoleRoomService, consoleGuestService, bookingService, inputManager.getScanner());

        this.roomService = consoleRoomService;
        this.guestService = consoleGuestService;
        this.hotelServiceService = consoleHotelServiceService;
        this.bookingFacade = bookingFacade;
    }

    public static void main(String[] args) {

        HotelAdministrator admin = new HotelAdministrator();

        DataLoader.roomsLoader(admin.roomService);
        DataLoader.servicesLoader(admin.hotelServiceService);
        DataLoader.guestsLoader(admin.guestService);
        DataLoader.bookingsLoader(admin.bookingFacade);

        Menu mainMenu = new Menu("Hotel administrator", new ArrayList<>());

        Builder builder = new Builder(mainMenu, admin.roomService, admin.guestService, admin.hotelServiceService, admin.bookingFacade);
        Navigator navigator = new Navigator(mainMenu);

        MenuController menuController = new MenuController(builder, navigator);
        menuController.run();

        InputManager.getInstance().close();
        log.info("Scanner closed");
        log.info("Application is closing...");
    }
}

class DataLoader {
    public static void servicesLoader(HotelServiceService serviceService) {
        serviceService.addService(new Service(1L, "Room Cleaning", 15.0, "Cleaning"));
        serviceService.addService(new Service(2L, "Laundry", 20.0, "Cleaning"));
        serviceService.addService(new Service(3L, "Spa Treatment", 50.0, "Wellness"));
        serviceService.addService(new Service(4L, "Gym Access", 10.0, "Wellness"));
        serviceService.addService(new Service(5L, "Breakfast", 25.0, "Dining"));
        serviceService.addService(new Service(6L, "Lunch", 30.0, "Dining"));
        serviceService.addService(new Service(7L, "Dinner", 40.0, "Dining"));
        serviceService.addService(new Service(8L, "Airport Shuttle", 60.0, "Transport"));
        serviceService.addService(new Service(9L, "Car Rental", 100.0, "Transport"));
        serviceService.addService(new Service(10L, "Wi-Fi", 5.0, "Miscellaneous"));
    }

    public static void roomsLoader(RoomService roomService) {
        roomService.addRoom(new Room(1L, 50, 2, 3, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(2L, 100, 1, 2, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(3L, 90, 1, 1, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(4L, 150, 2, 3, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(5L, 200, 3, 4, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(6L, 120, 1, 1, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(7L, 80, 1, 2, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(8L, 180, 2, 3, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(9L, 220, 3, 4, RoomStatus.AVAILABLE));
        roomService.addRoom(new Room(10L, 300, 4, 5, RoomStatus.AVAILABLE));
    }

    public static void guestsLoader(GuestService guestService) {
        guestService.addGuest(new Guest(1L, "Ivan", "Ivanov", "ivanov@yandex.ru", "+7(999)324-32-43", LocalDate.of(1998, 12, 12)));
        guestService.addGuest(new Guest(2L, "Petr", "Petrov", "petrov@mail.ru", "+7(912)123-45-67", LocalDate.of(1985, 5, 15)));
        guestService.addGuest(new Guest(3L, "Anna", "Sidorova", "sidorova@gmail.com", "+7(901)543-21-09", LocalDate.of(1990, 3, 10)));
        guestService.addGuest(new Guest(4L, "Olga", "Smirnova", "smirnova@outlook.com", "+7(950)123-33-22", LocalDate.of(1995, 7, 20)));
        guestService.addGuest(new Guest(5L, "Sergey", "Kuznetsov", "kuznetsov@mail.ru", "+7(987)654-32-10", LocalDate.of(1988, 11, 11)));
        guestService.addGuest(new Guest(6L, "Elena", "Morozova", "morozova@yandex.ru", "+7(996)223-45-67", LocalDate.of(1992, 6, 18)));
        guestService.addGuest(new Guest(7L, "Dmitry", "Volkov", "volkov@gmail.com", "+7(903)111-22-33", LocalDate.of(1980, 2, 2)));
        guestService.addGuest(new Guest(8L, "Ekaterina", "Zaitseva", "zaitseva@mail.ru", "+7(915)345-67-89", LocalDate.of(2000, 1, 25)));
        guestService.addGuest(new Guest(9L, "Nikolay", "Popov", "popov@mail.com", "+7(918)765-43-21", LocalDate.of(1993, 9, 14)));
        guestService.addGuest(new Guest(10L, "Maria", "Sokolova", "sokolova@outlook.com", "+7(901)876-54-32", LocalDate.of(1997, 4, 5)));
    }

    public static void bookingsLoader(BookingFacade bookingFacade) {
        bookingFacade.bookRoom(1L, List.of(1L, 2L), LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 5));
        bookingFacade.bookRoom(3L, List.of(3L), LocalDate.of(2023, 12, 4), LocalDate.of(2023, 12, 6));
        bookingFacade.bookRoom(4L, List.of(4L), LocalDate.of(2023, 12, 5), LocalDate.of(2023, 12, 10));
        bookingFacade.bookRoom(5L, List.of(5L), LocalDate.of(2023, 12, 7), LocalDate.of(2023, 12, 12));
        bookingFacade.bookRoom(6L, List.of(6L), LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 3));
        bookingFacade.bookRoom(7L, List.of(7L), LocalDate.of(2023, 12, 2), LocalDate.of(2023, 12, 4));
        bookingFacade.bookRoom(8L, List.of(8L), LocalDate.of(2023, 12, 3), LocalDate.of(2023, 12, 8));
        bookingFacade.bookRoom(9L, List.of(9L), LocalDate.of(2023, 12, 5), LocalDate.of(2023, 12, 9));
        bookingFacade.bookRoom(10L, List.of(10L), LocalDate.of(2023, 12, 6), LocalDate.of(2023, 12, 11));
    }
}