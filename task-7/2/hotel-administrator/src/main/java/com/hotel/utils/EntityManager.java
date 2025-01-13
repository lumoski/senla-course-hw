package com.hotel.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.hotel.config.ConfigurationManager;
import com.hotel.controller.BookingController;
import com.hotel.controller.GuestController;
import com.hotel.controller.RoomController;
import com.hotel.controller.ServiceController;
import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Slf4j
@AllArgsConstructor
public class EntityManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final BookingController bookingController;
    private final GuestController guestController;
    private final ServiceController serviceController;
    private final RoomController roomController;

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void serialize(Object object, String filename) throws IOException {
        objectMapper.writeValue(new File(filename), object);
    }

    public static <T> T deserialize(String filename, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(new File(filename), typeReference);
    }

    public void saveAll() throws IOException {
        serialize(bookingController.getAllBookings(), ConfigurationManager.getInstance().getBookingDataPath());
        serialize(guestController.getAllGuests(), ConfigurationManager.getInstance().getGuestDataPath());
        serialize(serviceController.getAllServices(), ConfigurationManager.getInstance().getServiceDataPath());
        serialize(roomController.getAllRooms(), ConfigurationManager.getInstance().getRoomDataPath());

        log.info("All data saved successfully");
    }

    public void loadAll() throws IOException {
        List<Room> rooms = deserialize(ConfigurationManager.getInstance().getRoomDataPath(), new TypeReference<List<Room>>() {});
        List<Guest> guests = deserialize(ConfigurationManager.getInstance().getGuestDataPath(), new TypeReference<List<Guest>>() {});
        List<Service> services = deserialize(ConfigurationManager.getInstance().getServiceDataPath(), new TypeReference<List<Service>>() {});
        List<Booking> bookings = deserialize(ConfigurationManager.getInstance().getBookingDataPath(), new TypeReference<List<Booking>>() {});

        bookings.stream().forEach(booking -> bookingController.addBooking(booking));
        guests.stream().forEach(guest -> guestController.addGuest(guest));
        services.stream().forEach(service -> serviceController.addService(service));
        rooms.stream().forEach(room -> roomController.addRoom(room));

        log.info("All data loaded successfully");
    }
}
