package com.hotel.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.hotel.framework.di.annotation.Inject;
import com.hotel.model.Service;
import com.hotel.repository.ServiceRepository;
import com.hotel.utils.UtilityClass;

import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for managing hotel services.
 * Handles business logic for creating and updating hotel services.
 */
@Slf4j
public class HotelServiceService {

    @Inject
    private ServiceRepository serviceRepository;

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

    public void importFromCsv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 4) {
                    throw new IllegalArgumentException("Invalid CSV format for Service");
                }

                Long id = Long.parseLong(fields[0]);
                String name = fields[1];
                double price = Double.parseDouble(fields[2]);
                String category = fields[3];

                Service service = new Service(id, name, price, category);
                addService(service);
            }
            System.out.println("Services imported successfully from " + filePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error importing services from CSV: " + e.getMessage());
        }
    }

    public void exportToCsv(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write(UtilityClass.getFieldNames(Service.class));
            writer.newLine();

            for (Service service : serviceRepository.findAll()) {
                String line = String.format("%d,%s,%.2f,%s",
                        service.getId(),
                        service.getName(),
                        service.getPrice(),
                        service.getCategory());
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Services exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting services to CSV: " + e.getMessage());
        }
    }
}