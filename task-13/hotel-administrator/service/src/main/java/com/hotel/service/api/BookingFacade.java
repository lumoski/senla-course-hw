package com.hotel.service.api;

import java.util.List;

import com.hotel.dto.request.BookingRoomDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;

public interface BookingFacade {
    
    BookingDTO createBooking(BookingRoomDTO bookingRoomDTO);

    void updateBookings();

    List<GuestDTO> evictGuestsFromRoom(Long roomId);

    List<BookingDTO> findAllBookings();

    List<BookingDTO> findAllBookingsSortedByEndDate();

    List<GuestDTO> findAllGuestsSortedByName();

    List<GuestDTO> findLimitGuestsByRoom(Long roomId);

    Integer findAllGuestsCountInHotel();

    void exportToCsv();
}
