package com.hotel.service;

import java.util.Scanner;

import com.hotel.model.Service;
import com.hotel.repository.ServiceRepository;

public class ConsoleHotelServiceService extends HotelServiceService {
    private final Scanner scanner;

    public ConsoleHotelServiceService(ServiceRepository serviceRepo, Scanner scanner) {
        super(serviceRepo);
        this.scanner = scanner;
    }

    public Service consoleAddService() {
        System.out.println("Create a new Service");
    
        System.out.print("Enter Service ID (Long): ");
        Long id = scanner.nextLong();
        scanner.nextLine();
    
        System.out.print("Enter Service first name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Service price (Long): ");
        double price = scanner.nextDouble();

        System.out.print("Enter Service category: ");
        String category = scanner.nextLine();

        Service service = new Service(id, name, price, category);

        return addService(service);
    }

    public Service consoleUpdateServicePrice() {
        System.out.print("Enter Service Name: ");
        String name = scanner.nextLine();
        scanner.nextLine();

        System.out.print("Enter new Service price (Long): ");
        double price = scanner.nextDouble();

        return updateServicePrice(name, price);
    }
}
