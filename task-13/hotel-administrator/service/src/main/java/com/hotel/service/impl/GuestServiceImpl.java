package com.hotel.service.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.exception.GuestNotFoundException;
import com.hotel.core.model.domain.Guest;
import com.hotel.database.entity.GuestEntity;
import com.hotel.dto.mapper.GuestMapper;
import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.framework.configurator.ConfigLoader;
import com.hotel.framework.configurator.ConfigProperty;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.CsvExporter;
import com.hotel.repository.api.GuestRepository;
import com.hotel.service.api.GuestService;

@Slf4j
public class GuestServiceImpl implements GuestService, CsvExporter.CsvConverter<Guest> {

    @Inject
    private GuestRepository<GuestEntity, Long> guestRepository;

    @Inject
    private GuestMapper guestMapper;

    @ConfigProperty
    private String csvFilePath;

    public GuestServiceImpl() {
        ConfigLoader.initialize(this, "service.properties");
    }

    @Override
    public GuestDTO findById(Long id) {
        GuestEntity guest = guestRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Guest '{}' not found", id);
                    return new GuestNotFoundException(id);
                }
        );
        return guestMapper.toDTOFromEntity(guest);
    }

    @Override
    public List<GuestDTO> findAllGuests() {
        return guestMapper.toDTOListFromEntity(
                guestRepository.findAll()
        );
    }

    @Override
    public GuestDTO createGuest(GuestCreateDTO guestCreateDTO) {
        GuestEntity guest = guestMapper.toEntityFromCreateDTO(guestCreateDTO);
        GuestEntity savedGuest = guestRepository.save(guest);
        GuestDTO savedGuestDTO = guestMapper.toDTOFromEntity(savedGuest);

        log.debug("Guests '{}' added successfully", guest.getId());

        return savedGuestDTO;
    }

    @Override
    public GuestDTO updateGuest(GuestUpdateDTO guestUpdateDTO) {
        GuestEntity guest = guestMapper.toEntityFromUpdateDTO(guestUpdateDTO);
        GuestEntity savedGuest = guestRepository.update(guest);
        GuestDTO updatedGuestDTO = guestMapper.toDTOFromEntity(savedGuest);

        log.debug("Guest '{}' updated successfully", guest.getId());

        return updatedGuestDTO;
    }

    @Override
    public void exportToCsv() {
        CsvExporter<Guest> exporter = new CsvExporter<>(csvFilePath);
        exporter.export(
                guestMapper.toDomainListFromEntity(guestRepository.findAll()),
                new GuestServiceImpl()
        );

        log.debug("Guest '{}' exported successfully", csvFilePath);
    }

    @Override
    public String getCsvHeaderLine() {
        String[] header = {"id", "first_name", "last_name", "email", "phone_number", "passport_number", "gender", "address", "birthdate", "status"};
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
