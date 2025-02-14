package com.hotel.controller.console;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hotel.controller.BookingController;
import com.hotel.dto.request.BookingRoomDTO;
import com.hotel.dto.request.RoomIdDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.exception.RoomNotAvailableException;
import com.hotel.framework.util.InputUtils;

public class BookingConsoleController extends BookingController {
    public BookingConsoleController() {
        super();
    }

    public BookingDTO createBooking() {
        System.out.println("Create a new book room");

        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.print("Enter number of guests: ");
        int count = InputUtils.readInt();

        List<Long> guestIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            System.out.print("Enter ID of guest " + (i + 1) + ": ");
            Long guestId = InputUtils.readLong();
            guestIds.add(guestId);
        }

        System.out.print("Enter number of amenities: ");
        count = InputUtils.readInt();

        List<Long> amenitiesIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            System.out.print("Enter ID of amenity " + (i + 1) + ": ");
            Long amenityId = InputUtils.readLong();
            amenitiesIds.add(amenityId);
        }

        System.out.print("Enter a date to in (yyyy-MM-dd): ");
        LocalDate checkInDate = InputUtils.readDate();

        System.out.print("Enter a date to out (yyyy-MM-dd): ");
        LocalDate checkOutDate = InputUtils.readDate();

        System.out.print("Enter a payment method: ");
        String paymentMethod = InputUtils.readString();

        try {
            BookingRoomDTO bookingRoomDTO = new BookingRoomDTO(
                    id,
                    guestIds,
                    amenitiesIds,
                    checkInDate,
                    checkOutDate,
                    paymentMethod
            );

            return createBooking(bookingRoomDTO);
        } catch (RoomNotAvailableException e) {
            System.out.println("Room not available");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<GuestDTO> evictGuestsFromRoom() {

        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        return evictGuestsFromRoom(id);
    }

    public List<GuestDTO> findLimitGuestsByRoom() {
        System.out.print("Enter Room ID (Long): ");
        Long id = InputUtils.readLong();

        return findLimitGuestsByRoom(id);
    }
}
