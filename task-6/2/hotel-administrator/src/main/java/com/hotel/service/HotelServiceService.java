package com.hotel.service;

import java.util.List;

import com.hotel.model.Service;
import com.hotel.repository.ServiceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for managing hotel services.
 * Handles business logic for creating and updating hotel services.
 */
@Slf4j
@RequiredArgsConstructor
public class HotelServiceService {
    private final ServiceRepository serviceRepository;

    public Service addService(Service service) {
        if (serviceRepository.findById(service.getId()).isPresent()) {
            log.warn("Failed to add service: Service with name '{}' already exists", service.getName());
            throw new IllegalArgumentException("Service with name '" + service.getName() + "' already exists");
        }

        Service savedService = serviceRepository.save(service);
        log.info("Service '{}' added successfully with price {}", service.getName(), service.getPrice());
        
        return savedService;
    }

    public Service updateServicePrice(Long id, double newPrice) {
        Service service = serviceRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Failed to update price: Service '{}' not found", id);
                return new IllegalArgumentException("Service '" + id + "' not found");
            });

        double oldPrice = service.getPrice();
        service.setPrice(newPrice);
        
        Service updatedService = serviceRepository.save(service);
        log.info("Service '{}' price updated from {} to {}", id, oldPrice, newPrice);
        
        return updatedService;
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<Service> getAllServicesSortedByPrice() {
        return serviceRepository.findAllSortedByPrice();
    }
}