package com.hotel.service;

import java.time.LocalDate;
import java.util.List;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BookingFacade {
    private final RoomService roomService;
    private final GuestService guestService;
    private final BookingService bookingService;

    public Booking bookRoom(Long roomId, List<Long> guestIds, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = roomService.findById(roomId);

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room is not available for booking");
        }

        List<Guest> guests = guestIds.stream()
                .map(guestService::findGuestById)
                .toList();


        if (guests.size() > room.getCapacity()) {
            throw new IllegalArgumentException("Room cannot accommodate more than " + room.getCapacity() + " guests");
        }

        if (checkInDate.isAfter(checkOutDate)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        roomService.changeRoomStatus(roomId, RoomStatus.OCCUPIED);

        Booking booking = new Booking(checkInDate, checkOutDate, room, room.getPrice() * guests.size(), guests);
        Booking savedBooking = bookingService.addBooking(booking);

        log.info("Room {} successfully booked from {} to {} for {} guests", roomId, checkInDate, checkOutDate, guests.size());
        return savedBooking;
    }

    public void evictGuestsFromRoom(Long roomId) {
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room with ID '" + roomId + "' does not exist");
        }
    
        if (room.getStatus() != RoomStatus.OCCUPIED) {
            throw new IllegalStateException("Room with ID '" + roomId + "' is not currently occupied");
        }
    
        List<Booking> roomBookings = bookingService.getAllBookings().stream()
                .filter(booking -> booking.getRoom().getId().equals(roomId))
                .filter(booking -> booking.getCheckOutDate().isAfter(LocalDate.now()) || 
                                   booking.getCheckOutDate().isEqual(LocalDate.now()))
                .toList();
    
        for (Booking booking : roomBookings) {
            booking.setCheckOutDate(LocalDate.now());
            bookingService.addBooking(booking);
            log.info("Booking '{}' for room '{}' checked out successfully", booking.getId(), roomId);
        }
    
        roomService.changeRoomStatus(roomId, RoomStatus.AVAILABLE);
    }
    

    public void checkOutExpiredBookings() {
        List<Booking> expiredBookings = bookingService.getAllBookings().stream()
                .filter(booking -> booking.getCheckOutDate().isBefore(LocalDate.now()) || booking.getCheckOutDate().isEqual(LocalDate.now()))
                .toList();

        for (Booking booking : expiredBookings) {
            Room room = booking.getRoom();

            roomService.changeRoomStatus(room.getId(), RoomStatus.AVAILABLE);

            log.info("Room {} has been checked out and set to AVAILABLE", room.getId());
        }
    }

    public double calculateTotalPaymentForBooking(Long bookingId) {
        return bookingService.calculateTotalPaymentForBooking(bookingId);
    }

    public Booking findBookingById(Long id) {
        return bookingService.findBookingById(id);
    }

    public List<Guest> getLastThreeGuestsByRoom(Long roomId) {
        return bookingService.getLastThreeGuestsByRoom(roomId);
    }

    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    public List<Booking> getBookingsSortedByName() {
        return bookingService.getBookingsSortedByName();
    }

    public List<Booking> getBookingsSortedByEndDate() {
        return bookingService.getBookingsSortedByName();
    }

    public Integer getAllGuestsInHotel() {
        return bookingService.getAllGuestsInHotel();
    }
}
