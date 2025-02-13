package com.hotel.controller;

import com.hotel.dto.request.BookingRoomDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.service.api.BookingFacade;

import java.util.List;

public class BookingController {

    @Inject
    private BookingFacade bookingFacade;

    public BookingDTO createBooking(BookingRoomDTO bookingRoomDTO) {
        return bookingFacade.createBooking(bookingRoomDTO);
    }

    public List<GuestDTO> evictGuestsFromRoom(Long roomId) {
        return bookingFacade.evictGuestsFromRoom(roomId);
    }

    public List<GuestDTO> findLimitGuestsByRoom(Long roomId) throws IllegalArgumentException {
        return bookingFacade.findLimitGuestsByRoom(roomId);
    }

    public List<BookingDTO> findAllBookings() {
        return bookingFacade.findAllBookings();
    }

    public List<GuestDTO> findAllBookingsSortedByGuestName() {
        return bookingFacade.findAllGuestsSortedByName();
    }

    public List<BookingDTO> findAllBookingsSortedByEndDate() {
        return bookingFacade.findAllBookingsSortedByEndDate();
    }

    public Integer findAllGuestsCountInHotel() {
        return bookingFacade.findAllGuestsCountInHotel();
    }

    public void exportToCsv() {
        bookingFacade.exportToCsv();
    }
}
