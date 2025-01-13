package com.hotel.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.utils.UtilityClass;

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

        Booking booking = new Booking(bookingService.getAllBookings().stream().count() + 1, checkInDate, checkOutDate,
                room, room.getPrice() * guests.size(), guests);
        Booking savedBooking = bookingService.addBooking(booking);

        log.info("Room {} successfully booked from {} to {} for {} guests", roomId, checkInDate, checkOutDate,
                guests.size());
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
        LocalDate today = LocalDate.now();
    
        List<Booking> expiredBookings = bookingService.getAllBookings().stream()
                .filter(booking -> {
                    Room room = booking.getRoom();
                    boolean hasActiveBooking = bookingService.getAllBookings().stream()
                            .anyMatch(b -> b.getRoom().equals(room)
                                    && (today.isAfter(b.getCheckInDate()) || today.isEqual(b.getCheckInDate()))
                                    && (today.isBefore(b.getCheckOutDate()) || today.isEqual(b.getCheckOutDate())));
    
                    return room.getStatus() == RoomStatus.OCCUPIED
                            && (booking.getCheckOutDate().isBefore(today) || booking.getCheckOutDate().isEqual(today))
                            && !hasActiveBooking;
                })
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

    public List<Guest> getLimitGuestsByRoom(Long id) {
        return bookingService.getLimitGuestsByRoom(id);
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

    public void importFromCsv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 6) {
                    throw new IllegalArgumentException("Invalid CSV format for Booking");
                }

                Long id = Long.parseLong(fields[0]);
                LocalDate checkInDate = LocalDate.parse(fields[1]);
                LocalDate checkOutDate = LocalDate.parse(fields[2]);
                Long roomId = Long.parseLong(fields[3]);
                double totalPrice = Double.parseDouble(fields[4]);
                List<Long> guestIds = Arrays.stream(fields[5].split(";"))
                        .map(Long::valueOf)
                        .toList();

                Booking booking = new Booking(id, checkInDate, checkOutDate, null, totalPrice, List.of());

                booking.setRoom(roomService.findById(roomId));
                booking.setGuests(guestIds
                        .stream()
                        .map(guestId -> guestService.findGuestById(guestId))
                        .toList());

                bookingService.addBooking(booking);
            }
            System.out.println("Bookings imported successfully from " + filePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error importing bookings from CSV: " + e.getMessage());
        }
    }

    public void exportToCsv(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write(UtilityClass.getFieldNames(Booking.class));
            writer.newLine();

            for (Booking booking : bookingService.getAllBookings()) {
                Long roomId = booking.getRoom() != null ? booking.getRoom().getId() : null;
                String guestIds = booking.getGuests().stream()
                        .map(guest -> String.valueOf(guest.getId()))
                        .collect(Collectors.joining(";"));
                String line = String.format("%d,%s,%s,%s,%.2f,%s",
                        booking.getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        roomId != null ? roomId.toString() : "null",
                        booking.getTotalPrice(),
                        guestIds);
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Bookings exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting bookings to CSV: " + e.getMessage());
        }
    }
}
