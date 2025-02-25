package com.hotel.core.model.domain;

import com.hotel.core.model.enums.GuestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Guest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String passportNumber;
    private String gender;
    private String address;
    private LocalDate birthDate;
    private GuestStatus status;

    public Guest() {
    }

    public Guest(Long id, String firstName, String lastName, String email, String phoneNumber, 
                String passportNumber, String gender, String address, LocalDate birthDate, 
                GuestStatus status) {
        this.id = id;
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
