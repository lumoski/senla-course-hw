package com.hotel.repository.impl;

import com.hotel.model.Service;
import com.hotel.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryServiceRepository implements ServiceRepository {
    private final List<Service> services = new ArrayList<>();

    @Override
    public Optional<Service> findById(Long id) {
        return services.stream()
            .filter(service -> service.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<Service> findAll() {
        return new ArrayList<>(services);
    }

    @Override
    public List<Service> findAllSortedByPrice() {
        return services.stream()
                .sorted(Comparator.comparingDouble(Service::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public Service save(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        findById(service.getId())
                .ifPresent(existingService -> services.remove(existingService));

        services.add(service);
        return service;
    }

    @Override
    public boolean deleteByName(String name) {
        return services.removeIf(service -> service.getName().equalsIgnoreCase(name));
    }

    @Override
    public void importFromCsv(String filePath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'importFromCsv'");
    }

    @Override
    public void exportToCsv(String filePath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportToCsv'");
    }
}
