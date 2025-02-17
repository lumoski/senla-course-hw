package com.hotel.service.impl;

import com.hotel.dto.mapper.GuestMapper;
import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.exception.GuestNotFoundException;
import com.hotel.framework.configurator.ConfigurationManager;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.CsvExporter;
import com.hotel.model.entity.Guest;
import com.hotel.repository.api.GuestRepository;
import com.hotel.service.api.GuestService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GuestServiceImpl implements GuestService, CsvExporter.CsvConverter<Guest> {

    @Inject
    private GuestRepository guestRepository;

    @Inject
    private GuestMapper guestMapper;

    @Override
    public GuestDTO findById(Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Guest '{}' not found", id);
                    return new GuestNotFoundException(id);
                }
        );
        return guestMapper.toDTO(guest);
    }

    @Override
    public List<GuestDTO> findAllGuests() {
        return guestMapper.toDTOList(
                guestRepository.findAll()
        );
    }

    @Override
    public GuestDTO createGuest(GuestCreateDTO guestCreateDTO) {
        Guest guest = guestMapper.toEntity(guestCreateDTO);
        Guest savedGuest = guestRepository.save(guest);
        GuestDTO savedGuestDTO = guestMapper.toDTO(savedGuest);

        log.info("Guest '{}' added successfully", guest.getId());

        return savedGuestDTO;
    }

    @Override
    public GuestDTO updateGuest(GuestUpdateDTO guestUpdateDTO) {
        Guest guest = guestMapper.toEntity(guestUpdateDTO);
        Guest savedGuest = guestRepository.save(guest);
        GuestDTO updatedGuestDTO = guestMapper.toDTO(savedGuest);

        log.info("Guest '{}' updated successfully", guest.getId());

        return updatedGuestDTO;
    }

    @Override
    public void exportToCsv() {
        String filePath = ConfigurationManager.getInstance().getGuestDataPath();

        CsvExporter<Guest> exporter = new CsvExporter<>(filePath);
        exporter.export(
                guestRepository.findAll(),
                new GuestServiceImpl()
        );
    }

    @Override
    public String getCsvHeaderLine() {
        String[] header = {"id", "first_name", "last_name", "email", "phone_number",
                "passport_number", "gender", "address", "birthdate", "status"};
        return String.join(";", header);
    }

    @Override
    public String getCsvDataLine(Guest item) {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                item.getId(),
                item.getFirstName(),
                item.getLastName(),
                item.getEmail(),
                item.getPhoneNumber(),
                item.getPassportNumber(),
                item.getGender(),
                item.getAddress(),
                item.getBirthDate().toString(),
                item.getStatus().name());
    }
}
