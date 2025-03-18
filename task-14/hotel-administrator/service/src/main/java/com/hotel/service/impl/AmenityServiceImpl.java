package com.hotel.service.impl;

import java.util.List;

import com.hotel.service.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.exception.AmenityNotFoundException;
import com.hotel.core.model.domain.Amenity;
import com.hotel.database.entity.AmenityEntity;
import com.hotel.dto.mapper.AmenityMapper;
import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdatePriceDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.framework.util.CsvExporter;
import com.hotel.repository.api.AmenityRepository;
import com.hotel.service.api.AmenityService;

@Slf4j
@Service
@PropertySource("classpath:service.properties")
public class AmenityServiceImpl implements AmenityService, CsvExporter.CsvConverter<Amenity> {

    private AmenityRepository amenityRepository;
    private AmenityMapper amenityMapper;
    private ServiceConfig serviceConfig;

    @Autowired
    public AmenityServiceImpl(AmenityRepository amenityRepository, AmenityMapper amenityMapper, ServiceConfig serviceConfig) {
        this.amenityRepository = amenityRepository;
        this.amenityMapper = amenityMapper;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public AmenityDTO findById(Long id) {
        AmenityEntity amenity = amenityRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Amenity '{}' not found", id);
                    return new AmenityNotFoundException(id);
                }
        );
        return amenityMapper.toDTO(amenity);
    }

    @Override
    public List<AmenityDTO> findAllAmenities() {
        return amenityMapper.toDTOListFromEntity(
                amenityRepository.findAll()
        );
    }

    @Override
    public List<AmenityDTO> findAllAmenitiesSortedByPrice() {
        return amenityMapper.toDTOListFromEntity(
                amenityRepository.findAllSortedByPrice()
        );
    }

    @Override
    public AmenityDTO createAmenity(AmenityCreateDTO amenityCreateDTO) {
        AmenityEntity amenity = amenityMapper.toEntity(amenityCreateDTO);
        AmenityEntity savedAmenity = amenityRepository.save(amenity);
        AmenityDTO savedAmenityDTO = amenityMapper.toDTO(savedAmenity);

        log.debug("Amenity {} with name {} added successfully with price {}",
                savedAmenity.getId(),
                savedAmenity.getName(),
                savedAmenity.getPrice());

        return savedAmenityDTO;
    }

    @Override
    public AmenityDTO updateAmenityPrice(AmenityUpdatePriceDTO amenityUpdateDTO) {
        AmenityEntity amenity = amenityRepository.findById(amenityUpdateDTO.id()).orElseThrow(
            () -> {
                log.error("Amenity '{}' not found", amenityUpdateDTO.id());
                return new AmenityNotFoundException(amenityUpdateDTO.id());
            }
        );

        double oldPrice = amenity.getPrice();
        amenity.setPrice(amenityUpdateDTO.newPrice());

        AmenityEntity updatedAmenity = amenityRepository.update(amenity);
        AmenityDTO updatedAmenityDTO = amenityMapper.toDTO(updatedAmenity);

        log.debug("Amenity '{}' price updated from {} to {}",
                amenityUpdateDTO.id(),
                oldPrice,
                amenityUpdateDTO.newPrice());

        return updatedAmenityDTO;
    }

    @Override
    public void exportToCsv() {
        CsvExporter<Amenity> exporter = new CsvExporter<>(serviceConfig.getAmenityCsvFilePath());
        exporter.export(
                amenityMapper.toDomainListFromEntity(amenityRepository.findAll()),
                this
        );

        log.debug("Amenities '{}' exported successfully", serviceConfig.getAmenityCsvFilePath());
    }

    @Override
    public String getCsvHeaderLine() {
        String[] header = {"id", "name", "price", "category"};
        return String.join(",", header);
    }

    @Override
    public String getCsvDataLine(Amenity item) {
        return String.format("%s,%s,%s,%s",
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getCategory().name()
        );
    }
}
