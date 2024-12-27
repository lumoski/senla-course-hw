package com.hotel.service;

import com.hotel.model.Service;
import com.hotel.repository.ServiceRepository;


public class HotelServiceService {
    private final ServiceRepository serviceRepo;

    public HotelServiceService(ServiceRepository serviceRepo) {
        this.serviceRepo = serviceRepo;
    }

    public void addService(String name, double price) {
        serviceRepo.save(new Service(name, price));
        System.out.println("Service " + name + " added.");
    }

    public void changeServicePrice(String name, double price) {
        Service service = serviceRepo.findByName(name);
        
        if (service == null) {
            System.out.println("Service " + name + " not found");
        }

        service.setPrice(price);
        serviceRepo.save(service);
        System.out.println("Service " + name + " has a new price " + price);
    }

}
