package com.hotel.service;

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
        validateServiceParameters(service);

        if (serviceRepository.findByName(service.getName()).isPresent()) {
            log.warn("Failed to add service: Service with name '{}' already exists", service.getName());
            throw new IllegalArgumentException("Service with name '" + service.getName() + "' already exists");
        }

        Service savedService = serviceRepository.save(service);
        log.info("Service '{}' added successfully with price {}", service.getName(), service.getPrice());
        
        return savedService;
    }

    public Service updateServicePrice(String name, double newPrice) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        Service service = serviceRepository.findByName(name)
            .orElseThrow(() -> {
                log.warn("Failed to update price: Service '{}' not found", name);
                return new IllegalArgumentException("Service '" + name + "' not found");
            });

        double oldPrice = service.getPrice();
        service.setPrice(newPrice);
        
        Service updatedService = serviceRepository.save(service);
        log.info("Service '{}' price updated from {} to {}", name, oldPrice, newPrice);
        
        return updatedService;
    }

    private void validateServiceParameters(Service service) {
        if (service.getId() == null) {
            throw new IllegalArgumentException("Service ID cannot be null");
        }

        if (service.getName() == null || service.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
        
        if (service.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}