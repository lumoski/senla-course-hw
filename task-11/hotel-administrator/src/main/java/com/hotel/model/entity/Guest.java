package com.hotel.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.model.BaseEntity;
import com.hotel.model.enums.GuestStatus;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Guest extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String passportNumber;
    private String gender;
    private String address;
    private LocalDate birthDate;
    private GuestStatus status;

    @Builder
    public Guest(Long id,
                 String firstName,
                 String lastName,
                 String email,
                 String phoneNumber,
                 String passportNumber,
                 String gender,
                 String address,
                 LocalDate birthDate,
                 GuestStatus status,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.gender = gender;
        this.address = address;
        this.birthDate = birthDate;
        this.status = status;
    }
}
