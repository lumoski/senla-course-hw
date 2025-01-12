package com.hotel.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;
import com.hotel.utils.UtilityClass;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for managing hotel guests.
 * Handles business logic for creating and updating hotel guests.
 */
@Slf4j
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;

    public Guest findGuestById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Guest ID cannot be null");
        }

        return guestRepository.findById(id).orElseThrow(() -> {
            log.warn("Failed to find guest: Guest with ID '{}' not found", id);
            return new IllegalArgumentException("Guest with ID '" + id + "' not found");
        });
    }

    public Guest addGuest(Guest guest) {
        if (guestRepository.findById(guest.getId()).isPresent()) {
            log.warn("Failed to add guest: Guest with id '{}' already exists", guest.getId());
            throw new IllegalArgumentException("Guest with id '" + guest.getId() + "' already exists");
        }

        Guest savedGuest = guestRepository.save(guest);
        log.info("Guest '{}' added successfully", guest.getId());
        
        return savedGuest;
    }

    public void deleteGuest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Guest ID cannot be null");
        }

        boolean deleted = guestRepository.deleteById(id);
        if (!deleted) {
            log.warn("Failed to delete guest: Guest with ID '{}' not found", id);
            throw new IllegalArgumentException("Guest with ID '" + id + "' not found");
        }

        log.info("Guest with ID '{}' deleted successfully", id);
    }

    public Guest updateGuest(Long id, Guest updatedGuest) {
        if (id == null || updatedGuest == null) {
            throw new IllegalArgumentException("Guest ID and updated guest information cannot be null");
        }

        Guest existingGuest = guestRepository.findById(id).orElseThrow(() -> {
            log.warn("Failed to update guest: Guest with ID '{}' not found", id);
            return new IllegalArgumentException("Guest with ID '" + id + "' not found");
        });

        existingGuest.setFirstName(updatedGuest.getFirstName());
        existingGuest.setLastName(updatedGuest.getLastName());
        existingGuest.setEmail(updatedGuest.getEmail());
        existingGuest.setPhoneNumber(updatedGuest.getPhoneNumber());
        existingGuest.setBirthDate(updatedGuest.getBirthDate());

        Guest savedGuest = guestRepository.save(existingGuest);
        log.info("Guest with ID '{}' updated successfully", id);

        return savedGuest;
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public void importFromCsv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 6) {
                    throw new IllegalArgumentException("Invalid CSV format");
                }

                Long id = Long.parseLong(fields[0]);
                String firstName = fields[1];
                String lastName = fields[2];
                String email = fields[3];
                String phoneNumber = fields[4];
                LocalDate birthDate = LocalDate.parse(fields[5]);

                Guest guest = new Guest(id, firstName, lastName, email, phoneNumber, birthDate);
                addGuest(guest);
            }
            System.out.println("Guests imported successfully from " + filePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error importing guests from CSV: " + e.getMessage());
        }
    }

    public void exportToCsv(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write(UtilityClass.getFieldNames(Guest.class));
            writer.newLine();

            for (Guest guest : guestRepository.findAll()) {
                String line = String.format("%d,%s,%s,%s,%s,%s",
                        guest.getId(),
                        guest.getFirstName(),
                        guest.getLastName(),
                        guest.getEmail(),
                        guest.getPhoneNumber(),
                        guest.getBirthDate().toString());
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Guests exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting guests to CSV: " + e.getMessage());
        }
    }
}