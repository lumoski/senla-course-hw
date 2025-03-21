package com.hotel.controller.console;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hotel.controller.api.GuestController;
import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.framework.util.ConsoleInputUtil;
import com.hotel.service.api.GuestService;
import org.springframework.stereotype.Component;

@Component
public class GuestConsoleControllerImpl implements GuestController {

    private final GuestService guestService;

    @Autowired
    public GuestConsoleControllerImpl(GuestService guestService) {
        this.guestService = guestService;
    }

    @Override
    public GuestDTO createGuest(GuestCreateDTO guestDTO) {
        return guestService.createGuest(guestDTO);
    }

    @Override
    public GuestDTO updateGuest(GuestUpdateDTO guestDTO) {
        return guestService.updateGuest(guestDTO);
    }

    @Override
    public List<GuestDTO> findAllGuests() {
        return guestService.findAllGuests();
    }

    @Override
    public void exportToCsv() {
        guestService.exportToCsv();
    }

    @Override
    public void importFromCsv() {
        // TODO:
    }

    public GuestDTO createGuest() {
        System.out.println("Create a new Guest");

        System.out.print("Enter Guest first name: ");
        String firstName = ConsoleInputUtil.readString();

        System.out.print("Enter Guest last name: ");
        String lastName = ConsoleInputUtil.readString();

        System.out.print("Enter Guest email: ");
        String email = ConsoleInputUtil.readString();

        System.out.print("Enter Guest phone: ");
        String phoneNumber = ConsoleInputUtil.readString();

        System.out.print("Enter Guest passport number: ");
        String passportNumber = ConsoleInputUtil.readString();

        System.out.print("Enter Guest gender: ");
        String gender = ConsoleInputUtil.readString();

        System.out.print("Enter Guest address: ");
        String address = ConsoleInputUtil.readString();

        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = ConsoleInputUtil.readDate();

        GuestCreateDTO createDTO = new GuestCreateDTO(
                firstName,
                lastName,
                email,
                phoneNumber,
                passportNumber,
                gender,
                address,
                date,
                "NEW"
        );

        try {
            GuestDTO savedCreatedGuest = createGuest(createDTO);

            System.out.println("\nGuest created successfully:");
            System.out.println(createDTO);

            return savedCreatedGuest;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public GuestDTO updateGuest() {
        System.out.println("Update a Guest");

        System.out.print("Enter Guest ID (Long): ");
        Long id = ConsoleInputUtil.readLong();

        System.out.print("Enter Guest first name: ");
        String firstName = ConsoleInputUtil.readString();

        System.out.print("Enter Guest last name: ");
        String lastName = ConsoleInputUtil.readString();

        System.out.print("Enter Guest email: ");
        String email = ConsoleInputUtil.readString();

        System.out.print("Enter Guest phone: ");
        String phoneNumber = ConsoleInputUtil.readString();

        System.out.print("Enter Guest passport number: ");
        String passportNumber = ConsoleInputUtil.readString();

        System.out.print("Enter Guest gender: ");
        String gender = ConsoleInputUtil.readString();

        System.out.print("Enter Guest address: ");
        String address = ConsoleInputUtil.readString();

        System.out.print("Enter a date (yyyy-MM-dd): ");
        LocalDate date = ConsoleInputUtil.readDate();

        GuestUpdateDTO updateDTO = new GuestUpdateDTO(
                id,
                firstName,
                lastName,
                email,
                phoneNumber,
                passportNumber,
                gender,
                address,
                date,
                "NEW"
        );

        try {
            GuestDTO savedUpdatedDTO = updateGuest(updateDTO);

            System.out.println("\nGuest updated successfully:");

            return savedUpdatedDTO;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
