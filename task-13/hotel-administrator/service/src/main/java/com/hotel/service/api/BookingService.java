package com.hotel.service.api;

import java.util.List;

import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.request.BookingUpdateBookingStatusDTO;
import com.hotel.dto.request.BookingUpdateCheckOutDateDTO;
import com.hotel.dto.request.BookingUpdatePaymentStatusDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;

public interface BookingService {
    BookingDTO findById(Long id);

    BookingDTO findCurrentBookingForRoom(Long roomId);

    BookingDTO createBooking(BookingCreateDTO bookingCreateDTO);

    void checkAndThrowIfBookingNotAllowed(BookingCreateDTO bookingCreateDTO);

    BookingDTO updateBookingStatus(BookingUpdateBookingStatusDTO bookingUpdateDTO);

    BookingDTO updatePaymentStatus(BookingUpdatePaymentStatusDTO bookingUpdatePaymentStatusDTO);

    BookingDTO updateCheckOutDate(BookingUpdateCheckOutDateDTO bookingUpdateCheckOutDateDTO);

    List<BookingDTO> findAllBookings();

    List<BookingDTO> findAllBookingsSortedByEndDate();

    List<GuestDTO> findAllGuestsSortedByName();

    List<GuestDTO> findLimitGuestsByRoomId(Long roomId);

    Integer findAllGuestsCountInHotel();

    void exportToCsv();
}
