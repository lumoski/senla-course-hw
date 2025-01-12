package com.hotel.controller;

import java.time.LocalDate;
import java.util.List;

import com.hotel.model.Guest;
import com.hotel.service.GuestService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GuestController {
    private final GuestService guestService;

    public Guest addGuest(Guest guest) {
        validate(guest);

        return guestService.addGuest(guest);
    }

    public Guest updateGuest(Long id, Guest newGuest) {
        validate(newGuest);

        return guestService.updateGuest(id, newGuest);
    }

    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    public void importFromCsv(String filePath) {
        guestService.importFromCsv(filePath);
    }

    public void exportToCsv(String filePath) {
        guestService.exportToCsv(filePath);
    }

    private void validate(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }

        validateId(guest.getId());
        validateFirstName(guest.getFirstName());
        validateLastName(guest.getLastName());
        validateEmail(guest.getEmail());
        validatePhoneNumber(guest.getPhoneNumber());
        validateBirthDate(guest.getBirthDate());
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Guest ID cannot be null");
        }
    }

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest first name cannot be null or empty");
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest last name cannot be null or empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Guest email must be a valid email address");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty() || !phoneNumber.matches("\\+?[0-9\\-\\s]+")) {
            throw new IllegalArgumentException("Guest phone number must be a valid phone number");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Guest birth date cannot be null");
        }
        
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Guest birth date cannot be in the future");
        }
    }
}
