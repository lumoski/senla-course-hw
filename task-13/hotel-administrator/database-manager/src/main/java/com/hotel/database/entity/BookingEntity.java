package com.hotel.database.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.hotel.core.model.enums.BookingStatus;
import com.hotel.core.model.enums.PaymentMethod;
import com.hotel.core.model.enums.PaymentStatus;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_reference", unique = true)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_guest",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    private Set<GuestEntity> guests;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_amenity",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<AmenityEntity> amenities;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    public String generateBookingReference() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
    
        String roomPart = room != null ? room.getRoomNumber() : "ROOM";
        String datePart = checkInDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    
        return String.format("BK-%s-%s-%s", uuid, roomPart, datePart);
    }
}
