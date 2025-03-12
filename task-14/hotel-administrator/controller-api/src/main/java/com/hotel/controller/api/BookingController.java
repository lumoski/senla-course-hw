package com.hotel.controller.api;

import java.util.List;

import com.hotel.dto.request.BookingRoomDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;

public interface BookingController {

    BookingDTO createBooking(BookingRoomDTO bookingDTO);

    List<GuestDTO> evictGuestByRoomId(Long roomId);

    List<GuestDTO> findLimitGuestHistoryByRoomId(Long roomId);

    List<BookingDTO> findAllBookings();

    List<BookingDTO> findAllBookingsSortedByEndDate();

    List<GuestDTO> findAllBookingsSortedByGuestName();

    Integer findAllGuestsCountInHotel();

    void exportToCsv();

    void importFromCsv();
}
