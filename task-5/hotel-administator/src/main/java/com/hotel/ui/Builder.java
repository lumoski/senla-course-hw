package com.hotel.ui;

import java.util.ArrayList;
import java.util.List;

import com.hotel.service.ConsoleBookingFacade;
import com.hotel.service.ConsoleGuestService;
import com.hotel.service.ConsoleHotelServiceService;
import com.hotel.service.ConsoleRoomService;

public class Builder {
    private final Menu rootMenu;

    private final ConsoleRoomService roomService;
    private final ConsoleGuestService guestService;
    private final ConsoleHotelServiceService hotelServiceService;
    private final ConsoleBookingFacade bookingFacade;

    public Builder(Menu rootMenu,
                   ConsoleRoomService roomService,
                   ConsoleGuestService guestService,
                   ConsoleHotelServiceService hotelServiceService,
                   ConsoleBookingFacade bookingFacade) {
        this.rootMenu = rootMenu;
        this.roomService = roomService;
        this.guestService = guestService;
        this.hotelServiceService = hotelServiceService;
        this.bookingFacade = bookingFacade;
    }

    public Menu buildMenu() {
        return rootMenu
                .addMenuItem(new MenuItem("Room", null, buildRoomMenu()))
                .addMenuItem(new MenuItem("Guest", null, buildGuestMenu()))
                .addMenuItem(new MenuItem("Service", null, buildServiceMenu()))
                .addMenuItem(new MenuItem("Booking", null, buildBookingMenu()))
                .addMenuItem(new MenuItem("Exit", null, null));
    }

    public void printList(List<?> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            list.forEach(System.out::println);
        }
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    private Menu buildRoomMenu() {
        Menu roomMenu = new Menu("Room Menu", new ArrayList<>());
        return addRoomMenuItems(roomMenu);
    }

    private Menu addRoomMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add room", roomService::consoleAddRoom, menu))
                .addMenuItem(createMenuItem("Change room status", roomService::consoleChangeRoomStatus, menu))
                .addMenuItem(createMenuItem("Get all rooms", () -> printList(roomService.getAllRooms()), menu))
                .addMenuItem(createMenuItem("Get all rooms sorted by price", () -> printList(roomService.getAllRoomsSortedByPrice()), menu))
                .addMenuItem(createMenuItem("Get all rooms sorted by capacity", () -> printList(roomService.getAllRoomsSortedByCapacity()), menu))
                .addMenuItem(createMenuItem("Get all rooms sorted by rating", () -> printList(roomService.getAllRoomsSortedByStars()), menu))
                .addMenuItem(createMenuItem("Get available rooms count", () -> System.out.println("Available rooms: " + roomService.getAvailableRoomsCount()), menu))
                .addMenuItem(createMenuItem("Get all available rooms sorted by price", () -> printList(roomService.getAllAvailableRoomsSortedByPrice()), menu))
                .addMenuItem(createMenuItem("Get all available rooms sorted by capacity", () -> printList(roomService.getAllAvailableRoomsSortedByCapacity()), menu))
                .addMenuItem(createMenuItem("Get all available rooms sorted by rating", () -> printList(roomService.getAllAvailableRoomsSortedByStars()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildGuestMenu() {
        Menu guestMenu = new Menu("Guest Menu", new ArrayList<>());
        return addGuestMenuItems(guestMenu);
    }

    private Menu addGuestMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add Guest", guestService::consoleAddGuest, menu))
                .addMenuItem(createMenuItem("Update Guest", guestService::consoleUpdateGuest, menu))
                .addMenuItem(createMenuItem("Get all guests", () -> printList(guestService.getAllGuests()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildServiceMenu() {
        Menu serviceMenu = new Menu("Service Menu", new ArrayList<>());
        return addServiceMenuItems(serviceMenu);
    }

    private Menu addServiceMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Add service", hotelServiceService::consoleAddService, menu))
                .addMenuItem(createMenuItem("Update service price", hotelServiceService::consoleUpdateServicePrice, menu))
                .addMenuItem(createMenuItem("Get all services", () -> printList(hotelServiceService.getAllServices()), menu))
                .addMenuItem(createMenuItem("Get all services sorted by price", () -> printList(hotelServiceService.getAllServicesSortedByPrice()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private Menu buildBookingMenu() {
        Menu bookingMenu = new Menu("Booking Menu", new ArrayList<>());
        return addBookingMenuItems(bookingMenu);
    }

    private Menu addBookingMenuItems(Menu menu) {
        return menu
                .addMenuItem(createMenuItem("Book room", bookingFacade::consoleBookRoom, menu))
                .addMenuItem(createMenuItem("Evict guests form room", bookingFacade::consoleEvictGuestsFromRoom, menu))
                .addMenuItem(createMenuItem("Calculate total payment for booking", bookingFacade::consoleCalculateTotalPaymentForBooking, menu))
                .addMenuItem(createMenuItem("Get last 3 guests by room", () -> printList(bookingFacade.consoleGetLastThreeGuestsByRoom()), menu))
                .addMenuItem(createMenuItem("Get all bookings", () -> printList(bookingFacade.getAllBookings()), menu))
                .addMenuItem(createMenuItem("Get all bookings sorted by name", () -> printList(bookingFacade.getBookingsSortedByName()), menu))
                .addMenuItem(createMenuItem("Get all bookings sorted by end date", () -> printList(bookingFacade.getBookingsSortedByEndDate()), menu))
                .addMenuItem(createMenuItem("Get all guests in hotel", () -> System.out.println("Guests in hotel: " + bookingFacade.getAllGuestsInHotel()), menu))
                .addMenuItem(createMenuItem("<- Back", null, rootMenu));
    }

    private MenuItem createMenuItem(String title, IAction action, Menu nextMenu) {
        return new MenuItem(title, action, nextMenu);
    }
}
