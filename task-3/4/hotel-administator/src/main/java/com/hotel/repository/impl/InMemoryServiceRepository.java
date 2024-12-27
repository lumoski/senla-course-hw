package com.hotel.repository.impl;

import com.hotel.model.Service;
import com.hotel.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;


public class InMemoryServiceRepository implements ServiceRepository {
    private final List<Service> services = new ArrayList<>();

    @Override
    public Service findByName(String name) {
        return services
            .stream()
            .filter(s -> s.getName().equals(name))
            .findFirst().orElse(null);
    }

    @Override
    public List<Service> findAll() {
        return new ArrayList<>(services);
    }

    @Override
    public void save(Service service) {
        services.stream()
            .filter(x -> x.getName() == service.getName())
            .findFirst()
            .ifPresentOrElse(
                x -> x = service,
                () -> services.add(service)
            );
    }

    @Override
    public void deleteByName(String name) {
        services.removeIf(s -> s.getName().equals(name));
    }
}
