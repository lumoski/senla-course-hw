package com.hotel.controller.console;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hotel.controller.api.BookingController;
import com.hotel.dto.request.BookingRoomDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.ConsoleInputUtil;
import com.hotel.service.api.BookingFacade;

public class BookingConsoleControllerImpl implements BookingController {

    @Inject
    private BookingFacade bookingFacade;

    @Override
    public BookingDTO createBooking(BookingRoomDTO bookingDTO) {
        return bookingFacade.createBooking(bookingDTO);
    }

    @Override
    public List<GuestDTO> evictGuestByRoomId(Long roomId) {
        return bookingFacade.evictGuestsFromRoom(roomId);
    }

    @Override
    public List<GuestDTO> findLimitGuestHistoryByRoomId(Long roomId) {
        return bookingFacade.findLimitGuestsByRoom(roomId);
    }

    @Override
    public List<BookingDTO> findAllBookings() {
        return bookingFacade.findAllBookings();
    }

    @Override
    public List<BookingDTO> findAllBookingsSortedByEndDate() {
        return bookingFacade.findAllBookingsSortedByEndDate();
    }

    @Override
    public List<GuestDTO> findAllBookingsSortedByGuestName() {
        return bookingFacade.findAllGuestsSortedByName();
    }

    @Override
    public Integer findAllGuestsCountInHotel() {
        return bookingFacade.findAllGuestsCountInHotel();
    }

    @Override
    public void exportToCsv() {
        bookingFacade.exportToCsv();
    }

    @Override
    public void importFromCsv() {
        // TODO:
    }

    public BookingDTO createBooking() {
        System.out.println("Create a new book room");

        System.out.print("Enter Room ID (Long): ");
        Long id = ConsoleInputUtil.readLong();

        System.out.print("Enter number of guests: ");
        int count = ConsoleInputUtil.readInt();

        List<Long> guestIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            System.out.print("Enter ID of guest " + (i + 1) + ": ");
            Long guestId = ConsoleInputUtil.readLong();
            guestIds.add(guestId);
        }

        System.out.print("Enter number of amenities: ");
        count = ConsoleInputUtil.readInt();

        List<Long> amenitiesIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            System.out.print("Enter ID of amenity " + (i + 1) + ": ");
            Long amenityId = ConsoleInputUtil.readLong();
            amenitiesIds.add(amenityId);
        }

        System.out.print("Enter a date to in (yyyy-MM-dd): ");
        LocalDate checkInDate = ConsoleInputUtil.readDate();

        System.out.print("Enter a date to out (yyyy-MM-dd): ");
        LocalDate checkOutDate = ConsoleInputUtil.readDate();

        System.out.print("Enter a payment method: ");
        String paymentMethod = ConsoleInputUtil.readString();

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<GuestDTO> evictGuestByRoomId() {

        System.out.print("Enter Room ID (Long): ");
        Long id = ConsoleInputUtil.readLong();

        return evictGuestByRoomId(id);
    }

    public List<GuestDTO> findLimitGuestHistoryByRoomId() {
        System.out.print("Enter Room ID (Long): ");
        Long id = ConsoleInputUtil.readLong();

        return findLimitGuestHistoryByRoomId(id);
    }
}
