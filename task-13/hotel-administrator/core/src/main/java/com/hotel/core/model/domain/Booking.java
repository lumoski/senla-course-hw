package com.hotel.core.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import com.hotel.core.model.enums.BookingStatus;
import com.hotel.core.model.enums.PaymentMethod;
import com.hotel.core.model.enums.PaymentStatus;

@Getter
@Setter
public class Booking {
    private Long id;
    private String bookingReference;
    private Room room;
    private List<Guest> guests;
    private List<Amenity> amenities;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private LocalDateTime cancelledAt;

    public Booking() {
    }

    public Booking(Long id,
                   String bookingReference,
                   Room room,
                   List<Guest> guests, 
                   List<Amenity> amenities,
                   LocalDate checkInDate,
                   LocalDate checkOutDate, 
                   double totalPrice,
                   BookingStatus bookingStatus,
                   PaymentStatus paymentStatus, 
                   PaymentMethod paymentMethod,
                   LocalDateTime cancelledAt) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.room = room;
        this.guests = guests;
        this.amenities = amenities;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.cancelledAt = cancelledAt;
    }

    public String generateBookingReference() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
    
        String roomPart = room != null ? room.getRoomNumber() : "ROOM";
        String datePart = checkInDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    
        return String.format("BK-%s-%s-%s", uuid, roomPart, datePart);
    }
}
