package com.hotel.service.impl;

import com.hotel.dto.mapper.AmenityMapper;
import com.hotel.dto.request.AmenityCreateDTO;
import com.hotel.dto.request.AmenityUpdateDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.exception.AmenityNotFoundException;
import com.hotel.framework.configurator.ConfigurationManager;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.CsvExporter;
import com.hotel.model.entity.Amenity;
import com.hotel.repository.api.AmenityRepository;
import com.hotel.service.api.AmenityService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class AmenityServiceImpl implements AmenityService, CsvExporter.CsvConverter<Amenity> {

    @Inject
    private AmenityRepository amenityRepository;

    @Inject
    private AmenityMapper amenityMapper;

    @Override
    public AmenityDTO findById(Long id) {
        Amenity amenity = amenityRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Amenity '{}' not found", id);
                    return new AmenityNotFoundException(id);
                }
        );
        return amenityMapper.toDTO(amenity);
    }

    @Override
    public List<AmenityDTO> findAllAmenities() {
        return amenityMapper.toDTOList(
                amenityRepository.findAll()
        );
    }

    @Override
    public List<AmenityDTO> findAllAmenitiesSortedByPrice() {
        return amenityMapper.toDTOList(
                amenityRepository.findAllSortedByPrice()
        );
    }

    @Override
    public AmenityDTO createAmenity(AmenityCreateDTO amenityCreateDTO) {
        Amenity amenity = amenityMapper.toEntity(amenityCreateDTO);
        Amenity savedAmenity = amenityRepository.save(amenity);
        AmenityDTO savedAmenityDTO = amenityMapper.toDTO(savedAmenity);

        log.info("Service {} with name {} added successfully with price {}",
                savedAmenity.getId(),
                savedAmenity.getName(),
                savedAmenity.getPrice());

        return savedAmenityDTO;
    }

    @Override
    public AmenityDTO updateAmenityPrice(AmenityUpdateDTO amenityUpdateDTO) {
        Amenity amenity = amenityMapper.toEntity(findById(amenityUpdateDTO.id()));

        double oldPrice = amenity.getPrice();
        amenity.setPrice(amenityUpdateDTO.newPrice());

        Amenity updatedAmenity = amenityRepository.updatePrice(amenity);
        AmenityDTO updatedAmenityDTO = amenityMapper.toDTO(updatedAmenity);

        log.info("Service '{}' price updated from {} to {}",
                amenityUpdateDTO.id(),
                oldPrice,
                amenityUpdateDTO.newPrice());

        return updatedAmenityDTO;
    }

    @Override
    public void exportToCsv() {
        String filePath = ConfigurationManager.getInstance().getServiceDataPath();

        CsvExporter<Amenity> exporter = new CsvExporter<>(filePath);
        exporter.export(
                amenityRepository.findAll(),
                new AmenityServiceImpl()
        );
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
