package com.hotel.model.entity;

import com.hotel.model.BaseEntity;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.enums.PaymentMethod;
import com.hotel.model.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {
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

    @Builder
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
                   LocalDateTime cancelledAt,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
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

    // TODO: CHANGE TO UNIQUE REFERENCE GENERATING METHOD
    public String generateBookingReference() {
        return "BK-" + System.currentTimeMillis() + "-" + this.checkInDate;
    }
}
