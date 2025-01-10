package com.hotel.controller.console.ui;

import java.util.ArrayList;
import java.util.List;

import com.hotel.controller.console.BookingConsoleController;
import com.hotel.controller.console.GuestConsoleController;
import com.hotel.controller.console.RoomConsoleController;
import com.hotel.controller.console.ServiceConsoleController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Builder {
    private final Menu rootMenu;

    private final RoomConsoleController roomConsoleController;
    private final GuestConsoleController guestConsoleController;
    private final ServiceConsoleController serviceConsoleController;
    private final BookingConsoleController bookingConsoleController;

    public Menu buildMenu() {
        return rootMenu
                .addMenuItem(new MenuItem("Room", null, buildRoomMenu()))
                .addMenuItem(new MenuItem("Guest", null, buildGuestMenu()))
                .addMenuItem(new MenuItem("Service", null, buildServiceMenu()))
                .addMenuItem(new MenuItem("Booking", null, buildBookingMenu()))
                .addMenuItem(new MenuItem("Exit", null, null));
    }

    public Menu getRootMenu() {
        return rootMenu;
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
        return menu
                .addMenuItem(createMenuItem("Add room", roomConsoleController::addRoom, menu))
                .addMenuItem(createMenuItem("Change room status", roomConsoleController::changeRoomStatus, menu))
                .addMenuItem(createMenuItem("Get all rooms", () -> printList(roomConsoleController.getAllRooms()), menu))
                .addMenuItem(createMenuItem("Get all rooms sorted by price", () -> printList(roomConsoleController.getAllRoomsSortedByPrice()), menu))
                .addMenuItem(createMenuItem("Get all rooms sorted by capacity", () -> printList(roomConsoleController.getAllRoomsSortedByCapacity()), menu))
                .addMenuItem(createMenuItem("Get all rooms sorted by rating", () -> printList(roomConsoleController.getAllRoomsSortedByStars()), menu))
                .addMenuItem(createMenuItem("Get available rooms count", () -> System.out.println("Available rooms: " + roomConsoleController.getAvailableRoomsCount()), menu))
                .addMenuItem(createMenuItem("Get all available rooms sorted by price", () -> printList(roomConsoleController.getAllAvailableRoomsSortedByPrice()), menu))
                .addMenuItem(createMenuItem("Get all available rooms sorted by capacity", () -> printList(roomConsoleController.getAllAvailableRoomsSortedByCapacity()), menu))
                .addMenuItem(createMenuItem("Get all available rooms sorted by rating", () -> printList(roomConsoleController.getAllAvailableRoomsSortedByStars()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildGuestMenu() {
        Menu guestMenu = new Menu("Guest Menu", new ArrayList<>());
        return addGuestMenuItems(guestMenu);
    }

    private Menu addGuestMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add Guest", guestConsoleController::addGuest, menu))
                .addMenuItem(createMenuItem("Update Guest", guestConsoleController::updateGuest, menu))
                .addMenuItem(createMenuItem("Get all guests", () -> printList(guestConsoleController.getAllGuests()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildServiceMenu() {
        Menu serviceMenu = new Menu("Service Menu", new ArrayList<>());
        return addServiceMenuItems(serviceMenu);
    }

    private Menu addServiceMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add service", serviceConsoleController::addService, menu))
                .addMenuItem(createMenuItem("Update service price", serviceConsoleController::updateServicePrice, menu))
                .addMenuItem(createMenuItem("Get all services", () -> printList(serviceConsoleController.getAllServices()), menu))
                .addMenuItem(createMenuItem("Get all services sorted by price", () -> printList(serviceConsoleController.getAllServicesSortedByPrice()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildBookingMenu() {
        Menu bookingMenu = new Menu("Booking Menu", new ArrayList<>());
        return addBookingMenuItems(bookingMenu);
    }

    private Menu addBookingMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Book room", bookingConsoleController::bookRoom, menu))
                .addMenuItem(createMenuItem("Evict guests from room", bookingConsoleController::evictGuestsFromRoom, menu))
                .addMenuItem(createMenuItem("Calculate total payment for booking", bookingConsoleController::calculateTotalPaymentForBooking, menu))
                .addMenuItem(createMenuItem("Get last 3 guests by room", () -> printList(bookingConsoleController.getLastThreeGuestsByRoom()), menu))
                .addMenuItem(createMenuItem("Get all bookings", () -> printList(bookingConsoleController.getAllBookings()), menu))
                .addMenuItem(createMenuItem("Get all bookings sorted by name", () -> printList(bookingConsoleController.getAllBookingsSortedByGuestName()), menu))
                .addMenuItem(createMenuItem("Get all bookings sorted by end date", () -> printList(bookingConsoleController.getAllBookingsSortedByEndDate()), menu))
                .addMenuItem(createMenuItem("Get all guests in hotel", () -> System.out.println("Guests in hotel: " + bookingConsoleController.getAllGuestsCountInHotel()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private MenuItem createMenuItem(String title, IAction action, Menu nextMenu) {
        return new MenuItem(title, action, nextMenu);
    }
}
