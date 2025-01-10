package com.hotel.controller;

import java.util.List;

import com.hotel.model.Service;
import com.hotel.service.HotelServiceService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServiceController {
    private final HotelServiceService hotelServiceService;

    public Service addService(Service service) {
        validate(service);

        return hotelServiceService.addService(service);
    }

    public Service updateServicePrice(Long id, double newPrice) {
        validateId(id);
        validatePrice(newPrice);

        return hotelServiceService.updateServicePrice(id, newPrice);
    }

    public List<Service> getAllServices() {
        return hotelServiceService.getAllServices();
    }

    public List<Service> getAllServicesSortedByPrice() {
        return hotelServiceService.getAllServicesSortedByPrice();
    }

    public void importFromCsv(String filePath) {
        hotelServiceService.importFromCsv(filePath);
    }

    public void exportToCsv(String filePath) {
        hotelServiceService.exportToCsv(filePath);
    }

    private void validate(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        validateId(service.getId());
        validateName(service.getName());
        validatePrice(service.getPrice());
        validateCategory(service.getCategory());
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Service ID cannot be null");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Service price cannot be negative");
        }
    }

    private void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Service category cannot be null or empty");
        }
    }
}
