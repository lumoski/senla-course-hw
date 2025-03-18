package com.hotel.controller.console.ui;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.hotel.controller.console.AmenityConsoleControllerImpl;
import com.hotel.controller.console.BookingConsoleControllerImpl;
import com.hotel.controller.console.GuestConsoleControllerImpl;
import com.hotel.controller.console.RoomConsoleControllerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:ui.properties")
public class Builder {

    @Getter
    private Menu rootMenu;

    private RoomConsoleControllerImpl roomConsoleController;

    private GuestConsoleControllerImpl guestConsoleController;

    private AmenityConsoleControllerImpl amenityConsoleController;

    private BookingConsoleControllerImpl bookingConsoleController;

    @Value("${builder.isroomstatuschangedenabled}")
    private boolean isRoomStatusChangeEnabled;

    public Builder(Menu rootMenu, RoomConsoleControllerImpl roomConsoleControllerImpl,
        GuestConsoleControllerImpl guestConsoleControllerImpl, AmenityConsoleControllerImpl amenityConsoleControllerImpl,
        BookingConsoleControllerImpl bookingConsoleControllerImpl) {
        this.rootMenu = rootMenu;
        this.amenityConsoleController = amenityConsoleControllerImpl;
        this.bookingConsoleController = bookingConsoleControllerImpl;
        this.guestConsoleController = guestConsoleControllerImpl;
        this.roomConsoleController = roomConsoleControllerImpl;
    }

    public Menu buildMenu() {
        return rootMenu
                .addMenuItem(new MenuItem("Room", null, buildRoomMenu()))
                .addMenuItem(new MenuItem("Guest", null, buildGuestMenu()))
                .addMenuItem(new MenuItem("Service", null, buildServiceMenu()))
                .addMenuItem(new MenuItem("Booking", null, buildBookingMenu()))
                .addMenuItem(new MenuItem("Import All", () -> {
                }, null))
                .addMenuItem(new MenuItem("Export All", () -> {
                    bookingConsoleController.exportToCsv();
                    roomConsoleController.exportToCsv();
                    guestConsoleController.exportToCsv();
                    amenityConsoleController.exportToCsv();
                }, null))
                .addMenuItem(new MenuItem("Exit", null, null));
    }

    private void printList(List<?> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private Menu buildRoomMenu() {
        Menu roomMenu = new Menu("Room Menu", new ArrayList<>());
        return addRoomMenuItems(roomMenu);
    }

    private Menu addRoomMenuItems(Menu menu) {
        menu.addMenuItem(createMenuItem("Add room", roomConsoleController::createRoom, menu));
        
        if (isRoomStatusChangeEnabled) {
            menu.addMenuItem(createMenuItem("Change room status", roomConsoleController::updateRoomStatus, menu));
        }
        
        menu.addMenuItem(createMenuItem("Update room price", roomConsoleController::updateRoomPrice, menu))
            .addMenuItem(createMenuItem("Get all rooms",
                    () -> printList(roomConsoleController.findAllRooms()), menu)
            )
            .addMenuItem(createMenuItem("Get all rooms sorted by price",
                    () -> printList(roomConsoleController.findAllRoomsSortedByPrice()), menu)
            )
            .addMenuItem(createMenuItem("Get all rooms sorted by capacity",
                    () -> printList(roomConsoleController.findAllRoomsSortedByCapacity()), menu)
            )
            .addMenuItem(createMenuItem("Get all rooms sorted by rating",
                    () -> printList(roomConsoleController.findAllRoomsSortedByStars()), menu)
            )
            .addMenuItem(createMenuItem("Get available rooms count",
                    () -> System.out.println("Available rooms: " + roomConsoleController.findAvailableRoomsCount()), menu)
            )
            .addMenuItem(createMenuItem("Get all available rooms sorted by price",
                    () -> printList(roomConsoleController.findAllAvailableRoomsSortedByPrice()), menu)
            )
            .addMenuItem(createMenuItem("Get all available rooms sorted by capacity",
                    () -> printList(roomConsoleController.findAllAvailableRoomsSortedByCapacity()), menu)
            )
            .addMenuItem(createMenuItem("Get all available rooms sorted by rating",
                    () -> printList(roomConsoleController.findAllAvailableRoomsSortedByStars()), menu)
            )
            .addMenuItem(createMenuItem("Get available rooms by date",
                    () -> printList(roomConsoleController.findAvailableRoomsByDate()), menu))
            .addMenuItem(createMenuItem("Export rooms",
                    roomConsoleController::exportToCsv, menu)
            )
            .addMenuItem(createMenuItem("<- Back", null, rootMenu));

        return menu;
    }

    private Menu buildGuestMenu() {
        Menu guestMenu = new Menu("Guest Menu", new ArrayList<>());
        return addGuestMenuItems(guestMenu);
    }

    private Menu addGuestMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add Guest", guestConsoleController::createGuest, menu))
                .addMenuItem(createMenuItem("Update Guest", guestConsoleController::updateGuest, menu))
                .addMenuItem(createMenuItem("Get all guests", () -> printList(guestConsoleController.findAllGuests()), menu))
                .addMenuItem(createMenuItem("Export guests", guestConsoleController::exportToCsv, menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildServiceMenu() {
        Menu serviceMenu = new Menu("Service Menu", new ArrayList<>());
        return addServiceMenuItems(serviceMenu);
    }

    private Menu addServiceMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add service", amenityConsoleController::createAmenity, menu))
                .addMenuItem(createMenuItem("Update service price", amenityConsoleController::updateAmenityPrice, menu))
                .addMenuItem(createMenuItem("Get all services", () -> printList(amenityConsoleController.findAllAmenities()), menu))
                .addMenuItem(createMenuItem("Get all services sorted by price", () -> printList(amenityConsoleController.findAllAmenitiesSortedByPrice()), menu))
                .addMenuItem(createMenuItem("Export services", amenityConsoleController::exportToCsv, menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildBookingMenu() {
        Menu bookingMenu = new Menu("Booking Menu", new ArrayList<>());
        return addBookingMenuItems(bookingMenu);
    }

    private Menu addBookingMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Book room", bookingConsoleController::createBooking, menu))
                .addMenuItem(createMenuItem("Evict guests from room", bookingConsoleController::evictGuestByRoomId, menu))
                .addMenuItem(createMenuItem("Get last guests by room", () -> printList(bookingConsoleController.findLimitGuestHistoryByRoomId()), menu))
                .addMenuItem(createMenuItem("Get all bookings", () -> printList(bookingConsoleController.findAllBookings()), menu))
                .addMenuItem(createMenuItem("Get all bookings sorted by name", () -> printList(bookingConsoleController.findAllBookingsSortedByGuestName()), menu))
                .addMenuItem(createMenuItem("Get all bookings sorted by end date", () -> printList(bookingConsoleController.findAllBookingsSortedByEndDate()), menu))
                .addMenuItem(createMenuItem("Get all guests in hotel", () -> System.out.println("Guests in hotel: " + bookingConsoleController.findAllGuestsCountInHotel()), menu))
                .addMenuItem(createMenuItem("Export bookings", bookingConsoleController::exportToCsv, menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private MenuItem createMenuItem(String title, IAction action, Menu nextMenu) {
        return new MenuItem(title, action, nextMenu);
    }
}
